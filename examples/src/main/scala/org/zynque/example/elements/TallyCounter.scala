package org.zynque.example.elements

import org.zynque.tyriancombinators.data.*
import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*

object TallyCounter:
  def apply[F[_]](label: String) =
    val counter = DataElement.stateTransformer[F, Unit, Int](0)((s, _) => s + 1)
    val displayedCounter = counter.withView((state: Int) => div(state.toString))
    val button = Button[F](label)
    button.feedInto(displayedCounter, div(_, _))
