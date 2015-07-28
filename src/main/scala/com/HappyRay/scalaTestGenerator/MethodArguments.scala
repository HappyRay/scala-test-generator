package com.HappyRay.scalaTestGenerator

import java.lang.reflect.Method

/**
 * Get arguments array of the given method
 */
class MethodArguments(val classUnderTest: Class[_],
                      val methodUnderTest: Method) {

  val arguments = getArguments()

  /**
   * Return the values of the arguments to the given method.
   * @return
   */
  private def getArguments(): Array[Any] = {
    val args = methodUnderTest.getParameterTypes
    if (args.length > 0) {
      val msg = "Gathering arguments"
      Global.writeln(msg)
    }
    val arguments =
      for ((arg, i) <- args.zipWithIndex) yield getOneArgument(arg, i)

    arguments
  }

  private def getOneArgument(argumentType: Class[_], index: Int): Any = {
    // @TODO (ryang 9/5/14) : Display parameter name
    if (argumentType.isPrimitive || argumentType.getName == "java.lang.String") {
      Global.writeln(s"Get argument #$index value")
      val rc = UserInput.getValue(argumentType)
      rc
    } else {
      val mockObjName = "mockArg" + index.toString
      val className = argumentType.getName
      Global.writeln(s"Mock argument #$index of type $className")
      val mockClassObj = new MockClass(argumentType, mockObjName)
      mockClassObj
    }
  }
}
