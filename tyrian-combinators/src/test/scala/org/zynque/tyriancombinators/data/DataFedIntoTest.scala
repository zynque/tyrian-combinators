package org.zynque.tyriancombinators.data

import org.zynque.tyriancombinators.data.DataOnlyExtensions.*
import tyrian.*

class DataFedIntoTest extends munit.FunSuite {

  test("data fed into: init") {
    val element1 = DataElement.stateTransformer(0)((s: Int, i: Int) => s + i)
    val element2 = DataElement.fromFunction((i: Int) => i + 1)

    val element3 = element1.feedInto(element2)

    val (s, cmd) = element3.init
    val expectedCommand = (Cmd.None |+| Cmd.None).asInstanceOf[Cmd[Nothing, PairMsg[Int, Nothing, Nothing]]]
    assertEquals(s, (0, ()))
    assertEquals(cmd, expectedCommand)
  }

  test("data fed into: update") {
    val element1 = DataElement.stateTransformer(0)((s: Int, i: Int) => s + i)
    val element2 = DataElement.fromFunction((i: Int) => i + 1)

    val element3 = element1.feedInto(element2)

    val (s, cmd) = element3.update((1, ()), Left(2))
    val expectedCommand = Cmd.emit(Right(PairMsg.Out(3)))
    assertEquals(s, (3, ()))
    assertEquals(cmd, expectedCommand)
    val (s2, cmd2) = element3.update((3, ()), Right(PairMsg.Out(3)))
    val expectedCommand2 = Cmd.emit(Left(4))
    assertEquals(s2, (3, ()))
    assertEquals(cmd2, expectedCommand2)  
  }
}
