package hw.scala.akka

import akka.http.scaladsl.server._
import hw.scala.akka.model.Id
import play.api.libs.json.{Json, Writes}

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Success}

trait Controller extends Directives {
  /**
   * Directive which extracts [[Id]] of given type `A` from request path.
   *
   * Example:
   * {{{
   *   pathPrefixId[User] { userId =>
   *     // do something with userId here
   *   }
   * }}}
   */
  def pathPrefixId[A]: Directive1[Id[A]] = pathPrefix(LongNumber).map(new Id[A](_))

  /**
   * Provides a HTTP REST API error with code and error message.
   *
   * Example:
   * {{{
   *   userIdOptFuture.flatMap {
   *     case Some(userId) => ??? // do some work and return some successful future here
   *     case None => ApiError(404, "user not found")
   *   }
   * }}}
   */
  case class ApiError(code: Int, message: String) extends Throwable
  object ApiError {
    def apply(code: Int, message: String): Future[Nothing] =
      Future.failed(new ApiError(code, message))
  }

  val successResponse: Route = complete("""{"success":true}""")
  def errorResponse(error: ApiError): Route =
    complete(error.code, s"""{"error":"${error.message}"}""")

  def futureToResponse[T](result: Future[T], completer: T => Route): Route =
    onComplete(result) {
      case Success(writable) => completer(writable)
      case Failure(error: ApiError) => errorResponse(error)
      case Failure(error) =>
        error.printStackTrace()
        errorResponse(new ApiError(500, "internal server error"))
    }

  implicit def futureToResponse(result: Future[Unit]): Route =
    futureToResponse[Unit](result, (_: Unit) => successResponse)

  implicit def futureToResponse[T : Writes](result: Future[T]): Route =
    futureToResponse(result, (writable: T) => complete(Json.toJson(writable).toString()))
}
