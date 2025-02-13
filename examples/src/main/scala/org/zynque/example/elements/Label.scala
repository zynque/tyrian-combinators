package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.elements.*

class Label[F[_]](initialText: String)
    extends ConsumerElement[F, String, String]:

  def initialState = initialText

  def updateSimple(state: String, input: String): String =
    input

  def view(state: String): Html[Nothing] =
    // println("Label: viewing state") // todo: Investigate why this is being called so many times
    div(state)
