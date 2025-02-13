package org.zynque.tyriancombinators.data

import tyrian.*

// An element that "forks" or duplicates its output, emitting it twice, once as a Left and once as a Right
// The result can be fed into a pair of components so that they can both receive the same output
class Fork[F[_], I, O, M, S](
    element: DataElement[F, I, O, M, S]
) extends DataElement[
      F,
      I,
      Either[O, O],
      M,
      S
    ] {
  def init: (S, Cmd[F, M]) = element.init
  def update(state: S, value: Either[I, M]): (
      S,
      Cmd[F, Either[Either[O, O], M]]
  ) =
    element.update(state, value) match {
      case (s, cmd) =>
        val left = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(m)
        }
        val right = cmd.map {
          case Left(o)  => Left(Right(o))
          case Right(m) => Right(m)
        }
        (s, left |+| right)
    }
}
