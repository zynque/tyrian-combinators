package org.zynque.tyriancombinators.elements

import tyrian.Html

type CombinedMessage = (Int, Any)

type HtmlTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, _] *: tail =>
    Html[CombinedMessage] *: HtmlTypes[tail]
  case _ => Nothing
}

type StateTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, s] *: tail =>
    s *: StateTypes[tail]
}
