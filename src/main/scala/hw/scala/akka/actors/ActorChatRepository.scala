package hw.scala.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import hw.scala.akka.ChatRepository
import hw.scala.akka.model.{ApiError, Channel, ChannelId, Message}

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContext, Future}

class ActorChatRepository(system: ActorSystem) extends ChatRepository {
  import RegistryActor._
  import ChannelActor._
  private val registry: ActorRef = system.actorOf(Props[RegistryActor])

  private implicit val ec: ExecutionContext = system.getDispatcher
  private implicit val timeout: Timeout = 3.seconds

  override def listChannels(): Future[Seq[Channel]] =
    (registry ? ListChannels).mapTo[Seq[Channel]]

  override def createChannel(channelName: String): Future[ChannelId] =
    (registry ? CreateChannel(channelName)).mapTo[ChannelId]

  override def listMessages(channelId: ChannelId): Future[Seq[Message]] =
    getChannelActor(channelId)
      .flatMap(_ ? ListMessages).mapTo[Seq[Message]]

  override def createMessage(channelId: ChannelId, message: Message): Future[Unit] =
    getChannelActor(channelId)
      .map(_ ! SendMessage(message))

  private def getChannelActor(channelId: ChannelId): Future[ActorRef] =
    (registry ? GetChannelActor(channelId)).mapTo[Option[ActorRef]]
      .flatMap {
        case Some(actor: ActorRef) => Future.successful(actor)
        case None => ApiError(404, "channel not found")
      }
}
