package org.zynque.example.elements

import org.zynque.tyriancombinators.extensions.*
import tyrian.*
import tyrian.Html.*
import org.zynque.tyriancombinators.elements.TyrianElement
// import org.zynque.tyriancombinators.view.*

def homogeneousListExample[F[_]] = {
  // https://www.w3schools.com/howto/howto_js_todolist.asp

  val titleInput    = TextInput.Element[F]("Title...")
  val addItemButton = ButtonElement.Element[F]("Add")

  val header = titleInput
    .pairWith(addItemButton) { case (input, button) =>
      div(
        h2("My To Do List"),
        input,
        button
      )
    }
    .accumulateAndTrigger
    .mapOutput {
      title => TodoListMsg.AddItem(title)
    }

  val todoList = new TodoListElement[F](List("Buy milk", "Walk the dog"))

  val combined = header.feedInto(todoList, div(_, _))

  combined
}

enum TodoListMsg:
  case AddItem(title: String)
  case RemoveItem(index: Int)
  case CompleteItem(index: Int)

case class TodoListItem(title: String, completed: Boolean)

// todo: consider optimizations for long lists - use vectors?
class TodoListElement[F[_]](
    initialItems: List[String]
) extends TyrianElement[F, TodoListMsg, Nothing, TodoListMsg, List[
      TodoListItem
    ]] {
  def init = (initialItems.map(TodoListItem(_, false)), Cmd.None)
  def update(
      state: List[TodoListItem],
      value: Either[TodoListMsg, TodoListMsg]
  ): (List[TodoListItem], Cmd[F, Either[Nothing, TodoListMsg]]) =
    value.merge match {
      case TodoListMsg.AddItem(title) =>
        val newItem = TodoListItem(title, false)
        (newItem :: state, Cmd.None)
      case TodoListMsg.RemoveItem(index) =>
        val newState = state.take(index) ++ state.drop(index + 1)
        (newState, Cmd.None)
      case TodoListMsg.CompleteItem(index) =>
        val updatedState =
          state.updated(index, state(index).copy(completed = true))
        (updatedState, Cmd.None)
    }
  def view(state: List[TodoListItem]): Html[TodoListMsg] = {
    val items = state.zipWithIndex.map { case (item, index) =>
      li(
        if !item.completed then div(item.title) else div(style(CSS.`text-decoration`("line-through")))(item.title),
        button(onClick(TodoListMsg.RemoveItem(index)))("Remove"),
        button(onClick(TodoListMsg.CompleteItem(index)))("Complete")
      )
    }
    ul(items)
  }
}

// object ListElement {
//   def apply[F[_]](
//       initialItems: List[String]
//   ): TyrianElement[F, Any, Nothing, TodoListMsg, Unit] = {
//     def buttonForRemovingItem(index: Int) = ButtonElement
//       .Element[F]("Remove")
//       .mapOutput(_ => TodoListMsg.RemoveItem(index))
//     def itemLabel(item: String) = p(item)
//     def itemElement(item: String, index: Int) =
//       buttonForRemovingItem(index).mapView { (_, button) =>
//         li(itemLabel(item), buttonForRemovingItem(index).view(()))
//       }
//     val itemsList     = initialItems.zipWithIndex.map(itemElement)
//     val itemsListView = listElement(itemsList)
//     def itemDataElement(item: String, index: Int) = buttonForRemovingItem(index)
//     val itemsDataElement = initialItems.zipWithIndex.map(itemDataElement)

//     viewElement.asElement
//   }
// }

def externalStreamExample = {
  // we want to be able to subscribe to an external stream of data and display it
}
