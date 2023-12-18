import sbt.*

object Dependencies {
  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  val catsEffect = "org.typelevel" %% "cats-effect" % "3.4.8"
  val newType = "io.estatico" %% "newtype" % "0.4.4"
  val pureConfig = "com.github.pureconfig" %% "pureconfig" % "0.17.4"
  val liquibase = "org.liquibase" % "liquibase-core" % "4.20.0"
  val os = "com.lihaoyi" %% "os-lib" % "0.9.1"

  object telegramium {
    val telegramiumVersion = "8.69.0"
    val modules: List[ModuleID] = List(
      "io.github.apimorphism" %% "telegramium-core" % telegramiumVersion,
      "io.github.apimorphism" %% "telegramium-high" % telegramiumVersion
    )
  }

  object doobie {
    val doobieVersion = "1.0.0-RC1"
    val modules: List[ModuleID] = List(
      "org.tpolecat" %% "doobie-core" % doobieVersion,
      "org.tpolecat" %% "doobie-postgres" % doobieVersion,
      "org.tpolecat" %% "doobie-hikari" % doobieVersion,
      "org.tpolecat" %% "doobie-specs2" % doobieVersion
    )
  }

  object circe {
    val modules: List[ModuleID] = List(
      "org.latestbit" %% "circe-tagged-adt-codec" % "0.10.0",
      "io.circe" %% "circe-generic-extras" % "0.14.3"
    )
  }
  object tethys {
    val tethysVersion = "0.26.0"
    val modules: List[ModuleID] = List(
      "com.tethys-json" %% "tethys" % tethysVersion,
      "com.tethys-json" %% "tethys-derivation" % tethysVersion,
      "com.tethys-json" %% "tethys-jackson" % tethysVersion,
      "com.tethys-json" %% "tethys-json4s" % tethysVersion
    )
  }

  object akka {
    val akkaVersion = "2.8.0"
    val akkaHttpVersion = "10.5.0"
    val modules: List[ModuleID] = List(
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
    )
  }

  object tofu {
    val tofuVersion = "0.12.0.1"
    val modules: List[ModuleID] = List(
      // "tf.tofu" %% "derevo-circe" % "0.13.0",
      "tf.tofu" %% "tofu-core-ce3" % tofuVersion,
      "tf.tofu" %% "tofu-kernel" % tofuVersion
    )

    val loggingModules: List[ModuleID] = List(
      "tf.tofu" %% "tofu-logging" % tofuVersion,
      "tf.tofu" %% "tofu-logging-derivation" % tofuVersion,
      "tf.tofu" %% "tofu-logging-layout" % tofuVersion,
      "tf.tofu" %% "tofu-logging-logstash-logback" % tofuVersion,
      "tf.tofu" %% "tofu-logging-structured" % tofuVersion
    )
  }
}
