package service

import spray.routing._
import akka.actor._
import akka.pattern.ask
import akka.util._
import scala.concurrent.duration._

import ssy066._
import spray.json._
import spray.httpx.unmarshalling._
import spray.httpx.marshalling._


/**
  * Created by Kristofer on 2014-05-16.
 */
class GUIRouter(sm: ActorRef, sm2: ActorRef) extends HttpServiceActor {
  override def receive: Receive =
  runRoute(myRoute ~ staticRoute)

  object MyJsonProtocol extends DefaultJsonProtocol {
    implicit val evFM = jsonFormat1(Event)
    implicit val stateFM = jsonFormat3(State)
  }


  import context.dispatcher
  import MyJsonProtocol._
  import spray.httpx.SprayJsonSupport._


  var part = "sm1"

  implicit val timeout = Timeout(3 seconds)
  def myRoute: Route = {
    pathPrefix("statemachine" / "sm1") {
      stateMRoute(sm)
    } ~
    pathPrefix("statemachine" / "sm2") {
      stateMRoute(sm2)
    } ~
    path("init"){
      dynamic{
        complete(part)
      }
    } ~
    path("sm2"){
      dynamic{
        part = "sm2"
        complete(part)
      }
    } ~
    path("sm1"){
      dynamic{
        part = "sm1"
        complete(part)
      }
    }
  }

  def stateMRoute(sm: ActorRef) = {
    path("init"){
      val f = sm ? GetInit
      compl(f)
    } ~
      path(Segment / Segment) { (state, event) =>
        compl(sm ? NextState(state, event))
      }
  }


  import scala.concurrent.Future
  def compl(f: Future[Any]) = {
    onSuccess(f) {
      case e: String => complete(e)
      case s: State => complete(s)
    }
  }


  def staticRoute: Route = {
    //path("")(getFromResource("webapp/index.html")) ~ getFromResourceDirectory("webapp")
    path("")(getFromFile("./webapp/index.html")) ~ getFromDirectory("./webapp")
  }
}

object GUIRouter {
  def props(routeToMe: ActorRef, routeToMe2: ActorRef) = Props(classOf[GUIRouter],routeToMe, routeToMe2)

}


