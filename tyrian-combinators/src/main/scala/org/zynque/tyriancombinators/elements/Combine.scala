package org.zynque.tyriancombinators.elements

import tyrian.Html
import tyrian.Cmd

// idea: Use lens instances to tell combiner how to construct combined messages & combined state?

def combineElements[F[_], I, O, T <: Tuple](
    elements: T
)(using ev: CompatibleElements[F, I, O, T])(
    f: HtmlTypes[T] => Html[CombinedMessage]
): TyrianElement[F, I, O, CombinedMessage, StateTypes[T]] =
  new TyrianElement[F, I, O, CombinedMessage, StateTypes[T]] {

  def init: (StateTypes[T], Cmd[F, CombinedMessage]) =
    val initialStatesAndCommandsList = elements.productIterator.map { e =>
      e.asInstanceOf[TyrianElement[F, I, O, ?, ?]].init
    }
    val initialStates = initialStatesAndCommandsList.map(_._1).toVector
    val initialCommands = initialStatesAndCommandsList.map(_._2).zipWithIndex
    val initialCommandsCombined = initialCommands.foldLeft(
      Cmd.None.asInstanceOf[Cmd[F, CombinedMessage]]
    ){
      case (cmds, (cmd, index)) =>
      cmds |+| cmd.asInstanceOf[Cmd[F, Any]].map(c => (index, c))
    }
    println("initialStates: " + initialStates)
    val initialStatesTuple =
      Tuple.fromArray(initialStates.toArray[Any]).asInstanceOf[StateTypes[T]]
    (initialStatesTuple, initialCommandsCombined)
  def update(
      state: StateTypes[T],
      value: Either[I, CombinedMessage]
  ): (StateTypes[T], Cmd[F, Either[O, CombinedMessage]]) =
    val result = value match {
      case Left(input) =>
        println("Dispatching input: " + input)
        // throw new Exception("broke here")
        dispatchInput(elements, state, input)
      case Right(message) =>
        println("Dispatching message: " + message)
        // throw new Exception("broke here")
        dispatchMessage(elements, state, message)
    }
    println("result: " + result)
    result
  def view(state: StateTypes[T]): Html[CombinedMessage] =
    println("A viewing states:" + state)
    val htmls = elements.productIterator
      .zip(state.asInstanceOf[Tuple].productIterator)
      .zipWithIndex
      .foldLeft(List.empty[Any]) { case (htmls, ((e, s), i)) =>
        println("viewing element: " + e)
        val html =
          e.asInstanceOf[TyrianElement[F, ?, ?, ?, ElementAt[T, F, I, O, i.type]#S]].view(s.asInstanceOf[ElementAt[T, F, I, O, i.type]#S])
        println("constructed view")
        val html2 = html.map(msg => (i, msg))
        html2 :: htmls
      }
      .reverse
    val htmlsTuple =
      Tuple.fromArray(htmls.toArray).asInstanceOf[HtmlTypes[T]]
    println("B viewing states:" + state)
    f(htmlsTuple)

  def dispatchInput[T <: Tuple, I, O](
      elements: T,
      states: StateTypes[T],
      input: I
  ): (StateTypes[T], Cmd[F, Either[O, CombinedMessage]]) = {
    val statesIterator = states.asInstanceOf[Tuple].productIterator
    val results = elements.productIterator.zip(statesIterator).map { case (e, s) =>
      e.asInstanceOf[TyrianElement[F, I, O, ?, ?]]
        .update(s.asInstanceOf, Left(input))
    }
    val newStates = results.map(_._1).toArray[Any]
    val commands = results
      .map(_._2)
      .foldLeft(
        Cmd.None.asInstanceOf[Cmd[F, Either[O, CombinedMessage]]]
      )((cmds, cmd) =>
        cmds |+| cmd.asInstanceOf[Cmd[F, Either[O, CombinedMessage]]]
      )
    (Tuple.fromArray(newStates).asInstanceOf[StateTypes[T]], commands)
  }

  def dispatchMessage[T <: Tuple, I, O](
      elements: T,
      states: StateTypes[T],
      message: CombinedMessage
  ): (StateTypes[T], Cmd[F, Either[O, CombinedMessage]]) = {
    val (index, m) = message
    println(s"dispatching with i: ${index} and m: ${m}")
    val element =
      elements.productElement(index).asInstanceOf[TyrianElement[F, I, O, ElementAt[T, F, I, O, index.type]#M, ElementAt[T, F, I, O, index.type]#S]]
    val state           = states.asInstanceOf[Tuple].productElement(index).asInstanceOf[ElementAt[T, F, I, O, index.type]#S]
    println("calling update on element")
    val typedMessage = m.asInstanceOf[ElementAt[T, F, I, O, index.type]#M]
    val (newState, cmd) = element.update(state, Right(typedMessage))
    println("update called on element")
    val cmdb = cmd.map {
      case Left(o)  => Left(o)
      case Right(m) => Right(Left((index, m)))
    }
    val statesArray = states.asInstanceOf[Tuple].toArray.asInstanceOf[Array[Any]]
    val newStates = statesArray.updated(index, newState)
    (
      Tuple.fromArray(newStates.toArray).asInstanceOf[StateTypes[T]],
      cmdb.asInstanceOf[Cmd[F, Either[O, CombinedMessage]]]
    )
  }
}
