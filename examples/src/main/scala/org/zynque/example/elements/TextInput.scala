package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*

class TextInput[F[_]](initialValue: String)
    extends SimpleStatePropagatorElement[F, String, String]:

  def initSimple = initialValue

  def updateSimple(state: String, message: String): String =
    message

  def view(state: String): Html[String] =
    input(
      `type`      := "text",
      placeholder := state.toString,
      onInput(identity)
    )
