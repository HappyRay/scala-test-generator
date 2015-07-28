package com.HappyRay.scalaTestGenerator

import com.HappyRay.scalaTestGenerator.fixture.testInput.Calculator
import org.specs2.Specification
import org.specs2.specification.Fragments

/**
 *
 */
class CalculatorSpec extends Specification {
  def is: Fragments = s2"""
        Calcualtor should
        add 2 and 3 correctly $add2and3test
    """

  def add2and3test() = {
    val calc = new Calculator()
    val rc = calc.add2And3()
    rc mustEqual (5)
  }

}
