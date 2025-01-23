name := "expense-tracker"

version := "0.1"

scalaVersion := "2.13.15"

Compile / compile / scalacOptions ++= Seq(
  "-Werror",
  "-Wdead-code",
  "-Wextra-implicit",
  "-Wnumeric-widen",
  "-Wunused",
  "-Wvalue-discard",
  "-Xlint",
  "-Xlint:-byname-implicit",
  "-Xlint:-implicit-recursion",
  "-unchecked",
)

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % "0.23.17",
  "org.http4s" %% "http4s-blaze-server" % "0.23.17",
  "co.fs2" %% "fs2-core" % "3.10.2",
  "org.http4s" %% "http4s-circe" % "0.23.17",
  "org.http4s" %% "http4s-ember-client" % "0.23.17",
  "org.http4s" %% "http4s-blaze-client" % "0.23.17",
  "org.postgresql" % "postgresql" % "42.7.3",
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC2",
  "org.typelevel" %% "cats-effect" % "3.5.7",
  "org.typelevel" %% "cats-core" % "2.12.0",
  "io.circe" %% "circe-generic" % "0.14.9",
  "ch.qos.logback" % "logback-classic" % "1.5.6",
"org.scalatest" %% "scalatest" % "3.2.19" % Test,
)

dependencyOverrides ++= Seq(
  "co.fs2" %% "fs2-core" % "3.10.2",
  "co.fs2" %% "fs2-io" % "3.10.2",
  "org.typelevel" %% "cats-effect" % "3.5.7"
)

enablePlugins(AssemblyPlugin)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

assembly / assemblyJarName := "expense-tracker.jar"

assembly / target := file("target/assembly")
