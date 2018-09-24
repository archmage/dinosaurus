import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "dinosaurus",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "com.github.austinv11" % "Discord4J" % "2.10.1"
  )

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"
