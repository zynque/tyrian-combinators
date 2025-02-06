package org.zynque.tyriancombinators.elements

import tyrian.Html

type MessageTypes[F[_], I, O, T <: Tuple] = (Int, Any) // CombinedMessage[F, I, O, T, ?]

type HtmlTypesHelper[AllMessages, T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, _] *: tail =>
    Html[AllMessages] *: HtmlTypesHelper[AllMessages, tail]
  case _ => Nothing
}

type HtmlTypes[F[_], I, O, T <: Tuple] = HtmlTypesHelper[MessageTypes[F, I, O, T], T]

type StateTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, s] *: tail =>
    s *: StateTypes[tail]
}
