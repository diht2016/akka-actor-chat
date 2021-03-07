package hw.scala.akka.http

import akka.http.scaladsl.server._
import hw.scala.akka.model.{ApiError, ChannelId}
import play.api.libs.json.{Json, Writes}

import scala.concurrent.Future
import scala.language.implicitConversions
import scala.util.{Failure, Success}

trait Controller extends Directives {
  def pathPrefixId: Directive1[ChannelId] = pathPrefix(IntNumber).map(new ChannelId(_))

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
