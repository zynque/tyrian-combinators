package org.zynque.tyriancombinators.extensions

import org.zynque.tyriancombinators.elements.*
import org.zynque.tyriancombinators.data.*
import tyrian.*

export TyrianElementExtensions.*


object TyrianElementExtensions {
  extension [F[_], I, O, M, S](element: TyrianElement[F, I, O, M, S]) {
    // todo: can we simplify the combineUI function to handle message wrapping automatically?
    def feedInto[O2, M2, S2](
        element2: TyrianElement[F, O, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] =
      new FedInto[F, I, O, M, S, O2, M2, S2](element, element2, combineUI)

    def feedInto[O2, M2, S2](
        element2: DataElement[F, O, O2, M2, S2]
    ): TyrianElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] =
      new FedIntoDataElement[F, I, O, M, S, O2, M2, S2](element, element2)

    def duplicate: TyrianElement[F, I, Either[O, O], M, S] =
      new Duplicated[F, I, O, M, S](element).withView(element.view)

    def mapOutput[O2](f: O => O2): TyrianElement[F, I, O2, M, S] =
      new OutputMapped[F, I, O, M, S, O2](element, f).withView(element.view)

    def contramapInput[I2](f: I2 => I): TyrianElement[F, I2, O, M, S] =
      new InputContramapped[F, I, I2, O, M, S](element, f).withView(element.view)

    def pairWith[I2, O2, M2, S2](
        element2: TyrianElement[F, I2, O2, M2, S2],
        combineUI: (Html[M], Html[M2]) => Html[Either[M, M2]]
    ): TyrianElement[F, Either[I, I2], Either[O, O2], Either[
      M,
      M2
    ], (S, S2)] =
      new PairedWith[F, I, O, M, S, I2, O2, M2, S2](
        element,
        element2
      ).withView((s, s2) => combineUI(element.view(s), element2.view(s2)))

    def propagateState: TyrianElement[F, I, S, M, S] =
      new StatePropagated[F, I, O, M, S](element).withView(element.view)
  }

  extension [F[_], I, OA, OB, M, S](element: TyrianElement[F, I, Either[OA, OB], M, S]) {
    def eitherAccumulated = element.feedInto(new EitherAccumulator[F, OA, OB])
  }
}
