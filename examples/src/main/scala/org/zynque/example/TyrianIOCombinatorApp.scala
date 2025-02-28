package org.zynque.example

import cats.effect.*
import cats.effect.kernel.Async
import org.zynque.tyriancombinators.elements.*
import tyrian.*

class TyrianIOCombinatorApp[I, O, M, S](
    element: TyrianElement[cats.effect.IO, I, O, M, S]
)(using Async[IO])
    extends TyrianIOApp[AppMessage[I, O, M], S]
    with TyrianCombinatorApp[IO, I, O, M, S](element)
