package hw.scala.akka

import akka.http.scaladsl.server.Route
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
    } ~ (pathPrefixId[Channel] & pathPrefix("messages") & pathEnd) { channelId =>
      get {
        repo.listMessages(channelId)
      } ~ (put & entity(as[Message])) { entity =>
        repo.createMessage(channelId, entity.username, entity.text)
      }
    }
  }
}
