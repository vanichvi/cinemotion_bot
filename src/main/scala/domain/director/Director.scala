package domain.director

import io.circe.{Decoder, Encoder}

case class Director(name: String)
object Director {
  implicit val directorKeyEncoder: Encoder[Director] = Encoder[String].contramap(_.name)
  implicit val directorKeyDecoder: Decoder[Director] = Decoder[String].map(Director(_))

  val notFound: Director = Director("Director not found")
}
