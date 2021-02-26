package hw.scala.akka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Application extends App {
  implicit val system: ActorSystem = ActorSystem("app-system")
  implicit val ec: ExecutionContext = system.dispatcher

  val route = new ChatRoute(???).route

  val binding = Http()
    .newServerAt(ApplicationConfig.interface, ApplicationConfig.port)
    .bind(route)

  println(s"Server running at http://localhost:${ApplicationConfig.port}/")
  println("Press RETURN to stop")
  StdIn.readLine()
  binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
}
