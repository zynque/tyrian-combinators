package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*

object TextInput:
  class Element[F[_]](initialValue: String)
      extends SimpleStatePropagatorElement[F, String, String]:

    def initSimple = initialValue

    def updateSimple(state: String, message: String): String =
      message

    def view(state: String): Html[String] =
      input(
        `type` := "text",
        value := state.toString,
        onChange(identity)
      )
