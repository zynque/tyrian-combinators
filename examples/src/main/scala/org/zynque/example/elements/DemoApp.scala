package org.zynque.example.elements

import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*

def DemoApp[F[_]] =
  sideBySideExample[F]
    .pairWith(toggleExample[F])(div(_, _))
    .pairWith(textInputExample[F])(div(_, _))
    .pairWith(heterogeneousListExample[F])(div(_, _))
    .pairWith(homogeneousListExample[F])(div(_, _))

// todo: requires examples to ignore inputs/outputs so they can be uniform
//       or else update combineElements to handle different input/output types
// def DemoApp[F[_]] = {
//   val examples = (
//     sideBySideExample[F],
//     toggleExample[F],
//     textInputExample[F],
//     heterogeneousListExample[F],
//     homogeneousListExample[F]
//   )
//   combineElements(examples) {
//     case (h1, h2, h3, h4, h5) =>
//       div(h1, h2, h3, h4, h5)
//   }
// }
