package org.zynque.tyriancombinators.data

import org.zynque.tyriancombinators.elements.*
import tyrian.*

class DataElementWithView[F[_], I, O, M, S](
    dataElement: DataElement[F, I, O, M, S],
    viewFunction: S => Html[M]
) extends TyrianElement[F, I, O, M, S] {
  def init: (S, Cmd[F, M]) = dataElement.init
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]]) =
    dataElement.update(state, value)
  def view(state: S): Html[M] = viewFunction(state)
}
