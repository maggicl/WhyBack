val scala3Version = "3.3.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "byteback",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies += "org.soot-oss" % "sootup.core" % "1.1.2" % Compile,
    libraryDependencies += "org.soot-oss" % "sootup.java.core" % "1.1.2" % Compile,
    libraryDependencies += "org.soot-oss" % "sootup.java.bytecode" % "1.1.2" % Compile exclude(
      "com.github.ThexXTURBOXx.dex2jar",
      "dex-tools"
    ),
    dependencyOverrides += "de.femtopedia.dex2jar" % "dex2jar" % "2.4.11" % Compile,
    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
