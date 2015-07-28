package com.HappyRay.scalaTestGenerator

/**
 * Holds global states and methods.
 */
object Global {

  private def write(msg: String): Unit = {
    print(msg)
  }

  /**
   * Write the given string and append a new line character at the end.
   *
   * @param msg
   */
  def writeln(msg: String): Unit = {
    write(msg + "\n")
  }

  /**
   * Read a line of text
   */
  def readln() = {
    val ln = readLine()
    ln
  }
}
