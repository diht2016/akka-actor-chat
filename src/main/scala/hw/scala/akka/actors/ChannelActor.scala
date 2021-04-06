package hw.scala.akka.actors

import akka.actor.Actor
import hw.scala.akka.model.Message

class ChannelActor extends Actor {
  import ChannelActor._

  private val maxMessages = 100
  override def receive: Receive = withState(List.empty)

  def withState(state: List[Message]): Receive = {
    case SendMessage(message) =>
      val newState = state.appended(message).takeRight(maxMessages)
      context.become(withState(newState))
    case ListMessages =>
      sender ! state
  }
}

object ChannelActor {
  case class SendMessage(message: Message)
  case object ListMessages
}
