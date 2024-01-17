package domain.genre

import domain.genre.implicits.GenreFromString
import doobie.Read
import io.circe._
import org.latestbit.circe.adt.codec._

sealed trait Genre
object Genre {
  implicit val genreRead: Read[Genre] = Read[String].map(genre => genre.fromString)
  implicit val genreEncoder: Encoder[Genre] = JsonTaggedAdtCodec.createPureEnumEncoder[Genre]()
  implicit val genreDecoder: Decoder[Genre] = JsonTaggedAdtCodec.createPureEnumDecoder[Genre]()

  /** OMDB returns json { ... "Genre" : "Genre1, Genre2, ..., ... } for now my in my database designed the way when
    * Films can have only one Genre
    */
  //  JsonTaggedAdtCodec.createDecoderDefinition[Genre] {
  //    case (converter, cursor) =>
  //      cursor.get[Option[String]]("Genre").flatMap {
  //        case Some(value) =>
  //          converter.fromJsonObject(
  //            value.split(",") match {
  //              case Array(genre) => genre
  //              case array        => array(1) // array len >= 2
  //            },
  //            cursor.downField("Director")
  //          )
  //        case _ => Decoder.failedWithMessage(s"Genre field isn't specified")(cursor)
  //      }
  //  }
}

case object Action extends Genre

case object Adventure extends Genre

case object Animations extends Genre

case object Comedy extends Genre

case object Crime extends Genre

case object Documentary extends Genre

case object Drama extends Genre

case object Family extends Genre

case object Fantasy extends Genre

case object History extends Genre

case object Horror extends Genre

case object Music extends Genre

case object Mystery extends Genre

case object Romance extends Genre

case object ScienceFiction extends Genre

case object Thriller extends Genre

case object TVMovie extends Genre

case object War extends Genre

case object Western extends Genre
