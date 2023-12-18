package bot

import cats.Parallel
import cats.effect.unsafe.implicits.global
import cats.effect.{Async, IO}
import cats.implicits.catsSyntaxFlatMapOps
import domain.film.Film
import domain.genre._
import domain.genre.implicits.GenreFromString
import storage.film.FilmStorage
import storage.user.UserStorage
import telegramium.bots.high._
import telegramium.bots.high.implicits._

class CinemotionBot[F[_]](userStorage: UserStorage[F], filmsStorage: FilmStorage[IO])(implicit
  bot: Api[F],
  asyncF: Async[F],
  parallel: Parallel[F]
) extends LongPollBot[F](bot) {

  import cats.syntax.functor._
  import telegramium.bots._

  override def onMessage(msg: Message): F[Unit] = {
    msg.text.getOrElse("NO_TEXT") match {
      case "/start" => onStart(ChatIntId(msg.chat.id))
      case "/film"  => sendFilm(ChatIntId(msg.chat.id))
      case "/reset" => resetGenres(ChatIntId(msg.chat.id))
      case _        => commandNotFound(msg)
    }
  }

  private def filmNotFound(chatId: ChatIntId): F[Unit] = {
    sendPhoto(
      chatId = chatId,
      photo = InputLinkFile("https://cs5.pikabu.ru/post_img/big/2015/06/25/7/1435228793_1097179331.png"),
      caption = Some(s"""
           |Seems that we couldn't find any film for you
           |
           |This may be due to the fact that you have not chose any genre
           |
           |Try to choose any genre and send command again
           |""".stripMargin)
    ).exec.void
  }

  private def sendFilm(chatId: ChatIntId): F[Unit] = {
    val film = filmsStorage
      .selectFilmByUserFavGenres(chatId)
      .map(_.getOrElse(Film.defaultFilm))
      .unsafeRunSync()
    film match {
      case Film.defaultFilm =>
        filmNotFound(chatId)
      case _ =>
        userStorage.addUserWatchedFilm(chatId, film) >>
          sendPhoto(
            chatId = chatId,
            photo = InputLinkFile(film.posterLink),
            caption = Some(s"""${film.title.value}
                 |
                 |${film.year}
                 |
                 |Director:   ${film.director.name}
                 |
                 |Genre:      ${film.genre}
                 |
                 |${film.description.value}
                 |""".stripMargin)
          ).exec.void
    }
  }

  private def resetGenres(chatId: ChatIntId): F[Unit] =
    userStorage.resetFavGenres(chatId) >> sendMessage(
      chatId = chatId,
      text = "Your Favorite genres successfully deleted",
      replyMarkup = Some(
        InlineKeyboardMarkup(
          List(
            List(
              InlineKeyboardButton(
                text = "Choose new genres",
                callbackData = Some("chooseGenres")
              )
            )
          )
        )
      )
    ).exec.void

  override def onCallbackQuery(query: CallbackQuery): F[Unit] = {
    def addUserFavGenreToDB(chatId: ChatIntId, genre: Genre): F[Unit] = {
      userStorage.addUserFavGenre(chatId, genre).map(_.getOrElse(0)) >>
        answerCallbackQuery(
          callbackQueryId = query.id,
          text = Some(s"You added $genre as your favorite genre")
        ).exec.void
    }

    def addGenreToDBButton(genre: Genre) =
      InlineKeyboardButton(text = genre.toString, callbackData = Some(s"add${genre.toString}ToDB"))

    def onChosenGenres(chatIntId: ChatIntId, messageId: Int): F[Unit] = {
      deleteMessage(chatIntId, messageId).exec.void >>
        sendMessage(
          chatId = chatIntId,
          text = "Now you can get films of your favorite genres by sending /film command"
        ).exec.void
    }
    def chooseGenres(chatId: ChatId): F[Unit] = {
      sendMessage(
        chatId = chatId,
        text = "Choose your favorite genres and press Continue",
        replyMarkup = Some(
          InlineKeyboardMarkup(
            List(
              List(
                addGenreToDBButton(Action),
                addGenreToDBButton(Adventure),
                addGenreToDBButton(Animations),
                addGenreToDBButton(Comedy)
              ),
              List(
                addGenreToDBButton(Crime),
                addGenreToDBButton(Documentary),
                addGenreToDBButton(Drama),
                addGenreToDBButton(Family)
              ),
              List(
                addGenreToDBButton(History),
                addGenreToDBButton(Horror),
                addGenreToDBButton(Music),
                addGenreToDBButton(Mystery)
              ),
              List(
                addGenreToDBButton(Romance),
                addGenreToDBButton(ScienceFiction),
                addGenreToDBButton(Thriller),
                addGenreToDBButton(War)
              ),
              List(
                InlineKeyboardButton(
                  text = "Continue..",
                  callbackData = Some("onChosenGenres")
                )
              )
            )
          )
        )
      ).exec >> answerCallbackQuery(query.id).exec.void
    }
    query.data
      .map {
        case "chooseGenres" =>
          query.message.fold(asyncF.unit)(m => chooseGenres(ChatIntId(m.chat.id)))
        case s"add${genre}ToDB" =>
          query.message.fold(asyncF.unit)(m => addUserFavGenreToDB(ChatIntId(m.chat.id), genre.fromString))
        case "onChosenGenres" =>
          query.message.fold(asyncF.unit)(m => onChosenGenres(ChatIntId(m.chat.id), m.messageId))

        case x =>
          answerCallbackQuery(
            callbackQueryId = query.id,
            text = Some(s"Your choice is $x")
          ).exec.void
      }
      .getOrElse(asyncF.unit)
  }

  private def onStart(chatId: ChatIntId): F[Unit] = {
    userStorage.addUser(chatId) >>
      sendMessage(
        chatId = chatId,
        text = "Choose your favorite genres",
        replyMarkup = Some(
          InlineKeyboardMarkup(
            List(
              List(InlineKeyboardButton(text = "Choose genres", callbackData = Some("chooseGenres")))
            )
          )
        )
      ).exec.void
  }

  private def commandNotFound(msg: Message): F[Unit] = {
    sendMessage(
      chatId = ChatIntId(msg.chat.id),
      text = "Command Not Found"
    ).exec.void
  }
}
