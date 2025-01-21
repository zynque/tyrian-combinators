package org.zynque.tyriancombinators.elements

import tyrian.*

trait ProducerComponent[F[_], O] extends TyrianComponent[F, Nothing, O, O, Unit]:
  def init: (Unit, Cmd[F, O]) = ((), Cmd.None)
  def update(state: Unit, value: Either[Nothing, O]): (Unit, Cmd[F, Either[O, O]]) =
    value match
      case Right(msg) => (state, Cmd.emit(Left(msg)))
      case _ => (state, Cmd.None)

trait ConsumerComponent[F[_], I, S] extends TyrianComponent[F, I, Nothing, Nothing, S]:
  def initialState: S
  def updateSimple(state: S, message: I): S

  def init: (S, Cmd[F, Nothing]) = (initialState, Cmd.None)
  def update(state: S, value: Either[I, Nothing]): (S, Cmd[F, Either[Nothing, Nothing]]) =
    value match
      case Left(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.None)
      case _ => (state, Cmd.None)

trait SimpleStatePropagatorComponent[F[_], M, S] extends TyrianComponent[F, Nothing, S, M, S]:
  def initSimple: S
  def updateSimple(state: S, message: M): S
  def init: (S, Cmd[F, M]) = (initSimple, Cmd.None)
  def update(state: S, value: Either[Nothing, M]): (S, Cmd[F, Either[S, M]]) =
    value match
      case Right(m) =>
        val newState = updateSimple(state, m)
        (newState, Cmd.emit(Left(newState)))
      case _ => (state, Cmd.None)
