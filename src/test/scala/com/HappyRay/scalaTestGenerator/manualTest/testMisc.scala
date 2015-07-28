package com.HappyRay.scalaTestGenerator.manualTest

import com.HappyRay.scalaTestGenerator.fixture.testInput.{Calculator, Misc}

/**
 *
 */
object testMisc {
  def main(args: Array[String]): Unit = {
    testCalc()
  }

  def testCalc() = {
    val miscObj = new Misc()
    val calc = new Calculator()
    val rc = miscObj.useCalculator(calc)

    println(rc)
  }

}
