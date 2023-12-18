package storage.film

import cats.effect.kernel.MonadCancelThrow
import domain.FilmId
import domain.errors.DBError
import domain.film.Film
import module.DbModule
import telegramium.bots.ChatIntId

trait FilmStorage[F[_]] {
  def addFilm(film: Film): F[Either[DBError, FilmId]]
  def selectFilmByUserFavGenres(chatId: ChatIntId): F[Either[DBError, Film]]

  def addAllFilms(films: List[Film]): F[List[Either[DBError, FilmId]]]
}

object FilmStorage {
  def apply[F[_]: MonadCancelThrow](
    dbModule: DbModule[F]
  ): FilmStorage[F] =
    new FilmStorageDbImpl[F](dbModule.transactor)

}
