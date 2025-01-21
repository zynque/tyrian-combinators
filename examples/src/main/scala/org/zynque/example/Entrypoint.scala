package com.fractaltreehouse.threetyrz

import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import org.zynque.example.elements.DemoAppComponent

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOComponentApp(DemoAppComponent)
