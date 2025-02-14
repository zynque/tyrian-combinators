package org.zynque.tyriancombinators.elements

import tyrian.*

trait ProducerElement[F[_], O] extends TyrianElement[F, Any, O, O, Unit]:
  def init: (Unit, Cmd[F, O]) = ((), Cmd.None)
  def update(state: Unit, value: Either[Any, O]): (Unit, Cmd[F, Either[O, O]]) =
    value match
      case Right(msg) => (state, Cmd.emit(Left(msg)))
      case _ => (state, Cmd.None)
