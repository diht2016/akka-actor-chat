package hw.scala.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import hw.scala.akka.model.{Channel, ChannelId}
import org.scalamock.handlers.CallHandler1
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class RegistryActorSpec extends TestKit(ActorSystem("ChannelActorSpec"))
  with ImplicitSender
  with AnyFlatSpecLike
  with Matchers
  with MockFactory
  with BeforeAndAfterAll {

  "RegistryActor" should "send empty list of channels on start" in new FreshActor {
    expectActorCreation.never()
    actor ! ()
    expectMsg(List.empty)
  }

  it should "create ChannelActor and send id when creating a channel" in new FreshActor {
    expectActorCreation.once()
    actor ! "channelName"
    expectMsg(ChannelId(0))
  }

  it should "retrieve created channels in correct order" in new FreshActor {
    expectActorCreation.twice()
    actor ! "channelName"
    expectMsg(ChannelId(0))
    actor ! "one more channel"
    expectMsg(ChannelId(1))
    actor ! ()
    expectMsg(List(
      Channel(ChannelId(0), "channelName"),
      Channel(ChannelId(1), "one more channel")
    ))
  }

  it should "retrieve actor by its id" in new FreshActor {
    expectActorCreation.once()
    actor ! "channelName"
    expectMsg(ChannelId(0))
    actor ! ChannelId(0)
    expectMsg(Some(testActor))
  }

  it should "respond with None if no actor found" in new FreshActor {
    expectActorCreation.once()
    actor ! "channelName"
    expectMsg(ChannelId(0))
    actor ! ChannelId(-1)
    expectMsg(None)
    actor ! ChannelId(1)
    expectMsg(None)
    actor ! ChannelId(100500)
    expectMsg(None)
  }

  trait FreshActor {
    protected val systemMock: ActorSystem = mock[ActorSystem]
    protected val testActor: ActorRef = TestProbe().ref
    protected val actor: ActorRef = system.actorOf(Props(classOf[RegistryActor], systemMock))

    protected def expectActorCreation: CallHandler1[Props, ActorRef] =
      (systemMock.actorOf: Props => ActorRef) expects * returns testActor
  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}
