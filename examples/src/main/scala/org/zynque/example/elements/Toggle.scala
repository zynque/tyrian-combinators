package org.zynque.example.elements

import tyrian.*
import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.extensions.*

// todo: generalize
// When input == true, displays first counter, when input == false, displays second counter
class Toggle[F[_]]
    extends TyrianElement[
      F,
      Boolean,
      Nothing,
      Boolean,
      (Boolean, Int, Int)
    ] {
  val incA = TallyCounter[F]("Inc A").propagateState
  val incB = TallyCounter[F]("Inc B").propagateState

  def init: ((Boolean, Int, Int), Cmd[F, Boolean]) = ((true, 0, 0), Cmd.None)
  def update(
      state: (Boolean, Int, Int),
      value: Either[Boolean, Boolean]
  ): ((Boolean, Int, Int), Cmd[F, Either[Nothing, Boolean]]) = {
    val (b, i1, i2) = state
    value match {
      case Left(b)      => ((b, i1, i2), Cmd.None)
      case Right(true)  => ((b, i1 + 1, i2), Cmd.None)
      case Right(false) => ((b, i1, i2 + 1), Cmd.None)
    }
  }
  def view(state: (Boolean, Int, Int)): Html[Boolean] = {
    val (b, i1, i2) = state
    if b then incA.view(i1).map(_ => true)
    else incB.view(i2).map(_ => false)
  }
}
