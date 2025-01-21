package org.zynque.tyriancombinators.data

import tyrian.*

// A Tyrian Element that handles messages and state but has no view
// F: Effect Type (Cats Effect or ZIO Task)
// I: Input, O: Output, S: State/Model, M: Message
trait DataElement[F[_], I, O, M, S]:
  def init: (S, Cmd[F, M])
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]])
