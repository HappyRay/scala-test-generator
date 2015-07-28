package com.HappyRay.scalaTestGenerator

import java.io.{File, PrintWriter}
import scala.collection.JavaConversions._

/**
 * Responsible for the main flow.
 */
class Engine(config: Config) {

  /**
   * Main entry point. Staring point
   */
  def run() = {
    Global.writeln("Start engine.")
    val validatedConfig = new ValidatedConfig(config)
    val rc = executeMethodUnderTest(validatedConfig)
    val renderer = new Renderer(validatedConfig)
    val result = renderer.render(rc)
    Global.writeln("Generated test text : \n" + result)
    writeToFile(result, validatedConfig)
  }

  private def writeToFile(outputString: String, validatedConfig: ValidatedConfig) = {
    // @TODO (ryang 9/4/14) : make output path configurable.
    val outClassName = validatedConfig.outClassName
    val outputFilePath = s"src/test/scala/com/HappyRay/scalaTestGenerator/$outClassName.scala"
    val writer = new PrintWriter(new File(outputFilePath))
    writer.write(outputString)
    writer.close()
    Global.writeln(s"Wrote generated test to the file : ( $outputFilePath ).")
  }

  /**
   * Execute the method under test and
   * get the result.
   */
  private def executeMethodUnderTest(validatedConfig: ValidatedConfig): Any = {
    val instance = validatedConfig.classUnderTest.newInstance()
    val arguments = validatedConfig.methodArgumentsObj.arguments

    // The Java reflection API expects Object array.
    // Convert the Array[Any] to Array[Object].
    val argumentsAsObjects = arguments.map(convertArgumentToObject(_))
    val result = validatedConfig.methodUnderTest.invoke(instance, argumentsAsObjects: _*);
    Global.writeln("Method invocation result is :" + result)
    result
  }

  private def convertArgumentToObject(arg: Any): Object = {
    if (arg.isInstanceOf[MockClass]) {
      val mockClassObj = arg.asInstanceOf[MockClass]
      val mockedObj = mockClassObj.getMockObject()
      val obj = mockedObj.asInstanceOf[Object]
      obj
    } else {
      val obj = arg.asInstanceOf[Object]
      obj
    }
  }
}
