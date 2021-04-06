package hw.scala.akka.actors

import akka.actor.{Actor, ActorRef, Props}
import hw.scala.akka.model.{Channel, ChannelId}

class RegistryActor extends Actor {
  import RegistryActor._

  override def receive: Receive = withState(List.empty)

  def withState(state: List[ChannelRecord]): Receive = {
    case name: String =>
      val newId = new ChannelId(state.length)
      val newChannel = Channel(newId, name)
      val newActor = createChannelActor
      val newState = state.appended(ChannelRecord(newChannel, newActor))
      context.become(withState(newState))
      sender ! newId
    case () =>
      sender ! state.map(_.channel)
    case id: ChannelId =>
      sender ! state.lift(id.value).map(_.actor)
  }

  def createChannelActor: ActorRef = context.actorOf(Props[ChannelActor]())
}

object RegistryActor {
  case class ChannelRecord(channel: Channel, actor: ActorRef)
}
