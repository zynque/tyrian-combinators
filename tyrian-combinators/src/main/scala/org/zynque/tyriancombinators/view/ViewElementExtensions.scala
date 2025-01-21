package org.zynque.tyriancombinators.view

import org.zynque.tyriancombinators.elements.*
export ViewElementExtensions.*

object ViewElementExtensions {
  extension [M, S](element: ViewElement[M, S]) {
    def asElement[F[_], I, O](
        initialState: S
    ): TyrianElement[F, I, O, M, S] =
      new ViewOnlyElement(element, initialState)
  }
}
