lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.6",
      version      := "1.1"
    )),
    name := "dinosaurus",
    mainClass in (Compile, run) := Some("com.archmage.dinosaurus.Main"),
    mainClass in assembly := Some("com.archmage.dinosaurus.Main"),
    assemblyJarName in assembly := "Dinosaurus.jar",
    libraryDependencies ++= Seq (
      "com.github.austinv11" % "Discord4J" % "2.10.1",
      "org.scalaj" %% "scalaj-http" % "2.3.0",
      "org.json4s" %% "json4s-native" % "3.6.0",
      "org.json4s" %% "json4s-jackson" % "3.6.0"
    )
  )

resolvers += "jcenter" at "http://jcenter.bintray.com"
resolvers += "jitpack.io" at "https://jitpack.io"
