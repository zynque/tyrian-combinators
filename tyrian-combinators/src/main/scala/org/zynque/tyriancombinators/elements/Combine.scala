package org.zynque.tyriancombinators.elements

import tyrian.Html
import tyrian.Cmd

class Combiner[F[_]] {
  def CombineElements[T <: Tuple, I, O](
      elements: T
  )( // todo: Something like (using ev: CompatibleElementTuple[T, F, I, O])(
      f: HtmlTypes[T] => Html[MessageTypes[T]]
  ): TyrianElement[F, I, O, MessageTypes[T], StateTypes[T]] =
    new TyrianElement[F, I, O, MessageTypes[T], StateTypes[T]] {
      def init: (StateTypes[T], Cmd[F, MessageTypes[T]]) =
        val initialStatesAndCommandsList = elements.productIterator.map { e =>
          e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].init
        }
        val initialStates = initialStatesAndCommandsList.map(_._1).toArray[Any]
        val initialCommands =
          initialStatesAndCommandsList.map(_._2).toArray[Any]
        val initialStatesTuple =
          Tuple.fromArray(initialStates).asInstanceOf[StateTypes[T]]
        val initialCommandsTuple = initialCommands.foldLeft(
          Cmd.None.asInstanceOf[Cmd[F, MessageTypes[T]]]
        )((cmds, cmd) => cmds |+| cmd.asInstanceOf[Cmd[F, MessageTypes[T]]])
        println("initialStatesTuple: " + initialStatesTuple)
        (initialStatesTuple, initialCommandsTuple)
      def update(
          state: StateTypes[T],
          value: Either[I, MessageTypes[T]]
      ): (StateTypes[T], Cmd[F, Either[O, MessageTypes[T]]]) =
        val rawStates = state.asInstanceOf[Tuple].productIterator
        val result = value match {
          case Left(input) =>
            println("Dispatching input: " + input)
            // throw new Exception("broke here")
            dispatchInput(elements, rawStates, input)
          case Right(message) =>
            println("Dispatching message: " + message)
            // throw new Exception("broke here")
            dispatchMessage(elements, rawStates, message)
        }
        println("result: " + result)
        result
      def view(state: StateTypes[T]): Html[MessageTypes[T]] =
        println("A viewing states:" + state)
        val rawStates = state.asInstanceOf[Tuple].productIterator
        val htmls = elements.productIterator
          .zip(rawStates)
          .foldLeft(
            (List.empty[Any], (a: Any) => Left(a).asInstanceOf[Any])
          ) { case ((htmls, msgWrapper), (e, s)) =>
            val html =
              e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].view(s.asInstanceOf)
            val html2 = html.map(msgWrapper)
            val msgWrapper2 = (msg: Any) => Right(msgWrapper(msg))
            (html2 :: htmls, msgWrapper2)
          }
          ._1.reverse
        val htmlsTuple =
          Tuple.fromArray(htmls.toArray).asInstanceOf[HtmlTypes[T]]
        println("B viewing states:" + state)
        f(htmlsTuple)
    }

  def dispatchInput[T <: Tuple, I, O](
      elements: T,
      states: Iterator[Any],
      input: I
  ): (StateTypes[T], Cmd[F, Either[O, MessageTypes[T]]]) = {
    val results = elements.productIterator.zip(states).map { case (e, s) =>
      e.asInstanceOf[TyrianElement[F, I, O, ?, ?]]
        .update(s.asInstanceOf, Left(input))
    }
    val newStates = results.map(_._1).toArray[Any]
    val commands = results
      .map(_._2)
      .foldLeft(
        Cmd.None.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]]
      )((cmds, cmd) =>
        cmds |+| cmd.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]]
      )
    (Tuple.fromArray(newStates).asInstanceOf[StateTypes[T]], commands)
  }

  def dispatchMessage[T <: Tuple, I, O](
      elements: T,
      states: Iterator[Any],
      message: MessageTypes[T]
  ): (StateTypes[T], Cmd[F, Either[O, MessageTypes[T]]]) = {
    message match {
      case Left(m) =>
        println("found left of message")
        try {
          val (state, cmd) = elements
            .productElement(0)
            .asInstanceOf[TyrianElement[F, I, O, FirstMessageType[T], FirstStateType[T]]]
            .update(states.next.asInstanceOf[FirstStateType[T]], Right(m.asInstanceOf[FirstMessageType[T]]))
          println("performed update on first element")
          val cmdb = cmd.map {
            case Left(o)  => Left(o)
            case Right(m) => Right(Left(m))
          }
          val newStates = Tuple.fromArray((state :: states.toList.tail).toArray)
          println("new states: " + newStates)
          (newStates.asInstanceOf[StateTypes[T]], cmdb.asInstanceOf[Cmd[F, Either[O, MessageTypes[T]]]])
        } catch {
          case e: Exception => println(e); throw e;
        }
      case Right(m2) =>
        println("found right of message")
        val theTail = Tuple.fromArray(elements.productIterator.toList.tail.toArray)
        val (newStates, cmd) = dispatchMessage(theTail, states, m2.asInstanceOf)
        val cmdb = cmd.map {
          case Left(o)  => Left(o)
          case Right(m) => Right(Right(m))
        }
        (newStates.asInstanceOf, cmdb.asInstanceOf)
      case m3 =>
        println("found neither left nor right of message")
        println("m3: " + m3)
        val (state, cmd) = elements
          .productElement(0)
          .asInstanceOf[TyrianElement[F, I, O, ?, ?]]
          .update(states.next.asInstanceOf, Right(m3.asInstanceOf))
        val cmdb = cmd.map {
          case Left(o)  => Left(o)
          case Right(m) => Right(Left(m))
        }
        val newStates = Tuple.fromArray((state :: states.toList.tail).toArray)
        println("new states: " + newStates)
        (newStates.asInstanceOf, cmdb.asInstanceOf)
    }
  }

  // def indexToEither[T <: Tuple, M](index: Int, value: M)(using
  //     ev: MessageTypes[T]
  // ): MessageTypes[T] =
  //   // if (index == 0) Left(value)
  //   // else Right(indexToEitherHelper(index - 1, value))
  //   ???

  // def indexToEitherHelper[T](index: Int, value: T): T =
  //   // if (index == 0) value
  //   // else Right(indexToEitherHelper(index - 1, value))
  //   ???

  // def indexOfEither[T <: Tuple](either: MessageTypes[T]): Int =
  //   // either match {
  //   //   case Left(_) => 0
  //   //   case Right(m:MessageTypes[Tuple.Tail[T]]) => indexOfEitherHelper[Tuple.Tail[T], MessageTypes[Tuple.Tail[T]]](1, m)
  //   //   case _ => 0
  //   // }
  //   ???

  // def indexOfEitherHelper[T <: Tuple, M](i: Int, either: MessageTypes[T]): Int =
  //   // either match {
  //   //   case Left(_) => i
  //   //   case Right(m:MessageTypes[Tuple.Tail[T]]) => indexOfEitherHelper[Tuple.Tail[T], M](1, m)
  //   //   case _ => i
  //   // }
  //   ???
}
