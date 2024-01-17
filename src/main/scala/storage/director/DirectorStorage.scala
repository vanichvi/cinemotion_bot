package storage.director

import cats.effect.kernel.MonadCancelThrow
import domain.director.Director
import domain.errors.DBError
import domain.film.Film
import module.DbModule

trait DirectorStorage[F[_]] {
  def addDirectorByFilm(film: Film): F[Either[DBError, Director]]

  def addAllDirectors(films: List[Film]): F[List[Either[DBError, Director]]]
}

object DirectorStorage {
  def apply[F[_]: MonadCancelThrow](
    dbModule: DbModule[F]
  ): DirectorStorage[F] =
    new DirectorStorageDbImpl[F](dbModule.transactor)

}
