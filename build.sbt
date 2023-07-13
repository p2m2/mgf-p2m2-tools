
scalaVersion := "2.13.11"
name := "mgf-parser"
organization := "com.github.p2m2"
version := "1.0"
organizationName := "p2m2"
organizationHomepage := Some(url("https://www6.inrae.fr/p2m2"))
licenses := Seq("MIT License" -> url("http://www.opensource.org/licenses/mit-license.php"))

scmInfo := Some(
  ScmInfo(
    url("https://github.com/p2m2/positional-carbon13-enrichment"),
    "scm:git@github.com:p2m2/positional-carbon13-enrichment.git"
  )
)

developers := List(
  Developer("ofilangi", "Olivier Filangi", "olivier.filangi@inrae.fr",url("https://github.com/ofilangi"))
)

lazy val root = (project in file(".")).
  //enablePlugins(ScalaJSPlugin).
  // add the `it` configuration
  //configs(IntegrationTest).
  // add `it` tasks
  //settings(Defaults.itSettings: _*).
  // add Scala.js-specific settings and tasks to the `it` configuration
  //settings(inConfig(IntegrationTest)(ScalaJSPlugin.testConfigSettings): _*).
  settings(
    credentials += {

      val realm = scala.util.Properties.envOrElse("REALM_CREDENTIAL", "")
      val host = scala.util.Properties.envOrElse("HOST_CREDENTIAL", "")
      val login = scala.util.Properties.envOrElse("LOGIN_CREDENTIAL", "")
      val pass = scala.util.Properties.envOrElse("PASSWORD_CREDENTIAL", "")

      val file_credential = Path.userHome / ".sbt" / ".credentials"

      if (reflect.io.File(file_credential).exists) {
        Credentials(file_credential)
      } else {
        Credentials(realm, host, login, pass)
      }
    },
    publishTo := {
      if (isSnapshot.value)
        Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/content/repositories/snapshots")
      else
        Some("Sonatype Snapshots Nexus" at "https://oss.sonatype.org/service/local/staging/deploy/maven2")
    },
    publishConfiguration := publishConfiguration.value.withOverwrite(true),
    publishLocalConfiguration := publishLocalConfiguration.value.withOverwrite(true),
    pomIncludeRepository := { _ => false },
    publishMavenStyle := true,
    coverageMinimumStmtTotal := 20,
    coverageFailOnMinimum := false,
    coverageHighlighting := true,
    libraryDependencies ++= Seq(
      "com.lihaoyi" %%% "utest" % "0.8.1" % Test,
      "com.lihaoyi" %%% "scalatags" % "0.12.0",
    ),
    testFrameworks += new TestFramework("utest.runner.Framework")
  )

Global / onChangedBuildSource := ReloadOnSourceChanges

