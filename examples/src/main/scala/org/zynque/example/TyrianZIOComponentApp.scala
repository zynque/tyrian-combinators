package com.fractaltreehouse.threetyrz

import zio.*
import cats.effect.kernel.Async
import org.zynque.tyriancombinators.elements.*
import tyrian.*

class TyrianZIOComponentApp[I, O, M, S](
    component: TyrianComponent[Task, I, O, M, S]
)(using Async[Task])
    extends TyrianZIOApp[ComponentMessage[I, O, M], S] with TyrianComponentApp[Task, I, O, M, S](component) {
}
