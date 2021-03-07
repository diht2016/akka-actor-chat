package hw.scala.akka.http

import akka.http.scaladsl.server.Route
import hw.scala.akka.ChatRepository
import hw.scala.akka.model.{Channel, Message}

import scala.concurrent.ExecutionContext

class ChatRoute(repo: ChatRepository)(implicit ec: ExecutionContext) extends Controller {
  def route: Route = (pathPrefix("api") & pathPrefix("channels")) {
    pathEnd {
      get {
        repo.listChannels()
      } ~ (post & entity(as[String])) { entity =>
        repo.createChannel(entity)
      }
    } ~ (pathPrefixChannelId & pathPrefix("messages") & pathEnd) { channelId =>
      get {
        repo.listMessages(channelId)
      } ~ (put & entity(as[Message])) { entity =>
        repo.createMessage(channelId, entity)
      }
    }
  }
}
