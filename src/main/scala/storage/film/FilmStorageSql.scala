package storage.film

import domain.film.Film
import doobie.implicits.toSqlInterpolator
import telegramium.bots.ChatIntId

case object FilmStorageSql {
  def addFilm(film: Film): doobie.Update0 =
    sql"""
    insert into films (id, title, year, genre, director, poster_link, description)
    values (${film.id.value},
            ${film.title.value},
            ${film.year.getValue},
            ${film.genre.toString},
            ${film.director.name},
            ${film.posterLink},
            ${film.description.value}
           )
       """.update

  def selectFilmByUserFavGenres(chatId: ChatIntId): doobie.Query0[Film] =
    sql"""
      select films.id,
           title,
           year,
           films.genre,
           director,
           poster_link,
           description
    from films,
         user_favorite_genres as ufg
--     left outer join user_watched_films uwf on uwf.user_id = ufg.user_id
    where ufg.user_id = ${chatId.id}
      and ufg.genre = films.genre
       """
      .query[Film]
}
