package hw.scala.akka.model

import play.api.libs.json.{Format, Json}

case class Channel(id: ChannelId, name: String)

object Channel {
  implicit val jsonFormat: Format[Channel] = Json.format[Channel]
}
