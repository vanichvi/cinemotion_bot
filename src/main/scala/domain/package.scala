import io.circe.{Decoder, Encoder}

package object domain {
  case class FilmId(value: String)

  object FilmId {
    implicit val filmIdKeyDecoder: Decoder[FilmId] = Decoder[String].map(FilmId(_))
    implicit val filmIdKeyEncoder: Encoder[FilmId] = Encoder[String].contramap(_.value)

  }

  case class FilmTitle(value: String)

  object FilmTitle {
    implicit val filmTitleKeyDecoder: Decoder[FilmTitle] = Decoder[String].map(FilmTitle(_))
    implicit val filmTitleKeyEncoder: Encoder[FilmTitle] = Encoder[String].contramap(_.value)

  }

  case class FilmDescription(value: String)

  object FilmDescription {
    implicit val filmDescriptionKeyDecoder: Decoder[FilmDescription] = Decoder[String].map(FilmDescription(_))
    implicit val filmDescriptionKeyEncoder: Encoder[FilmDescription] = Encoder[String].contramap(_.value)

  }
}
