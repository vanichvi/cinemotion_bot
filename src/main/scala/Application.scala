import bot.CinemotionBot
import cats.effect.{ExitCode, IO, IOApp}
import config.BotConfig
import module.DbModule
import org.http4s.blaze.client.BlazeClientBuilder
import org.http4s.client.Client
import org.http4s.client.middleware.Logger
import parser.FilmParser
import pureconfig.ConfigSource
import storage.director.DirectorStorage
import storage.film.FilmStorage
import storage.user.UserStorage
import telegramium.bots.high.{Api, BotApi}

object Application extends IOApp {
  private type Init[A] = IO[A]
  private type App[A] = IO[A]

  override def run(args: List[String]): IO[ExitCode] =
    BlazeClientBuilder[IO].resource
      .use { httpClient =>
        val http = Logger(logBody = false, logHeaders = false)(httpClient)
        val conf = ConfigSource.default
        val token = conf.at("bot").loadOrThrow[BotConfig].token
        implicit val api: Api[IO] = createBotBackend(http, token)
        (for {
          dbModule <- DbModule.init[Init, App](conf)
          _ <- LiquibaseMigration.run(dbModule)
          userStorage = UserStorage[App](dbModule)
          filmStorage = FilmStorage[App](dbModule)
          directorStorage = DirectorStorage[App](dbModule)
          films = FilmParser(filmStorage).parse("films.json")
          _ <- directorStorage.addAllDirectors(films)
          _ <- filmStorage.addAllFilms(films)
          bot = new CinemotionBot(userStorage, filmStorage)

        } yield bot)
          .flatMap(_.start())
          .as(ExitCode.Success)
      }

  private def createBotBackend(http: Client[IO], token: String) =
    BotApi(http, baseUrl = s"https://api.telegram.org/bot$token")

}
