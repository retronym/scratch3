scalaVersion := "2.10.2"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.2.0-RC2",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "org.slf4j" % "slf4j-nop" % "1.7.5"
)

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

