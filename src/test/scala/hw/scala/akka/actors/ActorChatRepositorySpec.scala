package hw.scala.akka.actors

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import hw.scala.akka.model.{ApiError, Channel, ChannelId, Message}
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext

class ActorChatRepositorySpec extends TestKit(ActorSystem("ChannelActorSpec"))
  with AnyFlatSpecLike
  with Matchers
  with MockFactory
  with BeforeAndAfterAll {
  import RegistryActor._
  import ChannelActor._
  import ActorChatRepositorySpec._

  "listChannels" should "return a list of channels" in new TestWiring {
    private val f = repo.listChannels()
    registryProbe.expectMsg(ListChannels)
    registryProbe.reply(sampleChannelList)
    f.map(_ shouldBe sampleChannelList)
  }

  "createChannel" should "return a channel id" in new TestWiring {
    private val f = repo.createChannel(sampleChannelName)
    registryProbe.expectMsg(CreateChannel(sampleChannelName))
    registryProbe.reply(sampleChannelId)
    f.map(_ shouldBe sampleChannelId)
  }

  "listMessages" should "return a list of message" in new TestWiring {
    private val f = repo.listMessages(sampleChannelId)
    registryProbe.expectMsg(GetChannelActor(sampleChannelId))
    registryProbe.reply(Some(channelActor))
    channelProbe.expectMsg(ListMessages)
    channelProbe.reply(sampleMessageList)
    f.map(_ shouldBe sampleMessageList)
  }

  "createMessage" should "return a message id" in new TestWiring {
    private val f = repo.createMessage(sampleChannelId, sampleMessage)
    registryProbe.expectMsg(GetChannelActor(sampleChannelId))
    registryProbe.reply(Some(channelActor))
    channelProbe.expectMsg(SendMessage(sampleMessage))
  }

  it should "return an error if channel not found" in new TestWiring {
    private val f = repo.listMessages(sampleChannelId)
    registryProbe.expectMsg(GetChannelActor(sampleChannelId))
    registryProbe.reply(None)
    f.failed.map(_ shouldBe sampleError)
  }

  trait TestWiring {
    implicit val ec: ExecutionContext = system.getDispatcher
    protected val systemMock: ActorSystem = mock[ActorSystem]
    protected val registryProbe: TestProbe = TestProbe()
    protected val registryActor: ActorRef = registryProbe.ref
    protected val channelProbe: TestProbe = TestProbe()
    protected val channelActor: ActorRef = channelProbe.ref

    (systemMock.actorOf: Props => ActorRef) expects * returns registryActor
    (systemMock.getDispatcher _).expects () returns system.getDispatcher
    protected val repo: ActorChatRepository = new ActorChatRepository(systemMock)
  }

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
}

object ActorChatRepositorySpec {
  private val sampleChannelId = ChannelId(1)
  private val sampleChannelName = "test channel"
  private val sampleChannelList = Seq(Channel(sampleChannelId, sampleChannelName))
  private val sampleMessage = Message("user", "text")
  private val sampleMessageList = Seq(sampleMessage)
  private val sampleError = new ApiError(404, "channel not found")
}
