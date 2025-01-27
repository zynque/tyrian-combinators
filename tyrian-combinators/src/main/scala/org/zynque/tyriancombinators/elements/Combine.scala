package org.zynque.tyriancombinators.elements

import tyrian.Html
import tyrian.Cmd

type InputTypes[T <: Tuple] = T match {
  case TyrianElement[_, i, _, _, _] *: EmptyTuple => i
  case TyrianElement[_, i, _, _, _] *: tail => Either[i, InputTypes[tail]]
  case _                                    => Nothing
}

type OutputTypes[T <: Tuple] = T match {
  case TyrianElement[_, _, o, _, _] *: EmptyTuple => o
  case TyrianElement[_, _, o, _, _] *: tail => Either[o, OutputTypes[tail]]
  case _                                    => Nothing
}

type MessageTypes[T <: Tuple] = T match {
  case TyrianElement[_, _, _, m, _] *: EmptyTuple => m
  case TyrianElement[_, _, _, m, _] *: tail => Either[m, MessageTypes[tail]]
  case _                                    => Nothing
}

type StateTypes[T <: Tuple] = T match {
  case TyrianElement[_, _, _, _, s] *: EmptyTuple => s *: EmptyTuple
  case TyrianElement[_, _, _, _, s] *: tail => s *: StateTypes[tail]
  case _ => Nothing
}

type HtmlTypesHelper[AllMessages, T <: Tuple] = T match {
  case EmptyTuple => EmptyTuple
  case TyrianElement[_, _, _, _, _] *: tail =>
    Html[AllMessages] *: HtmlTypesHelper[AllMessages, tail]
  case _ => Nothing
}

type HtmlTypes[T <: Tuple] = HtmlTypesHelper[MessageTypes[T], T]

class Combiner[F[_]] {
  def CombineElements[T <: Tuple](elements: T)(
      f: HtmlTypes[T] => Html[MessageTypes[T]]
  ) : TyrianElement[F, InputTypes[T], OutputTypes[T], MessageTypes[
        T
      ], StateTypes[T]] 
      = new TyrianElement[F, InputTypes[T], OutputTypes[T], MessageTypes[
        T
      ], StateTypes[T]] {
    def init: (StateTypes[T], Cmd[F, MessageTypes[T]]) =
      val initialStatesAndCommandsList = elements.productIterator.map { e =>
        e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].init
      }
      val initialStates   = initialStatesAndCommandsList.map(_._1).toArray[Any]
      val initialCommands = initialStatesAndCommandsList.map(_._2).toArray[Any]
      val initialStatesTuple =
        Tuple.fromArray(initialStates).asInstanceOf[StateTypes[T]]
      val initialCommandsTuple = initialCommands.foldLeft(
        Cmd.None.asInstanceOf[Cmd[F, MessageTypes[T]]]
      )((cmds, cmd) => cmds |+| cmd.asInstanceOf[Cmd[F, MessageTypes[T]]])
      (initialStatesTuple, initialCommandsTuple)
    def update(
        state: StateTypes[T],
        value: Either[InputTypes[T], MessageTypes[T]]
    ): (StateTypes[T], Cmd[F, Either[OutputTypes[T], MessageTypes[T]]]) =
      val rawStates = state.asInstanceOf[Tuple].productIterator
      val result = value match {
        case Left(input) =>
          dispatchInput(elements, rawStates, input)
        case Right(message) =>
          dispatchMessage(elements, rawStates, message)
      }
      result
    def view(state: StateTypes[T]): Html[MessageTypes[T]] =
      val rawStates = state.asInstanceOf[Tuple].productIterator
      val htmls = elements.productIterator.zip(rawStates).foldLeft(
        List.empty[Any]
      ) {
        case (acc, (e, s)) =>
          val html = e.asInstanceOf[TyrianElement[F, ?, ?, ?, ?]].view(s.asInstanceOf)
          html :: acc
      }.reverse
      val htmlsTuple = Tuple.fromArray(htmls.toArray).asInstanceOf[HtmlTypes[T]]
      f(htmlsTuple)
    }

  def dispatchInput[T <: Tuple](
      elements: T,
      states: Iterator[Any],
      input: InputTypes[T]): (StateTypes[T], Cmd[F, Either[OutputTypes[T], MessageTypes[T]]]) = {
          input match {
            case Left(i) =>
              val (state, cmd) = elements.productElement(0).asInstanceOf[TyrianElement[F,?, ?, ?, ?]].update(states.next.asInstanceOf, Left(i.asInstanceOf))
              val cmdb = cmd.map {
                case Left(o) => Left(Left(o))
                case Right(m) => Right(Left(m))
              }
              (state, cmdb)
            case Right(ii) =>
              ???
          }
          ???
      }

  def dispatchMessage[T <: Tuple](
      elements: T,
      states: Iterator[Any],
      message: MessageTypes[T]): (StateTypes[T], Cmd[F, Either[OutputTypes[T], MessageTypes[T]]]) = {
        message match {
          case Left(m) =>
            val (state, cmd) = elements.productElement(0).asInstanceOf[TyrianElement[F,?, ?, ?, ?]].update(states.next.asInstanceOf, Right(m.asInstanceOf))
            val cmdb = cmd.map {
              case Left(o) => Left(Left(o))
              case Right(m) => Right(Left(m))
            }
            (state, cmdb)
          case Right(mm) =>
            ???
        }
        ???
      }
  
  def indexToEither[T <: Tuple, M](index: Int, value: M)(using ev: MessageTypes[T]): MessageTypes[T] = {
    // if (index == 0) Left(value)
    // else Right(indexToEitherHelper(index - 1, value))
    ???
  }

  def indexToEitherHelper[T](index: Int, value: T): T = {
    // if (index == 0) value
    // else Right(indexToEitherHelper(index - 1, value))
    ???
  }

  def indexOfEither[T <: Tuple](either: MessageTypes[T]): Int = {
    // either match {
    //   case Left(_) => 0
    //   case Right(m:MessageTypes[Tuple.Tail[T]]) => indexOfEitherHelper[Tuple.Tail[T], MessageTypes[Tuple.Tail[T]]](1, m)
    //   case _ => 0
    // }
    ???
  }
  
  def indexOfEitherHelper[T <: Tuple, M](i: Int, either: MessageTypes[T]): Int = {
    // either match {
    //   case Left(_) => i
    //   case Right(m:MessageTypes[Tuple.Tail[T]]) => indexOfEitherHelper[Tuple.Tail[T], M](1, m)
    //   case _ => i
    // }
    ???
  }
}
