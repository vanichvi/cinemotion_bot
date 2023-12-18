package storage.director

import cats.effect.kernel.MonadCancelThrow
import domain.director.Director
import domain.errors.{DBError, UnexpectedDbError}
import domain.film.Film
import doobie.implicits.toSqlInterpolator
import doobie.util.transactor.Transactor
import cats.effect.kernel.MonadCancelThrow
import cats.implicits.{catsSyntaxApplicativeError, toTraverseOps}
import cats.syntax.functor._
import domain.FilmId
import domain.errors.DBError.NotFoundDbError
import domain.errors.{DBError, UnexpectedDbError}
import domain.film.Film
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.util.transactor.Transactor
import telegramium.bots.ChatIntId
import tofu.syntax.feither.EitherFOps

final private class DirectorStorageDbImpl[F[_]: MonadCancelThrow](
  transactor: Transactor[F]
) extends DirectorStorage[F] {
  override def addDirectorByFilm(film: Film): F[Either[DBError, Director]] =
    DirectorStorageSql
      .addDirectorByFilm(film)
      .run
      .transact(transactor)
      .map(f => Director(f.toString))
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  override def addAllDirectors(films: List[Film]): F[List[Either[DBError, Director]]] =
    films.traverse(film => addDirectorByFilm(film))
}
