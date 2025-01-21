package org.zynque.tyriancombinators.data

import tyrian.*

// Transforms an element into one that always outputs its latest state (ignoring the output of the original element, if any)
class StatePropagated[F[_], I, O, M, S](
    element: DataElement[F, I, O, M, S]
) extends DataElement[F, I, S, M, S] {
  override val init: (S, Cmd[F, M]) = element.init
  override def update(
      state: S,
      value: Either[I, M]
  ): (S, Cmd[F, Either[S, M]]) = {
    val (s2, cmd) = element.update(state, value)
    val cmd2 = cmd.map {
      case Left(o)  => Left(s2)
      case Right(m) => Right(m)
    }
    (s2, cmd2)
  }
}
