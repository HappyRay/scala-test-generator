package com.HappyRay.scalaTestGenerator.fixture.testInput

/**
 * Test class for testing simple test generation scenarios.
 */
class Misc {

  def useCalculator(calc: Calculator) = {
    val rc = calc.add2And3()

    rc
  }

  def userSays(user: User, words: String) = {
    val statement = user.getName() + " says " + words
    statement
  }
}
