package domain.genre

import doobie.Read

object implicits {
  implicit class GenreFromString(string: String) {
    def fromString: Genre = {
      string match {
        case "Action"         => Action
        case "Adventure"      => Adventure
        case "Animations"     => Animations
        case "Crime"          => Crime
        case "Comedy"         => Comedy
        case "Documentary"    => Documentary
        case "Drama"          => Drama
        case "Family"         => Family
        case "Fantasy"        => Fantasy
        case "History"        => History
        case "Horror"         => Horror
        case "Music"          => Music
        case "Mystery"        => Mystery
        case "Romance"        => Romance
        case "ScienceFiction" => ScienceFiction
        case "Thriller"       => Thriller
        case "TVMovie"        => TVMovie
        case "War"            => War
        case "Western"        => Western
      }
    }
  }
}
