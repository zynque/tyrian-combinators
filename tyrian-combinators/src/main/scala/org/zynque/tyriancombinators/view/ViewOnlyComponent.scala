package org.zynque.tyriancombinators.view

import org.zynque.tyriancombinators.elements.*
import tyrian.*

class ViewOnlyComponent[F[_], I, O, M, S](
    component: ViewComponent[M, S],
    initialState: S
) extends TyrianComponent[F, I, O, M, S] {
  def init: (S, Cmd[F, M]) = (initialState, Cmd.None)
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]]) =
    (state, Cmd.None)
  def view(state: S): Html[M] = component.view(state)
}
