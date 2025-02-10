package org.zynque.example.elements

import tyrian.*
import tyrian.Html.*

enum ListMsg[+A]:
  case AddItem(item: A)
  case RemoveItem(index: Int)
  case UpdateItem(index: Int, newValue: A)
