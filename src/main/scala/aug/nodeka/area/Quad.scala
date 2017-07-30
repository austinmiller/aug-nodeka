package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Quad extends Area {
  override val name: String = "The Quad"
  override val keyword: String = "quad"
  override val mobTriggers: List[MobTrigger] = List.empty
  override val paths: Map[String, List[String]] = Map("default" -> List("n", "e", "s", "w"))
}
