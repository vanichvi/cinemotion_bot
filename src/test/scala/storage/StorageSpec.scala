package storage

import cats.effect.IO
import config.DbConfig
import doobie.util.transactor.Transactor
import org.specs2.mutable.Specification
import pureconfig.ConfigSource

trait StorageSpec extends Specification with doobie.specs2.IOChecker {
  val conf: DbConfig = ConfigSource.default.at("db").loadOrThrow[DbConfig]

  override def transactor: doobie.Transactor[IO] = Transactor.fromDriverManager(
    driver = conf.driver,
    url = conf.url,
    user = conf.user,
    pass = conf.password
  )
}
