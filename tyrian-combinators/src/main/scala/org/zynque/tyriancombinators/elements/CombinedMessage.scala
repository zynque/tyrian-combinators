package org.zynque.tyriancombinators.elements

case class CombinedMessage[F[_], I, O, T <: Tuple, Index <: Int](
    index: I,
    message: ElementAt[T, F, I, O, Index]#M
)
