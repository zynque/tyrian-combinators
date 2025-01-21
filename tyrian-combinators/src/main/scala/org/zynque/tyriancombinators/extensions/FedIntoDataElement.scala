package org.zynque.tyriancombinators.extensions

import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.data.*
import tyrian.*

class FedIntoDataElement[F[_], I, O, M, S, O2, M2, S2](
    element: TyrianElement[F, I, O, M, S],
    element2: DataElement[F, O, O2, M2, S2]
) extends DataFedInto[F, I, O, M, S, O2, M2, S2](
      element,
      element2
    )
    with TyrianElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] {
  def view(state: (S, S2)): Html[PairMsg[O, M, M2]] = {
    val (s, s2) = state
    val h       = element.view(s)
    h.map(m => PairMsg.Msg1(m))
  }
}
