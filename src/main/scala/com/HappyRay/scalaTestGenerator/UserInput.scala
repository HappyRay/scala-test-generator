package com.HappyRay.scalaTestGenerator

/**
 * Get user inputs
 */
object UserInput {

  /**
   * Get a value from users of the given type
   */
  def getValue(valueType: Class[_]) = {
    valueType.getName match {
      case "int" => getInteger()
      case "java.lang.String" => getString()
    }
  }

  /**
   * Get a string from users.
   *
   * @return
   */
  def getString(): String = {
    Global.writeln("Please type a string.")
    val ln = Global.readln()
    ln
  }

  /**
   * Get an integer from users.
   * @return
   */
  def getInteger(): Int = {
    Global.writeln("Please type an integer.")
    val str = getString()
    // @TODO (ryang 9/4/14) : handle errors.
    val rc = str.toInt

    rc
  }
}
