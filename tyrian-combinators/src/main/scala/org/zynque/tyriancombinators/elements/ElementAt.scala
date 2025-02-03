package org.zynque.tyriancombinators.elements

import scala.compiletime.ops.int.+

trait ElementAt[T <: Tuple, F[_], I, O, Index <: Int]:
  type M
  type S
  def get(t: T): TyrianElement[F, I, O, M, S]

object ElementAt {
  given baseCase[H <: TyrianElement[F, I, O, M0, S0], F[
      _
  ], I, O, M0, S0, T <: Tuple]: ElementAt[H *: T, F, I, O, 0] with
    type M = M0
    type S = S0
    def get(t: H *: T): TyrianElement[F, I, O, M, S] = t.head

  given recursiveCase[H <: TyrianElement[F, I, O, M0, S0], F[
      _
  ], I, O, M0, S0, T <: Tuple, Index <: Int](using
      ev: ElementAt[T, F, I, O, Index]
  ): ElementAt[H *: T, F, I, O, Index + 1] with
    type M = ev.M
    type S = ev.S
    def get(t: H *: T): TyrianElement[F, I, O, M, S] = ev.get(t.tail)
}
