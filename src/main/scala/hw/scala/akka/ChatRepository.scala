package hw.scala.akka

import hw.scala.akka.model.{Channel, ChannelId, Message}

import scala.concurrent.Future

trait ChatRepository {
  def listChannels(): Future[Seq[Channel]]
  def createChannel(channelName: String): Future[ChannelId]
  def listMessages(channelId: ChannelId): Future[Seq[Message]]
  def createMessage(channelId: ChannelId, message: Message): Future[Unit]
}
