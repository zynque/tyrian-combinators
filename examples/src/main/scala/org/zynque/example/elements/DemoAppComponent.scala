package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.TyrianComponentExtensions.pairWith

def DemoAppComponent[F[_]] = sideBySideExample[F].pairWith(
  toggleExample[F],
  (h1, h2) => div(h1.map(Left(_)), h2.map(Right(_)))
)
