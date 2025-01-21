package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.extensions.*

def sideBySideExample[F[_]] = {
  val b1     = CounterButton.Component[F]("Inc A")
  val b2     = CounterButton.Component[F]("Inc B")
  val paired = b1.pairWith(b2, (a, b) => div(a.map(Left(_)), b.map(Right(_))))
  val accumulated = paired.eitherAccumulated
  val withTotalAsString = accumulated.mapOutput { case (a, b) =>
    (a.getOrElse(0) + b.getOrElse(0)).toString
  }
  val label = Label.Component[F]("(total)")
  withTotalAsString.feedInto(
    label,
    (a, b) => div(a.map(Left(_)), b.map(Right(_)))
  )
}

def HeterogeneousListExample = {
  
}

def HomogeneousListExample = {

}

def DynamicSubcomponentCreationExample = {
  // we want to be able to create a new subcomponent at run time, based on user-supplied parameters
  // and responsively display it

}

def ExternalStreamExample = {
  // we want to be able to subscribe to an external stream of data and display it
}
