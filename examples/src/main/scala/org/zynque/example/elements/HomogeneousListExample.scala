package org.zynque.example.elements

import org.zynque.tyriancombinators.extensions.*
import tyrian.*
import tyrian.Html.*

case class TodoListItem(title: String, completed: Boolean)

def homogeneousListExample[F[_]] = {
  // https://www.w3schools.com/howto/howto_js_todolist.asp

  val titleInput    = TextInput[F]("Title...")
  val addItemButton = Button[F]("Add")

  def addItemMessage(title: String) =
    ListMsg.AddItem(TodoListItem(title, false))

  val header =
    titleInput
      .pairWith(addItemButton) { case (input, button) =>
        div(
          h2("My To Do List"),
          input,
          button
        )
      }
      .accumulateAndTrigger
      .mapOutput(addItemMessage)

  def listView(items: List[TodoListItem]): Html[ListMsg[TodoListItem]] = {
    def itemLabel(item: TodoListItem) =
      if !item.completed then div(item.title)
      else div(style(CSS.`text-decoration`("line-through")))(item.title)
    def removeButton(index: Int) =
      button(onClick(ListMsg.RemoveItem(index)))("Remove")
    def completeButton(index: Int, item: TodoListItem) =
      button(onClick(ListMsg.UpdateItem(index, item.copy(completed = true))))(
        "Complete"
      )

    val listItems = items.zipWithIndex.map { case (item, index) =>
      li(
        itemLabel(item),
        removeButton(index),
        completeButton(index, item)
      )
    }

    ul(listItems)
  }

  val initialList = List("Buy milk", "Walk the dog").map(TodoListItem(_, false))

  val todoList = ListElement[F, TodoListItem](initialList, listView)

  val combined = header.feedInto(todoList, div(_, _))

  combined
}
