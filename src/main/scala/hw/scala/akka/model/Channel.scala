package hw.scala.akka.model

import play.api.libs.json.{Format, Json}

case class Channel(id: Id[Channel], name: String)

object Channel {
  implicit val jsonFormat: Format[Channel] = Json.format[Channel]
}
