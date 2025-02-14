package org.zynque.tyriancombinators.data

import tyrian.*

// Apply a function to transform the input channel before passing it to the element
class InputContramapped[F[_], I, I2, O, M, S](
    element: DataElement[F, I, O, M, S],
    f: I2 => I
) extends DataElement[F, I2, O, M, S] {
  override val init: (S, Cmd[F, M]) = element.init
  override def update(
      state: S,
      value: Either[I2, M]
  ): (S, Cmd[F, Either[O, M]]) =
    value match {
      case Left(i2) =>
        val i = f(i2)
        element.update(state, Left(i))
      case Right(m) =>
        element.update(state, Right(m))
    }
}

// class InputIgnored[F[_], I, O, M, S](
//     element: DataElement[F, I, O, M, S]
// ) extends DataElement[F, Any, O, M, S] {
//   override val init: (S, Cmd[F, M]) = element.init
//   override def update(
//       state: S,
//       value: Either[Any, M]
//   ): (S, Cmd[F, Either[O, M]]) =
//     value match {
//       case Left(_) =>
//         element.update(state, Left(()))
//       case Right(m) =>
//         element.update(state, Right(m))
//     }
//   }
