package com.HappyRay.scalaTestGenerator

/**
 * Class to generate the output code.
 */
class Renderer(val validatedConfig: ValidatedConfig) {

  def render(executionResult: Any) = {
    val methodName = validatedConfig.methodUnderTest.getName;
    val className = validatedConfig.classUnderTest.getSimpleName;
    val triplequotes = "\"\"\""
    val methodText = renderMethod(
      className,
      methodName,
      executionResult
    )
    val importText = renderImportStatements()
    val descriptionText = renderTestDescription(executionResult)

    // String interpolation
    // @see http://docs.scala-lang.org/overviews/core/string-interpolation.html
    // is not used because the difficulty in escaping """.
    val outputString =
      """
        |%s
        |class %sGenTest extends Specification with Mockito {
        |    def is: Fragments = s2%s
        | %s $test%s
        | %s
        |%s
        |}
      """.stripMargin.format(
            importText,
            className,
            triplequotes,
            descriptionText,
            methodName,
            triplequotes,
            methodText
          )
    outputString
  }

  private def renderTestDescription(executionResult: Any) = {
    val className = validatedConfig.classUnderTest.getSimpleName
    val methodName = validatedConfig.methodUnderTest.getName
    var argumentStringBuilder: StringBuilder = new StringBuilder()
    val arguments = validatedConfig.methodArgumentsObj.arguments
    for (arg <- arguments) {
      if (arg.isInstanceOf[MockClass]) {
        val mock = arg.asInstanceOf[MockClass]
        val classNameBeingMocked = mock.classBeingMocked.getSimpleName
        val mockClassText = s"(Mocked $classNameBeingMocked) "
        argumentStringBuilder.append(mockClassText)
      } else {
        val valueString = arg.toString + " "
        argumentStringBuilder.append(valueString)
      }
    }
    val argumentsText = argumentStringBuilder.toString()
    val resultAsString = executionResult.toString
    val descriptionText =
      s"When method ( $methodName ) of the Class ( $className ) is" +
          s" called with arguments: ( $argumentsText ) should return ( $resultAsString )"

    descriptionText
  }

  private def renderImportStatements() = {
    // @TODO (ryang 9/5/14) : create proper package information
    val importText =
      s"""
         |package com.HappyRay.scalaTestGenerator
         |
         |import com.HappyRay.scalaTestGenerator.fixture.testInput._
         |import org.specs2.Specification
         |import org.specs2.mock.Mockito
         |import org.specs2.specification.Fragments
            """.stripMargin

    importText

  }

  private def renderMethod(className: String,
                           methodName: String,
                           executionResult: Any) = {
    val resultAsString = executionResult.toString
    val arguments = validatedConfig.methodArgumentsObj.arguments
    val argumentsAsString = arguments.map(convertArgumentValueToString(_)).mkString(", ")
    val mockingObjectsStatementsString = renderMockingObjects()

    val methodText =
      s"""
         |def test$methodName()= {
                               |     $mockingObjectsStatementsString
            |     val obj = new $className()
                                            |     val rc = obj.$methodName($argumentsAsString)
                                                                                               |     val rcAsString = rc.toString()
                                                                                               |     rcAsString mustEqual ("$resultAsString")
                                                                                                                                             | }
            """.stripMargin

    methodText
  }

  private def convertArgumentValueToString(arg: Any): String = {
    if (arg.isInstanceOf[MockClass]) {
      val mockObj = arg.asInstanceOf[MockClass]
      mockObj.mockObjectName
    } else {
      val valueString = convertValueToValidStringRepresentationInCode(arg)
      valueString
    }
  }

  private def renderMockingObjects() = {
    val arguments = validatedConfig.methodArgumentsObj.arguments
    val mockedArguments = arguments.filter(_.isInstanceOf[MockClass]).map(_.asInstanceOf[MockClass])
    val mockStatements = for (arg <- mockedArguments) yield renderMockingAnObject(arg)
    val mockingObjectsStatementsString = mockStatements.mkString("")
    mockingObjectsStatementsString
  }

  private def renderMockingAnObject(arg: MockClass) = {
    val mockObjectName = arg.mockObjectName
    val classTypeBeingMocked = arg.classBeingMocked.getSimpleName
    val methodExpecationStatements = renderMethodExpectations(arg)

    val mockObjectText =
      s"""
         |val $mockObjectName = mock[$classTypeBeingMocked]
                                                            |$methodExpecationStatements
             """.stripMargin

    mockObjectText
  }

  private def renderMethodExpectations(arg: MockClass) = {
    val mockObjectName = arg.mockObjectName
    val invocations = arg.getInvocations()
    val expectations = for (inv <- invocations) yield renderOneExpectation(inv, arg)
    val expecationsText = expectations.mkString("")
    expecationsText
  }

  private def renderOneExpectation(inv: MethodInvocation, arg: MockClass) = {
    val mockObjectName = arg.mockObjectName
    val methodName = inv.invocation.getMethod.getName
    val arguments = inv.invocation.getArguments
    var argumentString = ""
    if (arguments.length > 0) {
      argumentString = arguments.map(_.toString).mkString(", ")
    }
    val returnValueString = convertValueToValidStringRepresentationInCode(inv.returnValue)
    // @TODO (ryang 9/5/14) : if the invocation is on a public property
    // don't add ()
    val expectationText =
      s"""
         |$mockObjectName.$methodName($argumentString).returns($returnValueString)
             """.stripMargin

    expectationText
  }

  /**
   * Return the string representation of the value that can be used in the code.
   * @param value
   */
  private def convertValueToValidStringRepresentationInCode(value: Any) = {
    val valueAsString = value.toString
    if (value.isInstanceOf[String]) {
      // Suround the string with " "
      val quote = "\""
      val stringValue = s"$quote$valueAsString$quote"
      stringValue
    } else {
      valueAsString
    }
  }
}
