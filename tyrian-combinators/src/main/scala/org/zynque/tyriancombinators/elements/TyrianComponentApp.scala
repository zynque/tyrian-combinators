package org.zynque.tyriancombinators.elements

import tyrian.*

trait TyrianComponentApp[F[_], I, O, M, S](
    component: TyrianComponent[F, I, O, M, S]
) extends TyrianApp[F, ComponentMessage[I, O, M], S] {
  // todo: allow subscriptions and feed into input channel of wrapped component
  def subscriptions(model: S): Sub[F, ComponentMessage[I, O, M]] = Sub.None
  // todo
  def router: Location => ComponentMessage[I, O, M] =
    Routing.none(ComponentMessage.None)
  def init(
      flags: Map[String, String]
  ): (S, Cmd[F, ComponentMessage[I, O, M]]) = {
    val (model, cmd) = component.init
    val cmd2         = cmd.map(ComponentMessage.InternalMessage(_))
    (model, cmd2)
  }
  def update(
      model: S
  ): ComponentMessage[I, O, M] => (S, Cmd[F, ComponentMessage[I, O, M]]) = {
    case ComponentMessage.InternalMessage(msg) =>
      val (model2, cmd) = component.update(model, Right(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        componentMessage = outOrMsg match {
          case Left(out)  => ComponentMessage.Output(out)
          case Right(msg) => ComponentMessage.InternalMessage(msg)
        }
      } yield componentMessage
      (model2, cmd2)
    case ComponentMessage.Input(msg) =>
      val (model2, cmd) = component.update(model, Left(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        componentMessage = outOrMsg match {
          case Left(out)  => ComponentMessage.Output(out)
          case Right(msg) => ComponentMessage.InternalMessage(msg)
        }
      } yield componentMessage
      (model2, cmd2)
    case ComponentMessage.Output(out) =>
      (model, Cmd.None)
    case _ =>
      (model, Cmd.None)

  }

  def view(model: S): Html[ComponentMessage[I, O, M]] =
    component.view(model).map(ComponentMessage.InternalMessage(_))
}
