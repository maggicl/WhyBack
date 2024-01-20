val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "byteback",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.soot-oss" % "sootup.core" % "1.0.0" % Compile,
    libraryDependencies += "org.soot-oss" % "sootup.java.core" % "1.0.0" % Compile,
    libraryDependencies += "org.soot-oss" % "sootup.java.bytecode" % "1.0.0" % Compile,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
