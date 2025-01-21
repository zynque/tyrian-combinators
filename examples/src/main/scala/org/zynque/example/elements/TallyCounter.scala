package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*
import scala.CanEqual.derived

object CounterButton:
  enum Msg derives CanEqual:
    case Increment


  class Component[F[_]](label: String)
      extends SimpleStatePropagatorComponent[F, Msg, Int]:

    def initSimple = 0

    def updateSimple(state: Int, message: Msg): Int =
      message match
        case Msg.Increment => state + 1

    def view(state: Int): Html[Msg] =
      div(
        button(onClick(Msg.Increment))(label),
        div(state.toString)
      )

object ButtonComponent:
  enum Msg:
    case Clicked

  class Component[F[_]](label: String) extends ProducerComponent[F, Msg]:
    def view(state: Unit): Html[Msg] =
      button(onClick(Msg.Clicked))(label)
