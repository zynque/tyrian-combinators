package org.zynque.example.elements

import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*
import tyrian.*
import tyrian.Html.*

def textInputExample[F[_]] = {
  val text  = TextInput[F]("Input")
  val label = Label[F]("Label")
  val result =
    text.feedInto(label, (textView, labelView) => div(textView, labelView))
  result
}
