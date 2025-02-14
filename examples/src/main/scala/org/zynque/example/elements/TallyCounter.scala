package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*

class TallyCounter[F[_]](label: String)
    extends SimpleStatePropagatorElement[F, Unit, Int]:

  def initSimple = 0

  def updateSimple(state: Int, message: Unit): Int = state + 1

  def view(state: Int): Html[Unit] =
    div(
      button(onClick(()))(label),
      div(state.toString)
    )
