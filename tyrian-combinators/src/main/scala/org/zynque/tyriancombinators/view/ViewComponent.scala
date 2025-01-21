package org.zynque.tyriancombinators.view

import tyrian.*

trait ViewComponent[M, S]:
  def view(state: S): Html[M]
