package org.zynque.tyriancombinators.data

import tyrian.*

// An element that feeds the output of one element into the input of another, while maintaining internal state for each
// For example, a button that emits click events can be fed into a logical counter element, and the result can be fed into a display element
class DataFedInto[F[_], I, O, M, S, O2, M2, S2](
    element: DataElement[F, I, O, M, S],
    element2: DataElement[F, O, O2, M2, S2]
) extends DataElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] {
  def init: ((S, S2), Cmd[F, PairMsg[O, M, M2]]) = {
    val (s1, c1) = element.init
    val (s2, c2) = element2.init
    val leftC1   = c1.map(PairMsg.Msg1(_))
    val rightC2  = c2.map(PairMsg.Msg2(_))
    val combined = leftC1 |+| rightC2
    ((s1, s2), combined)
  }
  def update(
      state: (S, S2),
      value: Either[I, PairMsg[O, M, M2]]
  ): ((S, S2), Cmd[F, Either[O2, PairMsg[O, M, M2]]]) = {
    val (s, s2) = state
    value match {
      case Left(i) =>
        val (sb, cmd) = element.update(s, Left(i))
        val cmdb = cmd.map {
          case Left(o)  => Right(PairMsg.Out(o))
          case Right(m) => Right(PairMsg.Msg1(m))
        }
        ((sb, s2), cmdb)
      case Right(PairMsg.Out(o)) =>
        val (s2b, cmd2) = element2.update(s2, Left(o))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(o2)
          case Right(m2) => Right(PairMsg.Msg2(m2))
        }
        ((s, s2b), cmd2b)
      case Right(PairMsg.Msg1(m)) =>
        val (sb, cmd) = element.update(s, Right(m))
        val cmdb = cmd.map {
          case Left(o)  => Right(PairMsg.Out(o))
          case Right(m) => Right(PairMsg.Msg1(m))
        }
        ((sb, s2), cmdb)
      case Right(PairMsg.Msg2(m2)) =>
        val (s2b, cmd2) = element2.update(s2, Right(m2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(o2)
          case Right(m2) => Right(PairMsg.Msg2(m2))
        }
        ((s, s2b), cmd2b)
    }
  }
}
