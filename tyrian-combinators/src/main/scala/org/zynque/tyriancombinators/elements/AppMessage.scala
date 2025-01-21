package org.zynque.tyriancombinators.elements

enum AppMessage[+I, +O, +M] {
  case Input(i: I)           extends AppMessage[I, Nothing, Nothing]
  case Output(o: O)          extends AppMessage[Nothing, O, Nothing]
  case InternalMessage(m: M) extends AppMessage[Nothing, Nothing, M]
  case None extends AppMessage[Nothing, Nothing, Nothing]
}
