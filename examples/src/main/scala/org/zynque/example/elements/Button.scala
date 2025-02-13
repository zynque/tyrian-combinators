package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*

class Button[F[_]](label: String) extends ProducerElement[F, Unit]:
  def view(state: Unit): Html[Unit] =
    button(onClick(()))(label)
