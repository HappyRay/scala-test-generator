package com.HappyRay.scalaTestGenerator.manualTest

import com.HappyRay.scalaTestGenerator.fixture.testInput.Calculator

/**
 *
 */

object TestCalculator {
  def main(args: Array[String]) = {
    val calc = new Calculator()
    val total = calc.add(1, 2)
    println("total is: " + total)
  }
}
