package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*
import tyrian.*
import tyrian.Html.*

def textInputExample[F[_]] = {
  val text = TextInput.Element[F]("Input")
  val label = Label.Element[F]("Label")
  text.feedInto(label, (textView, labelView) => div(textView, labelView))
}

def heterogeneousListExample[F[_]] = {
  enum ExampleUpdate:
    case Input1(value: String)
    case Input2(value: String)
    case Counter(value: Int)

  // val text0 = TextInput.Element[F]("Input 0")
  val text1 = TextInput.Element[F]("Input 1").mapOutput(ExampleUpdate.Input1.apply)
  val text2 = TextInput.Element[F]("Input 2").mapOutput(ExampleUpdate.Input2.apply)
  val counter = CounterButton.Element[F]("Counter").mapOutput(ExampleUpdate.Counter.apply)

  val elements = (text1, text2, counter)

  // val m = messages(elements)
  // val h = htmls(elements)
  // val s = states(elements)
  // println(m)
  // println(h)
  // println(s)

  val combined = combineElements (elements) {
    case (t1, t2, c) =>
      div(
        t1,
        t2,
        c
      )
  }

  val combinedAsString = combined.mapOutput(_.toString())

  val label = Label.Element[F]("Label").mapOutput(a => a)

  combinedAsString.feedInto(label, (combinedView, labelView) => div(combinedView, labelView))

  // text0.feedInto(label, (textView, labelView) => div(textView, labelView))
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
