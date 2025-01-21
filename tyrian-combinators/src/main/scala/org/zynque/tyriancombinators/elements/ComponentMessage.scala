package org.zynque.tyriancombinators.elements

enum ComponentMessage[+I, +O, +M] {
  case Input(i: I)           extends ComponentMessage[I, Nothing, Nothing]
  case Output(o: O)          extends ComponentMessage[Nothing, O, Nothing]
  case InternalMessage(m: M) extends ComponentMessage[Nothing, Nothing, M]
  case None extends ComponentMessage[Nothing, Nothing, Nothing]
}
