package hw.scala.akka.model

import play.api.libs.json.{Format, Json}

class ChannelId(val value: Int) extends AnyVal {
  override def toString: String = value.toString
}

object ChannelId {
  implicit def jsonFormat[A]: Format[ChannelId] = Json.valueFormat[ChannelId]
}
