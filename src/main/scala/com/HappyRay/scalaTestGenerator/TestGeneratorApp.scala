package com.HappyRay.scalaTestGenerator

/**
 * The entry point to the application.
 */
object TestGeneratorApp {

  def main(args: Array[String]): Unit = {
    val sourceJarFilePath = args(0)
    Global.writeln("Path of the source file being tested : " + sourceJarFilePath)
    val methodName = args(1)
    Global.writeln("Name of the method being tested : " + methodName)
    val className = args(2)
    Global.writeln("Name of the class being tested : " + className)
    val config = new Config(
      sourceJarFilePath,
      methodName,
      className
    )
    val engine = new Engine(config)
    engine.run()
  }
}
