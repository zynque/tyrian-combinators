package org.zynque.tyriancombinators.elements

import tyrian.Html

type InputTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, i, _, _, _] *: tail => Either[i, InputTypes[tail]]
  case _ => Nothing
}

type OutputTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, o, _, _] *: tail => Either[o, OutputTypes[tail]]
  case _ => Nothing
}

type MessageTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, m, _] *: tail => Either[m, MessageTypes[tail]]
  case _ => Nothing
}

type StateTypes[T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, s] *: tail => (s, StateTypes[tail])
  case _ => Nothing
}

type HtmlTypesHelper[AllMessages, T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, _] *: tail => Html[AllMessages] *: HtmlTypesHelper[AllMessages, tail]
  case _ => Nothing
}

type HtmlTypes[T <: Tuple] = HtmlTypesHelper[MessageTypes[T], T]

class Combiner[F[_]] {
  def combineElements[T <: Tuple](elements: T)(f: HtmlTypes[T] => Html[MessageTypes[T]]): 
    TyrianElement[F, InputTypes[T], OutputTypes[T], MessageTypes[T], StateTypes[T]] = {
      ???
  }
}
