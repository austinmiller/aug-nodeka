package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Telg extends Area {
  override val name = "Telgoran Ruins"
  override val keyword = "telg"
  override val mobTriggers: List[MobTrigger] = List(
    MobTrigger("^As ancient as time itself, this spirit stares at your soul\\.$", "spirit"),
    MobTrigger("^This spirit was once a slave to the god of Telgoran\\.$", "spirit"),
    MobTrigger("^Somehow, this female spirit still holds the beauty of her past life\\.$", "spirit"),
    MobTrigger("^A phantom wears the ragged remains of its worship robe\\.$", "spirit"),
    MobTrigger("^A following of \\[ ([1-9][0-9]{0,}) \\] spirits are cursed to walk this world forever\\.$", "spirit"),
    MobTrigger("^A spirit, in the form of a regal priest, no longer leads others to his god\\.$", "spirit"),
    MobTrigger("^A gathering of \\[ ([1-9][0-9]{0,}) \\] servants still desire to serve Telgoran\\.$", "spirit"),
    MobTrigger("^A group of \\[ ([1-9][0-9]{0,}) \\] priests traverse this plane, and others above\\.$", "spirit"),
    MobTrigger("^\\[ ([1-9][0-9]{0,}) \\] female spirits smile at you and whisper to each other\\.$", "spirit"),
    MobTrigger("^A crowd of \\[ ([1-9][0-9]{0,}) \\] ancient spirits take you in with a glance\\.$", "spirit"),
  )

  private val defaultPath: List[String] = {
    val path =
      "e,e,n,w,w,n,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,s,w,w,n,e,n,n,n,w,s,s,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,s,w,w,n,e,n,w,n," +
        "n,e,s,s,e,e,e,e,e,e,e,e,e,e,e,e,e,e,e,s,s,e,n,n,n,w,n,e,e,s,s,s,s,e,e,n,w,n,n,n,e,s,s,e,e,s,s,e,n,n,n,w,n,e," +
        "e,s,s,s,s,e,e,n,w,n,n,n,e,s,s,s,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,e,e,n,w,w,n,e,e,n,w,w,n,w,w,s,s,w,n,n,n,e,n," +
        "w,w,s,s,s,w,w,s,e,e,n,n,w,n,n,w,s,s,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,s,w,w,n,e,n,w,n,n,e,s,s,e,e,e,e,e,e,e,e,e," +
        "e,e,e,e,e,e,s,s,e,n,n,n,w,n,e,e,s,s,s,s,e,e,n,w,n,n,n,e,s,s,e,e,e,s,s,w,n,n,n,n,e,s,n,e,s,s,s,s,e,e,n,n,n,n," +
        "w,s,s,s,n,w,w,w,w,w,w,w,w,w,w,w,n,w,w,e,n,n,w,w,n,e,e,e,s,e,n,n,w,w,w,w,n,n,e,s,e,e,e,n,w,w,n,n,w,w,n,e,e,e," +
        "s,e,n,n,w,w,w,w,n,n,e,s,e,n,e,e,s,w,w,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,w,u,e,e,n,w,w,n,w,w,s,s,w,n,n,n," +
        "e,n,w,w,s,s,s,s,w,w,n,e,n,n,n,w,s,s,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,s,w,w,n,e,n,w,n,n,e,s,s,e,e,e,e,e,e,e,e,e," +
        "e,e,e,e,e,e,s,s,e,n,n,n,w,n,e,e,s,s,s,s,e,e,n,w,n,n,n,e,s,s,e,e,s,s,e,n,n,n,w,n,e,e,s,s,s,s,e,e,n,w,n,n,n,e," +
        "s,s,s,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,e,e,n,w,w,n,e,e,n,w,w,n,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,w,w,s,e,e,n,n,w," +
        "n,n,w,s,s,w,w,s,s,w,n,n,n,e,n,w,w,s,s,s,s,w,w,n,e,n,w,n,n,e,s,s,e,e,e,e,e,e,e,e,e,e,e,e,e,e,e,s,s,e,n,n,n,w," +
        "n,e,e,s,s,s,s,e,e,n,w,n,n,n,e,s,s,e,e,e,s,s,w,n,n,n,n,e,s,n,e,s,s,s,s,e,e,n,n,n,n,w,s,s,s,n,w,w,w,w,w,w,w,w," +
        "w,w,w,n,w,w,e,n,n,w,w,n,e,e,e,s,e,n,n,w,w,w,w,n,n,e,s,e,e,e,n,w,w,n,n,w,w,n,e,e,e,s,e,n,n,w,w,w,w,n,n,e,s,e," +
        "n,e,e,s,w,w,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,s,w,d,"
    path.split(",").toList
  }

  private val reversePath: List[String] = {
    val path =
      "u,e,n,n,n,n,n,n,n,n,n,n,n,n,n,n,n,n,n,n,n,e,e,n,w,w,s,w,n,w,s,s,e,e,e,e,s,s,w,n,w,w,w,s,e,e,s,s,e,e,s,w,w,w,n," +
        "w,s,s,e,e,e,e,s,s,w,n,w,w,w,s,e,e,s,s,w,e,e,s,e,e,e,e,e,e,e,e,e,e,e,s,n,n,n,e,s,s,s,s,w,w,n,n,n,n,w,s,n,w,s," +
        "s,s,s,e,n,n,w,w,w,n,n,w,s,s,s,e,s,w,w,n,n,n,n,w,w,s,e,s,s,s,w,n,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,n,w,s,s,e," +
        "s,w,s,e,e,n,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,n,n,e,s,s,e,s,s,w,w,n,e,e,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,s,e,e,s,w," +
        "w,s,e,e,s,w,w,s,e,e,e,e,e,e,e,e,e,e,e,e,e,e,s,n,n,n,w,s,s,s,e,s,w,w,n,n,n,n,w,w,s,e,s,s,s,w,n,n,w,w,n,n,w,s," +
        "s,s,e,s,w,w,n,n,n,n,w,w,s,e,s,s,s,w,n,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,n,w,s,s,e,s,w,s,e,e,n,n,n,n,e,e,s,w," +
        "s,s,s,e,n,n,e,e,n,n,e,s,s,s,w,s,e,e,n,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,s,e,e,s,w,w,d,e,n,n,n,n,n,n,n,n,n,n,n,n," +
        "n,n,n,n,n,n,n,e,e,n,w,w,s,w,n,w,s,s,e,e,e,e,s,s,w,n,w,w,w,s,e,e,s,s,e,e,s,w,w,w,n,w,s,s,e,e,e,e,s,s,w,n,w,w," +
        "w,s,e,e,s,s,w,e,e,s,e,e,e,e,e,e,e,e,e,e,e,s,n,n,n,e,s,s,s,s,w,w,n,n,n,n,w,s,n,w,s,s,s,s,e,n,n,w,w,w,n,n,w,s," +
        "s,s,e,s,w,w,n,n,n,n,w,w,s,e,s,s,s,w,n,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,n,w,s,s,e,s,w,s,e,e,n,n,n,n,e,e,s,w," +
        "s,s,s,e,n,n,e,e,n,n,e,s,s,e,s,s,w,w,n,e,e,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,s,e,e,s,w,w,s,e,e,s,w,w,s,e,e,e,e,e," +
        "e,e,e,e,e,e,e,e,e,s,n,n,n,w,s,s,s,e,s,w,w,n,n,n,n,w,w,s,e,s,s,s,w,n,n,w,w,n,n,w,s,s,s,e,s,w,w,n,n,n,n,w,w,s," +
        "e,s,s,s,w,n,n,w,w,w,w,w,w,w,w,w,w,w,w,w,w,w,n,n,w,s,s,e,s,w,s,e,e,n,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,n,n,e,s,s," +
        "s,w,s,e,e,n,n,n,n,e,e,s,w,s,s,s,e,n,n,e,e,s,e,e,s,w,w"
    path.split(",").toList
  }

  override val paths = Map("default" -> defaultPath, "reverse" -> reversePath)
}
