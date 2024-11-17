scalaVersion := "3.5.2"

val AkkaVersion = "2.10.0"
val AkkaHttpVersion = "10.7.0"
scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked"
)

enablePlugins(
  JavaAppPackaging,
  DockerPlugin
)

Compile / mainClass := Some("com.pirale.raft.Main")
Compile / PB.targets := Seq(
  scalapb.gen() -> (Compile / sourceManaged).value / "scalapb"
)
Docker / packageName := "pirale/raft-node"
dockerExposedPorts ++= Seq(8761, 7654)
dockerEnvVars ++= Map(("COCKROACH_HOST", "dev.localhost"), ("COCKROACH_PORT", "26257"))
dockerExposedVolumes := Seq("/opt/docker/.logs", "/opt/docker/.keys")

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
  "org.scalameta" %% "munit" % "1.0.0" % Test,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.5.7",
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf"
)

Test / parallelExecution := false
