import Dependencies._

ThisBuild / scalaVersion := "2.13.11"
ThisBuild / version := "0.1.0-SNAPSHOT"

scalacOptions ++= Seq(
  "-Ymacro-annotations"
)

lazy val root = (project in file("."))
  .settings(
    name := "cinemotion",
    libraryDependencies ++= Seq(
      scalaTest,
      catsEffect,
      newType,
      pureConfig,
      liquibase,
      os
    ) ++ tofu.modules ++ tofu.loggingModules
      ++ akka.modules ++ telegramium.modules ++ doobie.modules
      ++ circe.modules
  )

dependencyOverrides += "io.circe" %% "circe-core" % "0.14.5"
