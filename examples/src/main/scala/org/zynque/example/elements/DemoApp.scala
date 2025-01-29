package org.zynque.example.elements

// import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*

def DemoApp[F[_]] =
  TextInput.Element[F]("txt").feedInto(Label.Element[F]("Demo App"), (txt, lbl) => div(txt, lbl))
//    sideBySideExample[F].pairWith(
//   toggleExample[F],
//   (h1, h2) => div(h1, h2)
// ).pairWith(
//   textInputExample[F],
//   (h1, h2) => div(h1, h2)
// )
// .pairWith(
//   heterogeneousListExample[F],
//   (h1, h2) => div(h1, h2)
// )
