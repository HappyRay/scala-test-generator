package com.HappyRay.scalaTestGenerator

import org.mockito.invocation.InvocationOnMock

/**
 * Represent a method invocation of a mocked object.
 */
class MethodInvocation(val invocation: InvocationOnMock,
                       val returnValue: Any) {

}
