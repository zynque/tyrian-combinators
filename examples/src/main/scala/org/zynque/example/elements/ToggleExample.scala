package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.*

// toggle example
// two buttons, each, when pressed, reveals a different counter instance
def toggleExample[F[_]] = {
  val showA = ButtonElement.Element[F]("Show A")
  val showB = ButtonElement.Element[F]("Show B")
  val selector = showA
    .pairWith(showB){(a, b) => div(a, b)}
    .mapOutput {
      case Left(_)  => true
      case Right(_) => false
    }
  val toggle = Toggle[F]()

  val label = Label.Element[F]("Selected...").contramapInput((b: Boolean) => b match {
    case true  => "A Selected"
    case false => "B Selected"
  })

  val toggleAndLabel = toggle.pairWith(label){(a, b) => div(a, b)}

  selector.duplicate.feedInto(
    toggleAndLabel,
    (a, b) => div(a, b)
  )
}
