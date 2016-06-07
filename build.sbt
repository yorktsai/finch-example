import sbt._
import sbt.Keys._

lazy val IntegrationTest = config("it") extend(Test)

lazy val root = (project in file("."))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    name := "finch-example",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.github.finagle" %% "finch-core" % "0.10.0",
      "com.github.finagle" %% "finch-jackson" % "0.10.0",
      "com.github.finagle" %% "finch-test" % "0.10.0" % "it,test",
      "com.softwaremill.macwire" %% "macros" % "2.2.3" % "provided",
      "com.softwaremill.macwire" %% "util" % "2.2.3",
      "com.softwaremill.macwire" %% "proxy" % "2.2.3",
      "com.twitter" %% "finagle-stats" % "6.35.0",
      "com.twitter" %% "twitter-server" % "1.20.0",
      "com.twitter" %% "util-eval" % "6.34.0",
      "com.typesafe" % "config" % "1.3.0",
      "org.slf4j" % "slf4j-api" % "1.7.21",
      "org.slf4j" % "jul-to-slf4j" % "1.7.21",
      "org.slf4j" % "jcl-over-slf4j" % "1.7.21",
      "org.slf4j" % "log4j-over-slf4j" % "1.7.21",
      "org.scalactic" %% "scalactic" % "2.2.1" % "it,test",
      "org.scalatest" %% "scalatest" % "2.2.1" % "it,test",
      "org.scalacheck" %% "scalacheck" % "1.13.1" % "it,test",
      "ru.arkoit" %% "finchrich-controller" % "0.1.1"
    ),
    resolvers ++= Seq(
      "Artima Maven Repository" at "http://repo.artima.com/releases",
      "twttr" at "https://maven.twttr.com/"
    ),
    // disable test whem assembly
    test in assembly := {},
    // disable parallel execution in test because SharedSparkContext does not support
    parallelExecution in test := false,
    parallelExecution in testOnly := false,
    // add integration test code to Eclipse src folder
    EclipseKeys.configurations := Set(Compile, Test, IntegrationTest)
  )
