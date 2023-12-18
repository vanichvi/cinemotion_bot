package storage.user

import cats.effect.kernel.MonadCancelThrow
import cats.implicits.catsSyntaxApplicativeError
import cats.syntax.functor._
import domain.FilmId
import domain.errors.{DBError, UnexpectedDbError}
import domain.film.Film
import domain.genre.Genre
import doobie.implicits.toConnectionIOOps
import doobie.util.transactor.Transactor
import telegramium.bots.ChatIntId
import tofu.syntax.feither.EitherFOps

final private class UserStorageDbImpl[F[_]: MonadCancelThrow](
  transactor: Transactor[F]
) extends UserStorage[F] {
  override def addUser(chatId: ChatIntId): F[Either[DBError, Long]] =
    UserStorageSql
      .addUser(chatId)
      .run
      .transact(transactor)
      .map(_.longValue)
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  override def addUserFavGenre(chatId: ChatIntId, genre: Genre): F[Either[DBError, Long]] =
    UserStorageSql
      .addUserFavGenre(chatId, genre)
      .run
      .transact(transactor)
      .map(_.longValue)
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  override def resetFavGenres(chatIntId: ChatIntId): F[Either[DBError, Unit]] =
    UserStorageSql
      .resetFavGenres(chatIntId)
      .run
      .transact(transactor)
      .map(_ => ())
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  override def addUserWatchedFilm(chatIntId: ChatIntId, film: Film): F[Either[DBError, domain.FilmId]] =
    UserStorageSql
      .addUserWatchedFilm(chatIntId, film)
      .run
      .transact(transactor)
      .map(t => FilmId(t.toString))
      .attempt
      .leftMapIn(err => UnexpectedDbError(err.getMessage))

  //  override def listUserFavoriteGenres(chatIntId: ChatIntId): F[Either[DBError, List[Genre]]] =
  //    sql"""
  //         select genre from user_favorite_genres
  //         where user_id = ${chatIntId.id}
  //       """
  //      .query[Genre]
  //      .to[List]
  //      .transact(transactor)
  //      .attempt
  //      .leftMapIn(err => UnexpectedDbError(err.getMessage))
}
