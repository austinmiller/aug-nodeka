package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Things extends Area {
  override val name: String = "Realm of the Ancients"
  override val keyword: String = "things"
  override val mobTriggers: List[MobTrigger] = List(
    MobTrigger("^A strange pile of bones lurches forward\\.$", "thing"),
//    MobTrigger("^  \\[ ([1-9][0-9]{0,}) \\] a suit of spiked leather armor$", ""),
    MobTrigger("^Hiding in the shadows, \\[ ([1-9][0-9]{0,}) \\] yellow things wait in ambush\\.$", "thing"),
    MobTrigger("^Several blue things, at least \\[ ([1-9][0-9]{0,}) \\] , are chanting mystical words\\.$", "thing"),
    MobTrigger("^Trading different body parts, \\[ ([1-9][0-9]{0,}) \\] different things wait for orders\\.$", "thing"),
    MobTrigger("^A lanky blue thing sketches runes in the air\\.$", "thing"),
    MobTrigger("^A mass of \\[ ([1-9][0-9]{0,}) \\] red things fall over each other\\.$", "thing"),
    MobTrigger("^Foaming at the mouth, a green thing stumbles about\\.$", "thing"),
    MobTrigger("^A secretive yellow thing tries to avoid being seen\\.$", "thing"),
    MobTrigger("^A small red thing scurries across the ground\\.$", "thing"),
    MobTrigger("^Wandering aimlessly, \\[ ([1-9][0-9]{0,}) \\] green things look for something to break\\.$", "thing")
  )

  val defaultPath : List[String] = {
    val path = "e,e,n,n,e,e,e,s,e,e,e,e,n,e,s,e,s,w,s,w,n,w,w,n,w,n,w,w,n,n,n,e,n,n,e,n,n,e,n,n,n,e,n,s,s,s,s,n," +
      "e,e,e,w,n,w,n,e,n,e,n,n,e,s,s,e,n,n,n,n,n,n,n,n,n,n,n,w,s,s,s,s,s,w,w,n,e,n,w,n,e,n,w,n,e,e,e," +
      "e,s,s,s,s,s,s,s,s,s,e,n,n,n,n,n,n,n,n,n,s,s,s,e,n,n,n,s,s,s,s,s,s,s,s,s,e,n,n,n,n,n,n,n,n,n,e," +
      "s,s,s,s,s,s,e,n,n,n,n,n,n,s,e,s,s,s,s,s,e,n,n,n,n,n,s,e,s,e,s,w,s,e,s,w,w,w,w,w,s,e,e,e,e,e,s," +
      "w,w,w,w,w,w,s,e,e,e,e,e,e,s,w,w,w,w,w,w,s,e,e,e,e,e,e,s,w,w,w,w,w,w,s,e,e,e,e,e,e,s,w,w,w,w,w," +
      "w,n,n,n,n,n,w,w,w,w,w,w,s,s,s,w,w,w,w,s,s,s,s,w,s,s,w,s,s,w,s,w,w,n,n,w,n,n,w,n,n,n,n,e,n,n,w," +
      "w,w,s,e,e,s,w,w,w,e,e,s,s,e,s,s,e,s,s,e,s,s,s,s,s,w,w"
    path.split(",").toList
  }

  override val paths: Map[String, List[String]] = Map("default" -> defaultPath)
}
