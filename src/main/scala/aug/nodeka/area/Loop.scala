package aug.nodeka.area

import aug.nodeka.Area

object Loop extends Area {
  override val name = "Loop"
  override val keyword = "loop"
  override val mobTriggers = List.empty

  val defaultPath = {
    val list = List.newBuilder[String]

    for (i <- 0 to 500) {
      list += "n"
    }

    list.result()
  }

  override val paths = Map("default" -> defaultPath)
}
