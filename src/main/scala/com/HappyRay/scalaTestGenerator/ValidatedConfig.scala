package com.HappyRay.scalaTestGenerator

import java.io.File
import java.lang.reflect.Method
import java.net.URLClassLoader

/**
 * The extended validated configuration that contains objects instead of simple strings
 */
class ValidatedConfig(val config: Config) {
  val classUnderTest = loadClassUnderTest()
  val methodUnderTest = getMethodUnderTest(classUnderTest)
  val outClassName = classUnderTest.getSimpleName + "GenSpec"

  // The arguments to the method under test.
  val methodArgumentsObj = new MethodArguments(
    classUnderTest,
    methodUnderTest
  )

  /**
   *
   * @param classUnderTest
   * @return
   */
  private def getMethodUnderTest(classUnderTest: Class[_]): Method = {
    val methods = classUnderTest.getMethods
    val matchedMethods = methods.filter(_.getName == config.methodName)
    if (matchedMethods.length != 1) {
      // @TODO (ryang 9/4/14) : handle method overloading
      val msg = "Failed to find the only method in the test class that matches the test method name."
      throw new GeneratorException(msg)
    }
    val methodUnderTest = matchedMethods(0)

    methodUnderTest
  }

  /**
   * Load the class under test and return it.
   */
  private def loadClassUnderTest(): Class[_] = {
    val sourceFile = new File(config.sourceJarFilePath)
    val url = sourceFile.toURI().toURL()
    val loader = new URLClassLoader(Array(url))
    val classUnderTest = loader.loadClass(config.className)

    classUnderTest
  }
}
