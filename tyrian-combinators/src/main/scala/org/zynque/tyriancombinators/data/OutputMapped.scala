package org.zynque.tyriancombinators.data

import tyrian.*

// Transforms an element into one that outputs a transformed output value
class OutputMapped[F[_], I, O, M, S, O2](
    element: DataElement[F, I, O, M, S],
    f: O => O2
) extends DataElement[F, I, O2, M, S] {
  override val init: (S, Cmd[F, M]) = element.init
  override def update(
      state: S,
      value: Either[I, M]
  ): (S, Cmd[F, Either[O2, M]]) =
    val (state2, output) = element.update(state, value)
    val output2 = for {
      outOrMsg <- output
      transformed = outOrMsg.left.map(f)
    } yield transformed
    (state2, output2)
}
