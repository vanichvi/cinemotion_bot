package domain.errors

sealed trait AppError extends Error {
  def message: String = "Application error occurred"
}
final case class FilmError() extends AppError

final case class DirectorError() extends AppError
