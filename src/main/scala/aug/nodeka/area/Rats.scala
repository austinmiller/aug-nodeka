package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Rats extends Area {
  override val name: String = "Aragnarack Mines"
  override val keyword: String = "rats"

  val defaultPath: List[String] = {
    val path =
      "open mine\nn,n,open door\ne,s,e,e,n,w,e,n,w,w,s,w,n,s,w,w,w,w,n,open crack\ne,e,e,w,w,w,n,e,e,e,e,n," +
        "open door\ne,s,e,e,n,w,e,n,w,w,s,w,n,s,w,w,w,w,n,open crack\ne,e,e,w,w,w,n,e,e,e,e,e,e,e,n,w,w,w,n," +
        "s,w,w,w,w,n,open crack\ne,e,e,w,w,w,n,e,e,e,e,open door\ne,s,e,e,n,w,e,n,w,w,s,w,n,n,s,w,w,w,w,n," +
        "open crack\ne,e,e,w,w,w,n,e,e,e,e,open door\ne,s,e,e,n,w,e,n,w,w,s,w,n,w,open door\nn,e,n,n,w,s,n," +
        "w,s,s,e,s,w,w,w,open door\nn,w,n,n,e,s,n,e,open door\nn,n,n,e,s,n,e,s,s,w,w,s,s,s,w,s,w,w,n,n,n," +
        "open door\nn,e,n,n,w,s,n,w,s,s,e,s,s,s,s,w,w,open door\nn,e,n,n,w,s,n,w,open door\nn,n,n,w,s,n,w," +
        "s,s,e,e,s,s,s,e,s,w,w,w,open door\nn,e,n,n,w,s,n,w,s,s,e,s,w,s,s,n,open door\nw,n,w,w,s,e,w,s,e,e,n," +
        "e,e,e,e,e,s,open crack\nw,w,w,e,e,e,s,w,w,w,w,s,s,n,open door\nw,n,w,w,s,e,w,s,e,e,n,e,e,e,e,e,s," +
        "open crack\nw,w,w,e,e,e,s,w,w,w,w,w,w,w,s,e,e,e,s,n,e,e,e,e,s,open crack\nw,w,w,e,e,e,s,w,w,w,w," +
        "open door\nw,n,w,w,s,e,w,s,e,e,n,e,s,s,n,e,e,e,e,s,open crack\nw,w,w,e,e,e,s,w,w,w,w,open door\nw," +
        "n,w,w,s,e,e,w,w,s,e,e,n,e,s,e,e,e,e,e,e,e,e,e,e,e,e,n,n,n,n,n,n,n,n,n,n,n,n,n,w,w,w,w,w,open crack\ns," +
        "s,s,s,s,s,s,s,s,s,s,s,w,w,n,n,n,n,n,n,n,n,n,n,n,open daylight\nd,n,n,n,n,n,w,w,w,w,w,s,s,open e.door" +
        "\nopen w.door\nw,n,w,w,s,e,w,s,e,e,n,e,e,n,e,e,s,w,e,s,w,w,n,w,n,n,e,e,e,e,e,e,e,e,e,e,s,s,open e.door" +
        "\nopen w.door\nw,n,w,w,s,e,w,s,e,e,n,e,e,n,e,e,s,w,e,s,w,w,n,w,n,n,w,w,w,w,w,n,n,e,e,e,e,e,e,s,s,e,e," +
        "e,n,n,n,w,w,open cell\ns,s,e,n,w,n,e,e,s,s,s,w,w,w,n,n,n,n,e,n,w,n,n,w,w,w,n,n,n,e,open door\ne,s,e,n," +
        "n,w,s,w,w,n,w,w,s,w,s,n,w,n,w,w,s,w,open door\nw,n,w,s,s,e,n,e,e,s,s,s,w,w,w,s,s,w,s,e,s,s,s,s,w,w,w,n," +
        "n,n,e,e,open cell\ns,s,w,n,e,n,w,w,s,s,s,e,e,e,n,n,e,e,e,e,e,e,n,n,n,n,n,w,n,n,w,w,e,e,s,s,open door\ns," +
        "s,s,w,n,w,s,w,n,w,s,n,n,e,e,e,e,n,e,e,n,n,e,e,w,w,s,s,open door\ns,s,s,e,n,e,s,e,n,e,s,n,n,w,w,w,w,n,w," +
        "open glow\nn,open peril\nd,n,n,w,open door\nw,w,w,s,s,e,e,n,w,e,n,e,n,n,w,n,n,open cell\nw,w,s,e,n,e," +
        "s,s,e,n,n,e,w,n,w,w,w,w,s,s,s,open cell\ne,e,s,w,n,w,n,n,n,n,n,open cell\ne,e,n,w,s,w,n,n,n,open door\ne," +
        "s,e,n,n,w,s,w,n,n,n,open door\ne,s,e,n,n,w,s,w,n,n,e,e,e,e,e,open door\ns,e,s,s,w,n,s,w,n,n,s," +
        "open door\nw,s,s,s,s,s,s,s,s,e,s,s,s,s,s,e,e,open door\ne,e,e,s,w,e,s,w,w,n,n,w,n,n,e,n,n,open cell\ne," +
        "e,s,w,n,w,s,s,w,n,n,n,e,e,e,e,s,s,s,open cell\nw,s,w,n,e,e,n,n,n,n,n,open cell\nw,w,n,e,s,e,n,n,n," +
        "open door\nw,n,w,s,s,e,n,e,n,n,n,open door\nw,n,w,s,s,e,n,e,n,n,w,w,w,w,w,open door\ns,e,s,open door\ne," +
        "s,s,s,s,s,s,s,n,open door\nw,w,w,n,e,e,s,w,s,n,n,n"
    path.split(",").toList
  }

  override val mobTriggers: List[MobTrigger] = List.empty
  override val paths: Map[String, List[String]] = Map("default" -> defaultPath)
}
