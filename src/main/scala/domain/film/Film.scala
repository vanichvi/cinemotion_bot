package domain.film

import domain.director.Director
import domain.genre.{Action, Genre}
import domain.{FilmDescription, FilmId, FilmTitle}
import doobie.Read
import io.circe.generic.extras.{Configuration, ConfiguredJsonCodec, JsonKey}

import java.time.Year

@ConfiguredJsonCodec case class Film(
  @JsonKey("imdbID") id: FilmId,
  @JsonKey("Title") title: FilmTitle,
  @JsonKey("Year") year: Year,
  @JsonKey("Genre") genre: Genre,
  @JsonKey("Director") director: Director,
  @JsonKey("Poster") posterLink: String,
  @JsonKey("Plot") description: FilmDescription = FilmDescription("")
)
object Film {
  implicit val config: Configuration = Configuration.default.withSnakeCaseMemberNames

  import domain.year.yearRead
  implicit val filmRead: Read[Film] =
    Read[(FilmId, FilmTitle, Year, Genre, Director, String, FilmDescription)].map {
      case (id, title, year, genre, director, posterLink, description) =>
        Film(
          id,
          title,
          year,
          genre,
          director,
          posterLink,
          description
        )
    }

  val defaultFilm: Film =
    Film(
      FilmId("Default"),
      FilmTitle("Film"),
      Year.now(),
      Action,
      Director("Def"),
      "",
      FilmDescription("")
    )
}
