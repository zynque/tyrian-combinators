package org.zynque.example.elements

import tyrian.*
import org.zynque.tyriancombinators.elements.TyrianElement

// todo: consider optimizations for long lists - use vectors?
class ListElement[F[_], A](
    initialItems: List[A],
    listView: List[A] => Html[ListMsg[A]]
) extends TyrianElement[F, ListMsg[A], Nothing, ListMsg[A], List[A]] {
  def init = (initialItems, Cmd.None)
  def update(
      state: List[A],
      value: Either[ListMsg[A], ListMsg[A]]
  ): (List[A], Cmd[F, Either[Nothing, ListMsg[A]]]) =
    value.merge match {
      case ListMsg.AddItem(item) =>
        (item :: state, Cmd.None)
      case ListMsg.RemoveItem(index) =>
        val newState = state.take(index) ++ state.drop(index + 1)
        (newState, Cmd.None)
      case ListMsg.UpdateItem(index, newValue) =>
        val updatedState =
          state.updated(index, newValue)
        (updatedState, Cmd.None)
    }
  def view(state: List[A]): Html[ListMsg[A]] =
    listView(state)
}
