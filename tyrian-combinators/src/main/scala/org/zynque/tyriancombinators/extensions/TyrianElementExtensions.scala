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
        combineUI: (
            Html[PairMsg[O, M, M2]],
            Html[PairMsg[O, M, M2]]
        ) => Html[PairMsg[O, M, M2]]
    ): TyrianElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] =
      FedInto[F, I, O, M, S, O2, M2, S2](element, element2, combineUI)

    def feedInto[O2, M2, S2](
        element2: DataElement[F, O, O2, M2, S2]
    ): TyrianElement[F, I, O2, PairMsg[O, M, M2], (S, S2)] =
      FedIntoDataElement[F, I, O, M, S, O2, M2, S2](element, element2)

    def fork: TyrianElement[F, I, Either[O, O], M, S] =
      Fork[F, I, O, M, S](element).withView(element.view)

    def mapOutput[O2](f: O => O2): TyrianElement[F, I, O2, M, S] =
      OutputMapped[F, I, O, M, S, O2](element, f).withView(element.view)

    def contramapInput[I2](f: I2 => I): TyrianElement[F, I2, O, M, S] =
      InputContramapped[F, I, I2, O, M, S](element, f)
        .withView(element.view)

    def pairWith[I2, O2, M2, S2](element2: TyrianElement[F, I2, O2, M2, S2])(
        combineUI: (
            Html[Either[M, M2]],
            Html[Either[M, M2]]
        ) => Html[Either[M, M2]]
    ): TyrianElement[F, Either[I, I2], Either[O, O2], Either[
      M,
      M2
    ], (S, S2)] =
      PairedWith[F, I, O, M, S, I2, O2, M2, S2](
        element,
        element2
      ).withView((s, s2) =>
        combineUI(element.view(s).map(Left(_)), element2.view(s2).map(Right(_)))
      )

    def propagateState: TyrianElement[F, I, S, M, S] =
      StatePropagated[F, I, O, M, S](element).withView(element.view)

    def mapView(f: (S, Html[M]) => Html[M]): TyrianElement[F, I, O, M, S] =
      MappedViewElement(element, f)
  }
}

extension [F[_], I, OA, OB, M, S](
    element: TyrianElement[F, I, Either[OA, OB], M, S]
) {
  def eitherAccumulated = element.feedInto(new EitherAccumulator[F, OA, OB])
  def accumulateAndTrigger = element.feedInto(new AccumulateAndTrigger[F, OA, OB])
}

extension [F[_], I, O, M, S](element: TyrianElement[F, I, Option[O], M, S]) {
  def collectSome: TyrianElement[F, I, O, PairMsg[Option[O], M, Nothing], (S, Unit)] =
    element.feedInto(SomeCollector())
}

class MappedViewElement[F[_], I, O, M, S](
    element: TyrianElement[F, I, O, M, S],
    f: (S, Html[M]) => Html[M]
) extends TyrianElement[F, I, O, M, S] {
  def init: (S, Cmd[F, M]) = element.init
  def update(state: S, value: Either[I, M]): (S, Cmd[F, Either[O, M]]) =
    element.update(state, value)
  def view(state: S): Html[M] = f(state, element.view(state))
}

class SomeCollector[F[_], I] extends DataElement[F, Option[I], I, Nothing, Unit] {
  def init: (Unit, Cmd[F, Nothing]) = ((), Cmd.None)
  def update(state: Unit, value: Either[Option[I], Nothing]): (Unit, Cmd[F, Either[I, Nothing]]) =
    value match {
      case Left(Some(i)) => ((), Cmd.emit(Left(i)))
      case _             => ((), Cmd.None)
    }
  }
