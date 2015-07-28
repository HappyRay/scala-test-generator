package com.HappyRay.scalaTestGenerator.fixture.testInput

/**
 * Test class for testing simple test generation scenarios.
 */
class Calculator {

  def add(num1: Int, num2: Int): Int = {
    val total = num1 + num2
    total
  }

  def add2And3() = {
    val total = add(2, 3)
    total
  }
}
