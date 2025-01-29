package org.zynque.tyriancombinators.elements

import tyrian.Html

// type CompatibleElementTuple[T <: Tuple, F[_], I, O] = T match {
//   case TyrianElement[F, I, O, m, s] *: EmptyTuple =>
//     TyrianElement[F, I, O, m, s] *: EmptyTuple
//   case TyrianElement[F, I, O, m, s] *: tail =>
//     TyrianElement[F, I, O, m, s] *: CompatibleElementTuple[tail, F, I, O]
//   case _ => Nothing
// }

type MessageTypes[T <: Tuple] = T match {
  case TyrianElement[_, _, _, m, _] *: EmptyTuple => m
  case TyrianElement[_, _, _, m, _] *: tail => Either[m, MessageTypes[tail]]
  case _                                    => Nothing
}

type FirstMessageType[T <: Tuple] = T match {
  case TyrianElement[_, _, _, m, _] *: _ => m
  case _ => Nothing
}

type StateTypes[T <: Tuple] = T match {
  case TyrianElement[_, _, _, _, s] *: EmptyTuple => s *: EmptyTuple
  case TyrianElement[_, _, _, _, s] *: tail       => s *: StateTypes[tail]
  case _                                          => Nothing
}

type FirstStateType[T <: Tuple] = T match {
  case TyrianElement[_, _, _, _, s] *: _ => s
  case _ => Nothing
}

type HtmlTypesHelper[AllMessages, T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, _] *: tail =>
    Html[AllMessages] *: HtmlTypesHelper[AllMessages, tail]
  case _ => Nothing
}

type HtmlTypes[T <: Tuple] = HtmlTypesHelper[MessageTypes[T], T]
