package org.zynque.example.elements

import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.extensions.TyrianElementExtensions.*
import tyrian.*
import tyrian.Html.*

def heterogeneousListExample[F[_]] = {
  enum ExampleUpdate:
    case Input1(value: String)
    case Input2(value: String)
    case Counter(value: Int)

  val text1 = TextInput.Element[F]("Input 1").mapOutput(ExampleUpdate.Input1.apply)
  val text2 = TextInput.Element[F]("Input 2").mapOutput(ExampleUpdate.Input2.apply)
  val counter = CounterButton.Element[F]("Counter").mapOutput(ExampleUpdate.Counter.apply)

  val elements = (text1, text2, counter)

  val combined = combineElements (elements) {
    case (t1, t2, c) =>
      div(
        t1,
        t2,
        c
      )
  }

  val combinedAsString = combined.mapOutput{
    case ExampleUpdate.Input1(v) => v
    case ExampleUpdate.Input2(v) => v
    case ExampleUpdate.Counter(c) => c.toString()
  }

  val label = Label.Element[F]("Label")

  val result = combinedAsString.feedInto(label, (combinedView, labelView) => div(combinedView, labelView))

  result
}
