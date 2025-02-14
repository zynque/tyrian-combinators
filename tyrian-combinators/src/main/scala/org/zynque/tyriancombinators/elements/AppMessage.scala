package org.zynque.tyriancombinators.elements

enum AppMessage[+I, +O, +M]:
  case Input(i: I)
  case Output(o: O)
  case InternalMessage(m: M)
  case None
