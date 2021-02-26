name := "akka-actor-chat"
version := "0.1"
scalaVersion := "2.13.3"

val AkkaVersion = "2.6.10"
val AkkaHttpVersion = "10.2.2"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion
libraryDependencies += "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % AkkaVersion
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.0"
libraryDependencies += "de.heikoseeberger" %% "akka-http-play-json" % "1.31.0"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0" % Test
libraryDependencies += "org.scalamock" %% "scalamock" % "4.4.0" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test
libraryDependencies += "com.typesafe.akka" %% "akka-http-testkit" % AkkaHttpVersion % Test
