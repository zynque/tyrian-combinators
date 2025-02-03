package org.zynque.tyriancombinators.elements

import tyrian.Html
import tyrian.Cmd

class Combiner {
  def CombineElements[F[_], I, O, T <: Tuple](
      elements: T
  )( using ev: CompatibleElements[F, I, O, T])(
      f: HtmlTypes[F, I, O, T] => Html[MessageTypes[F, I, O, T]]
  ): TyrianElement[F, I, O, MessageTypes[F, I, O, T], StateTypes[T]] =
    new TyrianElement[F, I, O, MessageTypes[F, I, O, T], StateTypes[T]] {
      
      def init: (StateTypes[T], Cmd[F, MessageTypes[F, I, O, T]]) =
        ???
        // val initialStatesAndCommandsList = elements.productIterator.map { e =>
        //   e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].init
        // }
        // val initialStates = initialStatesAndCommandsList.map(_._1).toVector
        // val initialCommands =
        //   initialStatesAndCommandsList.map(_._2).toArray[Any]
        // val initialCommandsCombined = initialCommands.foldLeft(
        //   Cmd.None.asInstanceOf[Cmd[F, MessageTypes[T]]]
        // )((cmds, cmd) => cmds |+| cmd.asInstanceOf[Cmd[F, MessageTypes[T]]])
        // println("initialStates: " + initialStates)
        // (initialStates, initialCommandsCombined)
      def update(state: StateTypes[T], value: Either[I, MessageTypes[F, I, O, T]]):
        (StateTypes[T], Cmd[F, Either[O, MessageTypes[F, I, O, T]]]) =
          ???
        // val result = value match {
        //   case Left(input) =>
        //     println("Dispatching input: " + input)
        //     // throw new Exception("broke here")
        //     dispatchInput(elements, state, input)
        //   case Right(message) =>
        //     println("Dispatching message: " + message)
        //     // throw new Exception("broke here")
        //     dispatchMessage(elements, state, message)
        // }
        // println("result: " + result)
        // result
      def view(state: StateTypes[T]): Html[MessageTypes[F, I, O, T]] =
        ???
        // println("A viewing states:" + state)
        // val htmls = elements.productIterator
        //   .zip(state)
        //   .foldLeft((List.empty[Any], 0)) { case ((htmls, i), (e, s)) =>
        //     println("viewing element: " + e)
        //     val html =
        //       e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].view(s.asInstanceOf)
        //     println("constructed view")
        //     val html2 = html.map(msg => (i, msg))
        //     (html2 :: htmls, i + 1)
        //   }
        //   ._1
        //   .reverse
        // val htmlsTuple =
        //   Tuple.fromArray(htmls.toArray).asInstanceOf[HtmlTypes[T]]
        // println("B viewing states:" + state)
        // f(htmlsTuple)
    }

  // def dispatchInput[T <: Tuple, I, O](
  //     elements: T,
  //     states: Vector[Any],
  //     input: I
  // ): (StateTypes[T], Cmd[F, Either[O, MessageTypes[T]]]) = {
  //   val results = elements.productIterator.zip(states).map { case (e, s) =>
  //     e.asInstanceOf[TyrianElement[F, I, O, ?, ?]]
  //       .update(s.asInstanceOf, Left(input))
  //   }
  //   val newStates = results.map(_._1).toArray[Any]
  //   val commands = results
  //     .map(_._2)
  //     .foldLeft(
  //       Cmd.None.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]]
  //     )((cmds, cmd) =>
  //       cmds |+| cmd.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]]
  //     )
  //   (Tuple.fromArray(newStates).asInstanceOf[StateTypes[T]], commands)
  // }

  // def dispatchMessage[T <: Tuple, I, O](
  //     elements: T,
  //     states: Vector[Any],
  //     message: MessageTypes[T]
  // ): (StateTypes[T], Cmd[F, Either[O, MessageTypes[T]]]) = {
  //   val (i, m) = message
  //   println(s"dispatching with i: $i and m: $m")
  //   val element =
  //     elements.productElement(i).asInstanceOf[TyrianElement[F, I, O, ?, ?]]
  //   val state           = states(i)
  //   println("calling update on element")
  //   val (newState, cmd) = element.update(state.asInstanceOf, m.asInstanceOf)
  //   println("update called on element")
  //   val cmdb = cmd.map {
  //     case Left(o)  => Left(o)
  //     case Right(m) => Right(Left((i, m)))
  //   }
  //   val newStates = states.updated(i, newState)
  //   (
  //     newStates.asInstanceOf[StateTypes[T]],
  //     cmdb.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]]
  //   )
  // }
}
