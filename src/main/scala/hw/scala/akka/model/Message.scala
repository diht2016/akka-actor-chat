package hw.scala.akka.model

import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.{Format, Json}

case class Message(username: String, text: String)

object Message extends PlayJsonSupport {
  implicit val jsonFormat: Format[Message] = Json.format[Message]
}
