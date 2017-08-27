package aug.nodeka.loop

import aug.nodeka._
import aug.script.framework.reload.Reload

sealed trait LoopState

case object StoppedLS extends LoopState
case object RunningLS extends LoopState
case object TrainingLS extends LoopState
case object WalkingBackLS extends LoopState
case object RecallingLS extends LoopState

object Loop extends Initable {

  @Reload private var state: LoopState = StoppedLS
  @Reload private var path: Path = _

  private def next(): Unit = {
    state match {
      case StoppedLS =>
      case RunningLS =>
      case TrainingLS =>
      case WalkingBackLS =>
      case RecallingLS =>
    }
  }

  override def init(client: NodekaClient): Unit = {
    Trigger.add("^For a list of all help topics, type: display help$", {
      if (state == WalkingBackLS) {

      }
    })
  }
}
