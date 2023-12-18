package module

import cats.effect.IO
import cats.effect.kernel.{Async, Sync}
import cats.syntax.functor._
import config.DbConfig
import doobie.Transactor
import pureconfig.ConfigSource

final case class DbModule[F[_]](
  transactor: Transactor[F]
)

object DbModule {
  def init[I[_]: Sync, F[_]: Async](conf: ConfigSource): I[DbModule[F]] =
    Sync[I]
      .delay(conf.at("db").loadOrThrow[DbConfig])
      .map(conf =>
        Transactor.fromDriverManager[F](
          driver = conf.driver,
          url = conf.url,
          user = conf.user,
          pass = conf.password
        )
      )
      .map(DbModule[F](_))
}
