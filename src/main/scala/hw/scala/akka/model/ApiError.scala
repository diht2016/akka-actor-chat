package hw.scala.akka.model

import scala.concurrent.Future

case class ApiError(code: Int, message: String) extends Throwable

object ApiError {
  def apply(code: Int, message: String): Future[Nothing] =
    Future.failed(new ApiError(code, message))
}
