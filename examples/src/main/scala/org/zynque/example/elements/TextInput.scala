package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import tyrian.*
import tyrian.Html.*

object TextInput:
  type Msg = String

  class Element[F[_]](initialValue: String)
      extends SimpleStatePropagatorElement[F, Msg, Msg]:

    def initSimple = initialValue

    def updateSimple(state: Msg, message: Msg): Msg =
      message

    def view(state: Msg): Html[Msg] =
      input(
        `type` := "text",
        value := state.toString,
        onChange(identity)
      )
