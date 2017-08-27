package aug.nodeka.loop

import aug.nodeka.Profile

case class Path(steps: List[String], step: Int = -1) {

  def next(): Path = this.copy(step = step + 1)

  def takeStep(): Unit = {
    if (step < steps.length) {
      Profile.send(steps(step))
    }
  }
}
