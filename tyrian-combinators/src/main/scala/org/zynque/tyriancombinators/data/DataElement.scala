package org.zynque.tyriancombinators.data

import tyrian.*

// A Tyrian Element that handles messages and state but has no view
// F: Effect Type (Cats Effect or ZIO Task)
// I: Input, O: Output, S: State/Model, M: Message
trait DataElement[F[_], I, O, M, S]:
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])

object DataElement {
  def fromFunction[F[_], I, O](f: I => O): DataElement[F, I, O, Nothing, Unit] =
    new DataElement[F, I, O, Nothing, Unit] {
      def init: (Unit, Cmd[F, Nothing]) = ((), Cmd.None)
      def update(
          state: Unit,
          value: Either[I, Nothing]
      ): (Unit, Cmd[F, Either[O, Nothing]]) =
        value match {
          case Left(i) => ((), Cmd.emit(Left(f(i))))
          case _       => ((), Cmd.None)
        }
    }

  def stateTransformer[F[_], I, S](initialState: S)(updateFunction: (S, I) => S):
    DataElement[F, I, S, Nothing, S] = new DataElement[F, I, S, Nothing, S] {
      def init: (S, Cmd[F, Nothing]) = (initialState, Cmd.None)
      def update(
          state: S,
          value: Either[I, Nothing]
      ): (S, Cmd[F, Either[S, Nothing]]) =
        value match {
          case Left(m) =>
            val s2 = updateFunction(state, m)
            (s2, Cmd.emit(Left(s2)))
          case _       => (state, Cmd.None)
        }
    }
}
