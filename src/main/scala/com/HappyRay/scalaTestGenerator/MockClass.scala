package com.HappyRay.scalaTestGenerator

import org.mockito.Mockito
import org.mockito.invocation.InvocationOnMock
import org.mockito.stubbing.Answer
import scala.collection.mutable.ArrayBuffer

/**
 * Represent a class being mocked
 */
class MockClass(val classBeingMocked: Class[_],
                val mockObjectName: String) {

  private var methodInvocations: ArrayBuffer[MethodInvocation] = ArrayBuffer[MethodInvocation]()

  def getMockObject() = {
    val customAnswer = new Answer[Any] {
      def answer(invocation: InvocationOnMock) = {
        val methodName = invocation.getMethod.getName
        val arguments = invocation.getArguments
        val simpleClassName = classBeingMocked.getSimpleName
        val msgPartial = s"Method ( $methodName ) of the class ( $simpleClassName )" +
            s" with the name ( $mockObjectName ) is called with "

        if (arguments.length > 0) {
          val argumentStrings = arguments.map(_.toString)
          val argumentString = argumentStrings.mkString(", ")
          Global.writeln(s"$msgPartial arguments ( $argumentString ).")
        } else {
          Global.writeln(s"$msgPartial no argument.")
        }
        // @TODO (ryang 9/4/14) : handle method with no return value.
        Global.writeln("Please provide the value for the return value.")
        val returnType = invocation.getMethod.getReturnType
        val returnValue = UserInput.getValue(returnType)
        methodInvocations += new MethodInvocation(invocation, returnValue)
        returnValue
      }
    }
    val mockedObject = Mockito.mock(classBeingMocked, customAnswer)

    mockedObject
  }

  def getInvocations() = {
    methodInvocations.toArray
  }
}
