package domain

import doobie.Read
import io.circe.{Decoder, Encoder}

import java.time.Year

object year {
  implicit val yearRead: Read[Year] = Read[Int].map(year => Year.of(year))
  implicit val yearEncoder: Encoder[Year] = Encoder[Int].contramap(_.getValue)
  implicit val yearDecoder: Decoder[Year] = Decoder[Int].map(Year.of)
}
