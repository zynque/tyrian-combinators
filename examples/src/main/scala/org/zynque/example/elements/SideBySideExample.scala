package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.*

def sideBySideExample[F[_]] = {
  val b1     = CounterButton.Element[F]("Inc A")
  val b2     = CounterButton.Element[F]("Inc B")
  val paired = b1.pairWith(b2, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
  val accumulated = paired.eitherAccumulated
  val withTotalAsString = accumulated.mapOutput { case (a, b) =>
    (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label.Element[F]("(total)")
  withTotalAsString.feedInto(
    label,
    (a, b) => div(a.map(Left(_)), b.map(Right(_)))
  )
}
