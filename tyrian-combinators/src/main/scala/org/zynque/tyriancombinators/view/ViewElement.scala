package org.zynque.tyriancombinators.view

import tyrian.*

trait ViewElement[M, S]:
  def view(state: S): Html[M]
