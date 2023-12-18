package storage.user

import domain.film.Film
import domain.genre.Genre
import doobie.implicits.toSqlInterpolator
import telegramium.bots.ChatIntId

case object UserStorageSql {
  def addUser(chatId: ChatIntId): doobie.Update0 =
    sql"""
      insert into users (id) values ($chatId)
       """.update

  def addUserFavGenre(chatId: ChatIntId, genre: Genre): doobie.Update0 =
    sql"""
      insert into user_favorite_genres (user_id, genre) values ($chatId, ${genre.toString})
       """.update

  def resetFavGenres(chatIntId: ChatIntId): doobie.Update0 =
    sql"""
         delete from user_favorite_genres where user_id = ${chatIntId.id}
       """.update

  def addUserWatchedFilm(chatIntId: ChatIntId, film: Film): doobie.Update0 =
    sql"""
         insert into user_watched_films (user_id, film_id) values (${chatIntId.id}, ${film.id})
       """.update

}
