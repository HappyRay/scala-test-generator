name := "scala-test-generator"

version := "1.0"

libraryDependencies ++=
    Seq(
      "org.specs2" %% "specs2" % "2.3.11" % "test",
      "org.mockito" % "mockito-core" % "1.9.5"
    )
