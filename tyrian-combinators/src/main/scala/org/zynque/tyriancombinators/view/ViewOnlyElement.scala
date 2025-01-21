package org.zynque.tyriancombinators.view

import org.zynque.tyriancombinators.elements.*
import tyrian.*

class ViewOnlyElement[F[_], I, O, M, S](
    viewElement: ViewElement[M, S],
    initialState: S
) extends TyrianElement[F, I, O, M, S] {
  def init: (S, Cmd[F, M]) = (initialState, Cmd.None)
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]]) =
    (state, Cmd.None)
  def view(state: S): Html[M] = viewElement.view(state)
}
