import io.circe.Decoder.Result
import io.circe.{Decoder, Encoder, HCursor, KeyDecoder}
import io.circe.generic.JsonCodec
import tethys.derivation.semiauto._
import tethys.{JsonReader, JsonWriter}

package object domain {
  // @newtype
  case class FilmId(value: String)

  object FilmId {
    implicit val filmIdKeyDecoder: Decoder[FilmId] = Decoder[String].map(FilmId(_))
    implicit val filmIdKeyEncoder: Encoder[FilmId] = Encoder[String].contramap(_.value)

  }

  // @newtype
  case class FilmTitle(value: String)

  object FilmTitle {
    implicit val filmTitleKeyDecoder: Decoder[FilmTitle] = Decoder[String].map(FilmTitle(_))
    implicit val filmTitleKeyEncoder: Encoder[FilmTitle] = Encoder[String].contramap(_.value)

  }

  // @newtype
  case class FilmDescription(value: String)

  object FilmDescription {
    implicit val filmDescriptionKeyDecoder: Decoder[FilmDescription] = Decoder[String].map(FilmDescription(_))
    implicit val filmDescriptionKeyEncoder: Encoder[FilmDescription] = Encoder[String].contramap(_.value)

  }
}
