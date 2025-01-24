package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*
import tyrian.*
import tyrian.Html.*

def heterogeneousListExample[F[_]] = {
  // case class Inputs(text1: String, text2: String, counter: Int)
  val text1 = TextInput.Element[F]("Input 1")
  val text2 = TextInput.Element[F]("Input 2")
  val counter = CounterButton.Element[F]("Counter")

  val combined = Combiner[F].combineElements((text1, text2, counter)) {
    case (t1, t2, c) =>
      div(
        t1,
        t2,
        c
      )
  }

  val label = Label.Element[F]("Label")
  val s = combined.mapOutput(_.toString())
  s.feedInto(label, (combinedView, labelView) => div(combinedView, labelView))
}

def homogeneousListExample = {
  // https://www.w3schools.com/howto/howto_js_todolist.asp   
}

def dynamicSubelementCreationExample = {
  // we want to be able to create a new subelement at run time, based on user-supplied parameters
  // and responsively display it

}

def externalStreamExample = {
  // we want to be able to subscribe to an external stream of data and display it
}
