package org.zynque.tyriancombinators.data

import org.zynque.tyriancombinators.elements.*
import tyrian.*
export DataElementExtensions.*

object DataElementExtensions {
  extension [F[_], I, O, M, S](element: DataElement[F, I, O, M, S]) {
    def withView(
        view: S => Html[M]
    ): TyrianElement[F, I, O, M, S] =
      new DataElementWithView(element, view)
  }
}

// these names conflict with TyrianElementExtensions
// so we require them to be imported explicitly
object DataOnlyExtensions {
  extension [F[_], I, O, M, S](element: DataElement[F, I, O, M, S]) {
    def feedInto[O2, M2, S2](
        element2: DataElement[F, O, O2, M2, S2]
    ): DataElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] =
      new DataFedInto(element, element2)

    def mapOutput[O2](f: O => O2): DataElement[F, I, O2, M, S] =
      new OutputMapped(element, f)

    def pairWith[I2, O2, M2, S2](
        element2: DataElement[F, I2, O2, M2, S2]
    ): DataElement[F, Either[I, I2], Either[O, O2], Either[M, M2], (S, S2)] =
      new PairedWith(element, element2)

    def propagateState: DataElement[F, I, S, M, S] =
      new StatePropagated(element)
  }
}
