package org.zynque.tyriancombinators.elements

import tyrian.*

trait TyrianCombinatorApp[F[_], I, O, M, S](
    element: TyrianElement[F, I, O, M, S]
) extends TyrianApp[F, AppMessage[I, O, M], S] {
  // todo: allow subscriptions and feed into input channel of wrapped element
  def subscriptions(model: S): Sub[F, AppMessage[I, O, M]] = Sub.None
  // todo
  def router: Location => AppMessage[I, O, M] =
    Routing.none(AppMessage.None)
  def init(
      flags: Map[String, String]
  ): (S, Cmd[F, AppMessage[I, O, M]]) = {
    val (model, cmd) = element.init
    val cmd2         = cmd.map(AppMessage.InternalMessage(_))
    (model, cmd2)
  }
  def update(
      model: S
  ): AppMessage[I, O, M] => (S, Cmd[F, AppMessage[I, O, M]]) = {
    case AppMessage.InternalMessage(msg) =>
      val (model2, cmd) = element.update(model, Right(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        appMessage = outOrMsg match {
          case Left(out)  => AppMessage.Output(out)
          case Right(msg) => AppMessage.InternalMessage(msg)
        }
      } yield appMessage
      (model2, cmd2)
    case AppMessage.Input(msg) =>
      val (model2, cmd) = element.update(model, Left(msg))
      val cmd2 = for {
        outOrMsg <- cmd
        appMessage = outOrMsg match {
          case Left(out)  => AppMessage.Output(out)
          case Right(msg) => AppMessage.InternalMessage(msg)
        }
      } yield appMessage
      (model2, cmd2)
    case AppMessage.Output(out) =>
      (model, Cmd.None)
    case _ =>
      (model, Cmd.None)

  }

  def view(model: S): Html[AppMessage[I, O, M]] =
    element.view(model).map(AppMessage.InternalMessage(_))
}
