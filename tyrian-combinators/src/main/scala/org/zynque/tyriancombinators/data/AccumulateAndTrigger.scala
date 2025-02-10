package org.zynque.tyriancombinators.data

import tyrian.*

// consumes either A or B, stores the most recent value of A, and outputs upon observing B
// (B's value is ignored)
// useful for example when creating a form with a submit button
class AccumulateAndTrigger[F[_], A, B]
    extends DataElement[
      F,
      Either[A, B],
      A,
      Nothing,
      Option[A]
    ] {
  def init: (Option[A], Cmd[F, Nothing]) = (None, Cmd.None)
  def update(
      state: Option[A],
      value: Either[Either[A, B], Nothing]
  ): (Option[A], Cmd[F, Either[A, Nothing]]) =
    value match
      case Left(Left(a)) =>
        (Some(a), Cmd.None)
      case Left(Right(b)) =>
        state match {
          case Some(a) => (Some(a), Cmd.emit(Left(a)))
          case None    => (None, Cmd.None)
        }
      case _ => (state, Cmd.None)
}
