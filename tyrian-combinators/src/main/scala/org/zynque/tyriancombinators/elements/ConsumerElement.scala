package org.zynque.tyriancombinators.elements

import tyrian.*

trait ConsumerElement[F[_], I, S]
    extends TyrianElement[F, I, Nothing, Nothing, S]:
  def initialState: S
  def updateSimple(state: S, message: I): S

  def init: (S, Cmd[F, Nothing]) = (initialState, Cmd.None)
  def update(
      state: S,
      value: Either[I, Nothing]
  ): (S, Cmd[F, Either[Nothing, Nothing]]) =
    value match
      case Left(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.None)
      case _ => (state, Cmd.None)
