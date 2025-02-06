package com.fractaltreehouse.threetyrz

import zio.interop.catz.*
import scala.scalajs.js.annotation.*
import org.zynque.example.elements.DemoApp
import zio.Task

@JSExportTopLevel("ThreeTyrz")
object ThreeTyrz extends TyrianZIOCombinatorApp(DemoApp[Task])
