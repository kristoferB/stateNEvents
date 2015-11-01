package ssy066

import akka.actor._
import akka.pattern._
import akka.util.Timeout



case class State(name: String, events: List[Event], display: String = "")
case class Event(name: String)
case object GetInit
case class NextState(state: String, event: String)

/**
 * Created by Kristofer on 2014-05-15.
 */
class StateMachine extends Actor {

import scala.concurrent.duration._

  val a = Event("a")
  val b = Event("b")
  val c = Event("c")
  val d = Event("d")
  val eventsPart1 = Map("a" -> a, "b" -> b, "c" -> c, "d" -> d)

  val initPart1 = State("init", List(a, b))
  val s1 = State("s1", List(c))
  val s2 = State("s2", List(a, d))
  val s3 = State("s3", List(b))
  val s4 = State("s4", List(c))
  val statesPart1 = Map("init" -> initPart1, s1.name -> s1, s2.name -> s2, s3.name -> s3, s4.name -> s4)

  val stateMachinePart1 = Map(
    (initPart1, a) -> s1,
    (initPart1, b) -> s4,
    (s1, c) -> s2,
    (s2, a) -> s3,
    (s2, d) -> s1,
    (s3, b) -> s4,
    (s4, c) -> initPart1
  )


  var init = initPart1
  var stateMachine = stateMachinePart1
  var states = statesPart1
  var events = eventsPart1


  def receive = {
    case GetInit => sender ! init
    case NextState(s, ev) => {
      if (states.contains(s) && events.contains(ev)) {
        val state = states(s)
        val event = events(ev)
        val tuple = (state, event)
        if (stateMachine.contains(tuple))
          sender ! stateMachine(tuple)
        else
          sender ! s"State $s, does not enable event $ev"
      } else sender ! s"your state: $s, event: $ev. States: $states, events: $events"
    }
  }
}

object StateMachine {
  def props() = Props(classOf[StateMachine])


}

object StateMachine2 {
  def props() = Props(classOf[StateMachine2])


}



class StateMachine2 extends Actor {

  import scala.concurrent.duration._

  val o1 = Event("o1")
  val o2 = Event("o2")
  val o3 = Event("o3")
  val o4 = Event("o4")
  val o5 = Event("o5")
  val eventsPart2 = Map("o1" -> o1, "o2" -> o2, "o3" -> o3, "o4" -> o4, "o5" -> o5)

  val initPart2 = State("init", List(o1, o2), "v1=0, v2=0")
  val s1p2 = State("s1p2", List(o2, o3, o5), "v1=1, v2=0")
  val s2p2 = State("s2p2", List(o4), "v1=0, v2=1")
  val s3p2 = State("s3p2", List(o4,o5), "v1=1, v2=1")
  val statesPart2 = Map("init" -> initPart2, s1p2.name -> s1p2, s2p2.name -> s2p2, s3p2.name -> s3p2)

  val stateMachinePart2 = Map(
    (initPart2, o1) -> s1p2,
    (initPart2, o2) -> s2p2,
    (s1p2, o2) -> s3p2,
    (s1p2, o3) -> initPart2,
    (s1p2, o5) -> initPart2,
    (s2p2, o4) -> initPart2,
    (s3p2, o4) -> s1p2,
    (s3p2, o5) -> s2p2
  )


  var init = initPart2
  var stateMachine = stateMachinePart2
  var states = statesPart2
  var events = eventsPart2


  def receive = {
    case GetInit => sender ! init
    case NextState(s, ev) => {
      if (states.contains(s) && events.contains(ev)) {
        val state = states(s)
        val event = events(ev)
        val tuple = (state, event)
        if (stateMachine.contains(tuple))
          sender ! stateMachine(tuple)
        else
          sender ! s"State $s, does not enable event $ev"
      } else sender ! s"your state: $s, event: $ev. States: $states, events: $events"
    }
  }
}

//