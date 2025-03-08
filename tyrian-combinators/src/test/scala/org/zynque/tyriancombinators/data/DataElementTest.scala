package org.zynque.tyriancombinators.data

import tyrian.*

class DataElementTest extends munit.FunSuite {

  test("fromFunction: init") {
    val element = DataElement.fromFunction((i: Int) => i + 1)
    val result = element.init
    assert(result == ((), Cmd.None))
  }

  test("fromFunction: update") {
    val element = DataElement.fromFunction((i: Int) => i + 1)
    val result = element.update((), Left(1))
    assert(result == ((), Cmd.emit(Left(2))))
  }

  test("stateTransformer: init") {
    val element = DataElement.stateTransformer(0)((s: Int, i: Int) => s + i)
    val result = element.init
    assert(result == (0, Cmd.None))
  }

  test("stateTransformer: update") {
    val element = DataElement.stateTransformer(0)((s: Int, i: Int) => s + i)
    val result = element.update(3, Left(2))
    assert(result == (5, Cmd.emit(Left(5))))
  }

}
