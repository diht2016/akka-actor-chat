package hw.scala.akka.actors

import akka.actor.Actor
import hw.scala.akka.model.Message

class ChannelActor extends Actor {
  private val maxMessages = 100
  override def receive: Receive = withState(List.empty)

  def withState(state: List[Message]): Receive = {
    case message: Message =>
      val newState = state.appended(message).takeRight(maxMessages)
      context.become(withState(newState))
      sender ! ()
    case () =>
      sender ! state
  }
}
