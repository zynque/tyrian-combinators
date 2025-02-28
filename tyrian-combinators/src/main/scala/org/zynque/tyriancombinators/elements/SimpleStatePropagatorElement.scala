package org.zynque.tyriancombinators.elements

import tyrian.*

trait SimpleStatePropagatorElement[F[_], M, S] extends TyrianElement[F, Any, S, M, S]:
  def initSimple: S
  def updateSimple(state: S, message: M): S
  def init: (S, Cmd[F, M]) = (initSimple, Cmd.None)
  def update(state: S, value: Either[Any, M]): (S, Cmd[F, Either[S, M]]) =
    value match
      case Right(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.emit(Left(newState)))
      case _ => (state, Cmd.None)
