package org.zynque.tyriancombinators.data

import tyrian.*

// Combines two elements into one, with the output of the new element being either the output of the first element or the output of the second element
// For example, multiple text boxes can be combined into a single element that emits the value of the text box that was most recently changed
class PairedWith[F[_], I, O, M, S, I2, O2, M2, S2](
    element: DataElement[F, I, O, M, S],
    element2: DataElement[F, I2, O2, M2, S2]
) extends DataElement[
      F,
      Either[I, I2],
      Either[O, O2],
      Either[M, M2],
      (S, S2)
    ] {
  def init: ((S, S2), Cmd[F, Either[M, M2]]) = {
    val (s1, c1) = element.init
    val (s2, c2) = element2.init
    val combined = c1.map(Left(_)) |+| c2.map(Right(_))
    ((s1, s2), combined)
  }
  def update(state: (S, S2), value: Either[Either[I, I2], Either[M, M2]]): (
      (S, S2),
      Cmd[F, Either[Either[O, O2], Either[M, M2]]]
  ) = {
    val (s, s2) = state
    value match {
      case Left(Left(i)) =>
        val (sb, cmd) = element.update(s, Left(i))
        val cmdb = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(Left(m))
        }
        ((sb, s2), cmdb)
      case Left(Right(i2)) =>
        val (s2b, cmd2) = element2.update(s2, Left(i2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(Right(o2))
          case Right(m2) => Right(Right(m2))
        }
        ((s, s2b), cmd2b)
      case Right(Left(o)) =>
        val (sb, cmd) = element.update(s, Right(o))
        val cmdb = cmd.map {
          case Left(o)  => Left(Left(o))
          case Right(m) => Right(Left(m))
        }
        ((sb, s2), cmdb)
      case Right(Right(o2)) =>
        val (s2b, cmd2) = element2.update(s2, Right(o2))
        val cmd2b = cmd2.map {
          case Left(o2)  => Left(Right(o2))
          case Right(m2) => Right(Right(m2))
        }
        ((s, s2b), cmd2b)
    }
  }
}
