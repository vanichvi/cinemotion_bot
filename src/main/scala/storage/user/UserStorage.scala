package storage.user

import cats.effect.kernel.MonadCancelThrow
import domain.FilmId
import domain.errors.DBError
import domain.film.Film
import domain.genre.Genre
import module.DbModule
import telegramium.bots.{ChatId, ChatIntId, User}

trait UserStorage[F[_]] {
  def addUser(chatId: ChatIntId): F[Either[DBError, Long]]
  def addUserFavGenre(chatId: ChatIntId, genre: Genre): F[Either[DBError, Long]]
  def resetFavGenres(chatIntId: ChatIntId): F[Either[DBError, Unit]]

  def addUserWatchedFilm(chatIntId: ChatIntId, film: Film): F[Either[DBError, FilmId]]

  // def listUserFavoriteGenres(chatIntId: ChatIntId): F[Either[DBError, List[Genre]]]
}
object UserStorage {
  def apply[F[_]: MonadCancelThrow](
    dbModule: DbModule[F]
  ): UserStorage[F] = {
    new UserStorageDbImpl[F](dbModule.transactor)
  }
}
