package parser

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpMethods, HttpRequest}
import domain.film.Film
import io.circe.parser.decode
import storage.film.FilmStorage

import scala.concurrent.duration.DurationDouble
import scala.concurrent.{Await, Future}

final case class FilmParser[F[_]](filmsStorage: FilmStorage[F]) {

  implicit val system: ActorSystem = ActorSystem()

  import system.dispatcher

  private def filmReqOmdb(list: List[String]): List[HttpRequest] = {
    val listMap = list.map(title => s"https://www.omdbapi.com/?apikey=41bc9db8&t=$title&type=movie")
    listMap.map(uri => HttpRequest(HttpMethods.GET, uri))
  }

  private def sendReqOmbd(request: HttpRequest): Future[String] = {
    val responseFuture = Http().singleRequest(request)
    responseFuture.flatMap(_.entity.toStrict(0.001.seconds)).map(_.data.utf8String)
  }

  def writeFilmsJson(source: String): Unit = {
    val src = scala.io.Source.fromFile(source)

    val lines = {
      try src.getLines().toList
      finally src.close()
    }
    val reqs = filmReqOmdb(lines)
    os.write.over(
      os.Path("\\films.json"),
      reqs
        .map(req =>
          Await.result(
            sendReqOmbd(req),
            1.seconds
          )
        )
        .mkString("[", ",", "]")
    )
  }

  def parse(source: String): List[Film] = {
    val src = scala.io.Source.fromFile(source)
    val lines = {
      try src.getLines().mkString("\n")
      finally src.close()
    }
    decode[List[Film]](lines).getOrElse(List(Film.defaultFilm))
  }
}
