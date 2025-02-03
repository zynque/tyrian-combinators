package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.elements.*

object Label:
  type State = String
  type Input = String
  
  class Element[F[_]](initialText: State)
      extends ConsumerElement[F, Input, State]:

    def initialState = initialText

    def updateSimple(state: State, input: Input): State =
      input

    def view(state: State): Html[Nothing] =
      // println("Label: viewing state") // todo: Investigate why this is being called so many times
      div(state)
