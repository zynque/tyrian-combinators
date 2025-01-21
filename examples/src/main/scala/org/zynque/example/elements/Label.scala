package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.elements.*

object Label:
  type State = String
  type Input = String
  
  class Component[F[_]](initialText: State)
      extends ConsumerComponent[F, Input, State]:

    def initialState = initialText

    def updateSimple(state: State, input: Input): State =
      input

    def view(state: State): Html[Nothing] =
      div(state)
