package storage.director

import domain.film.Film
import doobie.implicits.toSqlInterpolator

case object DirectorStorageSql {
  def addDirectorByFilm(film: Film): doobie.Update0 =
    sql"""
        insert into directors values (${film.director.name})
      """.update

}
