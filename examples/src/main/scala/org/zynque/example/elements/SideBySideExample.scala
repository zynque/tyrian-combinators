package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.*

def sideBySideExample[F[_]] = {
  val b1          = TallyCounter[F]("Inc A")
  val b2          = TallyCounter[F]("Inc B")
  val paired      = b1.pairWith(b2)((a, b) => div(a, b))
  val accumulated = paired.eitherAccumulated
  val withTotalAsString = accumulated.mapOutput { case (a, b) =>
    (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label[F]("(total)")
  withTotalAsString.feedInto(
    label,
    (a, b) => div(a, b)
  )
}
