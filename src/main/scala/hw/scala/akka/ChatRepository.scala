package hw.scala.akka

import hw.scala.akka.model.{Channel, Id, Message}

import scala.concurrent.Future

trait ChatRepository {
  def listChannels(): Future[Seq[Channel]]
  def createChannel(channelName: String): Future[Id[Channel]]
  def listMessages(channelId: Id[Channel]): Future[Seq[Message]]
  def createMessage(channelId: Id[Channel], username: String, text: String): Future[Unit]
}
