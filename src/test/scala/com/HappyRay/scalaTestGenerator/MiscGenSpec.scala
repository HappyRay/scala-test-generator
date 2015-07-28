

package com.HappyRay.scalaTestGenerator

import com.HappyRay.scalaTestGenerator.fixture.testInput._
import org.specs2.Specification
import org.specs2.mock.Mockito
import org.specs2.specification.Fragments

class MiscGenTest extends Specification with Mockito {
  def is: Fragments = s2"""
 When method ( userSays ) of the Class ( Misc ) is called with arguments: ( (Mocked User) world  ) should return ( Alice says world ) $testuserSays
 """

  def testuserSays() = {

    val mockArg0 = mock[User]

    mockArg0.getName().returns("Alice")


    val obj = new Misc()
    val rc = obj.userSays(mockArg0, "world")
    val rcAsString = rc.toString()
    rcAsString mustEqual ("Alice says world")
  }

}
