package config

import cats.effect.IO
import pureconfig.{ConfigReader, ConfigSource}
import pureconfig.generic.semiauto._

final case class AppConfig(db: DbConfig, server: ServerConfig, bot: BotConfig)
object AppConfig {
  implicit val reader: ConfigReader[AppConfig] = deriveReader

  def load: IO[AppConfig] =
    IO.delay(ConfigSource.default.loadOrThrow[AppConfig])
}

final case class BotConfig(token: String)

object BotConfig {
  implicit val reader: ConfigReader[BotConfig] = deriveReader

}
final case class DbConfig(
  url: String,
  driver: String,
  user: String,
  password: String
)
object DbConfig {
  implicit val reader: ConfigReader[DbConfig] = deriveReader
}

final case class ServerConfig(host: String, port: Int)
object ServerConfig {
  implicit val reader: ConfigReader[ServerConfig] = deriveReader
}
