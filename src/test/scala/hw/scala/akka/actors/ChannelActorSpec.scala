package hw.scala.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import hw.scala.akka.model.Message
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class ChannelActorSpec extends TestKit(ActorSystem("ChannelActorSpec"))
  with ImplicitSender
  with AnyFlatSpecLike
  with Matchers
  with BeforeAndAfterAll {

  "ChannelActor" should "send empty list of messages on start" in new FreshActor {
    actor ! ()
    expectMsg(List.empty)
  }

  it should "send unit acknowledgement on message" in new FreshActor {
    private val message = Message("user", "some text")
    actor ! message
    expectMsg(())
  }

  it should "retrieve message in list" in new FreshActor {
    private val message = Message("user", "some text")
    actor ! message
    expectMsg(())
    actor ! ()
    expectMsg(List(message))
  }

  it should "retrieve messages in correct order" in new FreshActor {
    private val message1 = Message("user", "some text")
    private val message2 = Message("user", "more text")
    actor ! message1
    expectMsg(())
    actor ! message2
    expectMsg(())
    actor ! ()
    expectMsg(List(message1, message2))
  }

  it should "cut chat on 100 last messages" in new FreshActor {
    private val messages = List.tabulate(110)(i => Message("user", i.toString))
    messages.foreach { message =>
      actor ! message
      expectMsg(())
    }
    actor ! ()
    expectMsg(messages.takeRight(100))
  }

  trait FreshActor {
    protected val actor: ActorRef = system.actorOf(Props[ChannelActor]())
  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}
