package org.zynque.tyriancombinators.data

enum PairMsg[+O, +M, +M2]:
  case Out(o: O)
  case Msg1(m: M)
  case Msg2(m: M2)
