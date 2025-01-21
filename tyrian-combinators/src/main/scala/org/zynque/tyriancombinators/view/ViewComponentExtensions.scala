package org.zynque.tyriancombinators.view

import org.zynque.tyriancombinators.elements.*
export ViewComponentExtensions.*

object ViewComponentExtensions {
  extension [M, S](component: ViewComponent[M, S]) {
    def asComponent[F[_], I, O](
        initialState: S
    ): TyrianComponent[F, I, O, M, S] =
      new ViewOnlyComponent(component, initialState)
  }
}
