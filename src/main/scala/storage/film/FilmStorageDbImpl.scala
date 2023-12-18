package storage.film

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

import scala.util.Random

final private class FilmStorageDbImpl[F[_]: MonadCancelThrow](
  transactor: Transactor[F]
) extends FilmStorage[F] {
  override def addFilm(film: Film): F[Either[DBError, FilmId]] =
    FilmStorageSql
      .addFilm(film)
      .run
      .transact(transactor)
      .map(id => FilmId(id.toString))
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  override def addAllFilms(films: List[Film]): F[List[Either[DBError, FilmId]]] = films.traverse(film => addFilm(film))

  override def selectFilmByUserFavGenres(chatId: ChatIntId): F[Either[DBError, Film]] =
    FilmStorageSql
      .selectFilmByUserFavGenres(chatId)
      .to[List]
      .map(list => Random.shuffle(list).headOption)
      .transact(transactor)
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))
      .flatMapIn(_.toRight(NotFoundDbError))

}
