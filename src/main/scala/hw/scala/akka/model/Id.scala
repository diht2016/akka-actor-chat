package hw.scala.akka.model

import play.api.libs.json.{Format, Json}

class Id[+A](val value: Long) extends AnyVal {
  override def toString: String = value.toString
}

object Id {
  def auto = new Id[Nothing](-1)
  implicit def jsonFormat[A]: Format[Id[A]] = Json.valueFormat[Id[A]]
}
