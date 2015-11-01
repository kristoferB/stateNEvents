package service

import akka.actor.{Props, ActorSystem}
import akka.io.IO
import spray.can.Http


/**
 * Main class for the service actor and can be stopped by hitting the `"e"` key.
 */
object Boot extends App {

  private def waitForExit() = {
    def waitEOF(): Unit = Console.readLine() match {
      case "" => system.shutdown()
      case _ => waitEOF()
    }
    waitEOF()
  }

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("actor-system")



  val stateMachine = system.actorOf(ssy066.StateMachine.props())
  val stateMachine2 = system.actorOf(ssy066.StateMachine2.props())

  // create and start our service actor
  val service = system.actorOf(GUIRouter.props(stateMachine, stateMachine2), "service-actor")

  // start a new HTTP server on port 8080 with our service actor as the handler
  val interface = ServiceSettings(system).interface
  val port = ServiceSettings(system).port
  IO(Http) ! Http.Bind(service, interface, port)

  Console.println(s"Server started ${system.name}, $interface:$port")
  Console.println("Type `exit` to exit....")

  //test

  waitForExit()
  system.shutdown()


}

