package org.zynque.tyriancombinators.elements

export CompatibleElements.*

// Used for typechecking to ensure the elements in a tuple are compatible -
// i.e. they all have the same input and output types and are of the same effect type
// State and Message types can vary
trait CompatibleElements[F[_], I, O, T <: Tuple]

object CompatibleElements {
  given empty[F[_], I, O]: CompatibleElements[F, I, O, EmptyTuple] =
    new CompatibleElements[F, I, O, EmptyTuple] {}

  given nonEmpty[F[_], I, O, M, S, T <: Tuple](using
      ev: CompatibleElements[F, I, O, T]): CompatibleElements[F, I, O, TyrianElement[F, I, O, M, S] *: T] =
    new CompatibleElements[F, I, O, TyrianElement[F, I, O, M, S] *: T] {}
}
