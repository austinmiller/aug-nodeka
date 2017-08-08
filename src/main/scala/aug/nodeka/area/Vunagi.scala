package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Vunagi extends Area {
  override val name = "Vunagi Jungle"
  override val keyword = "vunagi"
  override val mobTriggers = List(
    MobTrigger("^This tiny pixie floats around on its newly formed wings\\.$", "pixie"),
//    MobTrigger("^This silver elf is an elder of the forest, having been present at its creation\\.$", "elf"),
    MobTrigger("^An ophidian priest wanders here, head down but eyes alert\\.$", "priest"),
    MobTrigger("^This boar ruts around for roots and berries\\.$", "boar"),
    MobTrigger("^Casted out of his tribe, this ophidian lives in shame and constant fear\\.$", "ophidian"),
    MobTrigger("^A beautiful bird sings a magical song, filling the forest with music\\.$", "bird"),
    MobTrigger("^This young hobbit runs around the forest playing with his friends\\.$", "hobbit"),
    MobTrigger("^This thain looks at the youngsters running about with quiet reflection\\.$", "thain"),
    MobTrigger("^A crowd of \\[ ([1-9][0-9]{0,}) \\] ophidians step through the jungle, silent except for the occassional hiss\\.$", "ophidian"),
    MobTrigger("^A young elf stands quietly observing the forest\\.$", "elf"),
    MobTrigger("^A sylph is flying around the room, surrounded by a green glow\\.$", "sylph"),
    MobTrigger("^A huge boarlike rot-sniffer feasts on a mound of decomposing fruit\\.$", "rot"),
    MobTrigger("^A broken spirit drifts slowly and silently, its face contorted in a permanent rictus of suffering\\.$", "spirit"),
    MobTrigger("^A silky black raven hovers high above the desert\\.$", "raven"),
    MobTrigger("^A hairy tree sloth ambles along quietly\\.$", "sloth"),
    MobTrigger("^A needletooth gar glides swiftly through the water\\.$", "gar"),
    MobTrigger("^An ophidian tribe-head rests here, eyes half-closed in meditation\\.$", "ophidian"),
    MobTrigger("^A flock of \\[ ([1-9][0-9]{0,}) \\] noisy toucans squawk in cacophonic disarray\\.$", "toucan"),
    MobTrigger("^A school of \\[ ([1-9][0-9]{0,}) \\] needletooth garfish glides through the water\\.$", "garfish"),
    MobTrigger("^A noisy toucan hops from branch to branch clumsily\\.$", "toucan"),
    MobTrigger("^An ophidian shaman inhales the contents of a leather pouch, swaying to some rhythm\\.$", "shaman"),
    MobTrigger("^A pack of \\[ ([1-9][0-9]{0,}) \\] hairy tree sloths shuffle along at a relaxed pace\\.$", "sloth"),
    MobTrigger("^A group of \\[ ([1-9][0-9]{0,}) \\] tortured prisoners lie in a heap of bruises and scarred flesh\\.$", "prisoner"),
    MobTrigger("^A pack of \\[ ([1-9][0-9]{0,}) \\] howler monkeys swing frantically through the branches, howling wildly\\.$", "monkey"),
    MobTrigger("^An ophidian hisses to himself as he steps quietly through the jungle\\.$", "ophidian"),
    MobTrigger("^A sprite is flying through the room, causing a slight breeze to form in its wake\\.$", "sprite"),
    MobTrigger("^A nest of \\[ ([1-9][0-9]{0,}) \\] whipcoils slither within a wide bough\\.$", "whipcoil"),
    MobTrigger("^This wild horse grazes quietly on the grass of the forest\\.$", "horse"),
    MobTrigger("^This stoor sits quietly smoking his pipe, enjoying the silence of the forest\\.$", "stoor"),
    MobTrigger("^An ophidian sentry patrols the area, hissing menacingly\\.$", "sentry"),
    MobTrigger("^A long, slender whipcoil boa wraps its thin coils around a branch\\.$", "whipcoil"),
    MobTrigger("^A dingy orange monkey swings from a branch, howling wildly\\.$", "monkey"),
    MobTrigger("^A grey elf sits quietly on the ground, looking up into the sky\\.$", "elf"),
    MobTrigger("^A group of \\[ ([1-9][0-9]{0,}) \\] ophidian priests murmur and hiss softly\\.$", "priest"),
    MobTrigger("^A tortured prisoner lies in a heap, scarred and motionless\\.$", "prisoner"),
    MobTrigger("^A pack of \\[ ([1-9][0-9]{0,}) \\] rot-sniffers scout the rotting underbrush in search of pungent delicacies\\.$", "rot"),
    MobTrigger("^Casted out of their tribes, these \\[ ([1-9][0-9]{0,}) \\] ophidians live in shame and constant fear\\.$", "ophidian")
  )

  val repop: String = "Monkeys screech and howl loudly."

  private val defaultPath: List[String] = {
    val path =
      "s,s,u,s,n,d,n,n,w,w,n,n,e,s,u,n,n,n,e,e,s,s,s,d,n,n,e,n,n,n,e,n,w,e,s,s,s,e,n,e,d,n,e,d,e,u,s,e,e,n,n,e,s,w,s," +
        "s,w,w,w,n,s,s,s,s,e,e,n,w,e,s,w,w,n,d,w,n,u,u,n,w,s,w,s,w,s,s,w,w,n,n,n,w,w,n,n,n,w,s,s,s,s,w,w,s,n,n,n,n,w," +
        "s,s,u,n,n,n,w,w,s,e,s,s,e,s,s,s,n,w,e,n,n,d,e,s,e,e,n,n,n,n,e,e,n,w,w,u,w,w,d,w,w,s,w,w,n,n,e,e,s,w,e,e,e,u," +
        "e,e,d,e,e,n,n,e,e,s,s,s,w,n,n,u,s,s,s,s,n,n,n,n,w,w,w,n,n,n,e,e,n,n,s,s,w,w,s,w,w,w,n,n,n,d,e,s,s,s,e,n,w,n," +
        "n,w,w,s,n,w,s,n,n,s,w,s,n,n,s,w,s,n,w,e,n,w,e,n,w,e,n,e,s,e,n,n,w,s,w,w,e,n,w,e,n,w,e,n,w,e,n,s,e,n,s,s,n,e," +
        "n,s,s,n,e,n,s,e,n,s,e,w,s,e,w,w,e,s,e,w,w,e,s,e,w,w,e,s,e,w,w,e,e,w,s,w,e,e,w,s,u,s,s,s,e,e,e,s,s,e,e,e,e,e," +
        "s,n,e,e,e,e,n,n,d,e,s,s,s,e,n,w,n,n,w,w,s,n,w,s,n,n,s,w,s,n,n,s,w,s,n,w,e,n,w,e,n,w,e,n,e,n,e,s,s,w,n,w,w,e," +
        "n,w,e,n,w,e,n,w,e,n,s,e,n,s,s,n,e,n,s,s,n,e,n,s,e,n,s,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w," +
        "s,u,s,s,w,w,w,n,n,n,n,n,e,e,e,n,d,w,w,w,w,s,w,w,w,e,n,n,e,s,e,n,w,n,n,e,n,e,n,e,e,n,e,s,s,w,n,w,w,n,w,s,w,s," +
        "w,s,n,w,n,w,w,n,w,s,s,e,n,e,e,n,e,s,e,n,n,w,e,n,n,e,u,e,w,w,e,n,w,e,e,w,n,w,e,e,w,n,w,e,e,w,s,s,s,d,w,s,s,e," +
        "d," +
        "e,n,s,s,n,e,n,s,s,n,e,n,s,s,n,e,n,s,e,w,s,e,w,s,e,w,w,e,s,e,w,w,e,n,n,n,w,w,w,w,u,w,s,s,s,s,s,s,s,s,w,w,u,s," +
        "s,w,w,s,s,s,e,e,e,d,s,s,w,w,s,s,s,e,e,s,s,s"
//        "" +
//        "w,w,e,n,w,e,n,w,e,n,w,e,n,s,e,n,s,s,n,e,n,s,s,n,e,n,s,e,n,s,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s," +
//        "w,e,e,w,s,u," +
//        "s,s,w,w,w,n,n,n,n,n,e,e,e,n,d,w,w,w,w,s,w,w,w,e,n,n,e,s,e,n,w,n,n,e,n,e,n,e,e,n,e,s,s,w,n,w,w,n," +
//        "w,s,w,s,w,s,n,w,n,w,w,n,w,s,s,e,n,e,e,n,e,s,e,n,n,w,e,n,n,e,u,e,w,w,e,n,w,e,e,w,n,w,e,e,w,n,w,e,e,w,s,s,s,d," +
//        "w,s,s,e,d,e,s,n,n,s,e,s,n,n,s,e,s,n,n,s,e,n,s,e,e,w,s,e,w,s,e,w,w,w,e,s,w,e,e"



//    val path =
//      "s,s,u,s,n,d," +
//        "n,n,w,w,n,n," +
//        "e,s,u,n,n,n," +
//        "e,e,s,s,s,d," +
//        "n,n,e,n,n,n," +
//        "e,n,w,e,s,s," +
//        "s,e,n,e,d,n," +
//        "e,d,e,u,s,e," +
//        "e,n,n,e,s,w,s," +
//        "s,w,w," +
//        "w,n,s," +
//        "s,s,s," +
//        "e,e,n," +
//        "w,e,s," +
//        "w,w,n," +
//        "d,w,n," +
//        "u,u,n," +
//        "w,s,w," +
//        "s,w,s," +
//        "s,w,w," +
//        "n,n,n," +
//        "w,w,n," +
//        "n,n,w," +
//        "s,s,s," +
//        "s,w,w," +
//        "s,n,n," +
//        "n,n,w," + // correct
//        "s,s,u," +
//        "n,n,n," +
//        "w,w,s," +
//        "e,s,s," +
//        "e,s,s," +
//        "s,n,w," +
//        "e,n,n," +
//        "d,e,s," +
//        "e,e,n," +
//        "n,n,n," +
//        "e,e,n," +
//        "w,w,u," +
//        "w,w,d," +
//        "w,w,s," +
//        "w,w,n," +
//        "n,e,e," +
//        "s,w,e," +
//        "e,e,u," + // correct
//        "e,e,d," +
//        "e,e,n," +
//        "n,e,e," +
//        "s,s,s," +
//        "w,n,n," +
//        "u,s,s," +
//        "s,s,n," +
//        "n,n,n," +
//        "w,w,w," +
//        "n,n,n," +
//        "e,e,n," +
//        "n,s,s," +
//        "w,w,s," +
//        "w,w,w," +
//        "n,n,n," +
//        "d,e,s," +
//        "s,s,e," +
//        "n,w,n," + // correct
//        "n,w,w," +
//        "s,n,w," +
//        "s,n,n," +
//        "s,w,s," +
//        "n,n,s," +
//        "w,s,n," +
//        "w,e,n," +
//        "w,e,n," +
//        "w,e,n," +
//        "e,s,e," +
//        "n,n,w," +
//        "s,w,w," +
//        "e,n,w," +
//        "e,n,w," +
//        "e,n,w," +
//        "e,n,s," +
//        "e,n,s," +
//        "s,n,e," + // correct
//        "n,s,s," +
//        "n,e,n," +
//        "s,e,n," +
//        "s,e,w," +
//        "s,e,w," +
//        "w,e,s," +
//        "e,w,w," +
//        "e,s,e," +
//        "w,w,e," +
//        "s,e,w," +
//        "w,e,e," +
//        "w,s,w," +
//        "e,e,w," +
//        "s,u,s," +
//        "s,s,e," +
//        "e,e,s," +
//        "s,e,e," +
//        "e,e,e," + // correct
//        "s,n,e," +
//        "e,e,e," +
//        "n,n,d," +
//        "e,s,s," +
//        "s,e,n," +
//        "w,n,n," +
//        "w,w,s," +
//        "n,w,s," +
//        "n,n,s," +
//        "w,s,n," +
//        "n,s,w," +
//        "s,n,w," +
//        "e,n,w," +
//        "e,n,w," +
//        "e,n,e," +
//        "n,e,s," +
//        "s,w,n," +
//        "w,w,e," + // correct
//        "n,w,e," +
//        "n,w,e," +
//        "n,w,e," +
//        "n,s,e," +
//        "n,s,s," +
//        "n,e,n," +
//        "s,s,n," +
//        "e,n,s," +
//        "e,n,s," +
//        "e,w,s," +
//        "w,e,e," +
//        "w,s,w," +
//        "e,e,w," +
//        "s,w,e," +
//        "e,w,s," +
//        "w,e,e," +
//        "w,s,w," +
//        "e,e,w," + // correct
//        "s,u,s," +
//        "s,w,w," +
//        "w,n,n," +
//        "n,n,n," +
//        "e,e,e," +
//        "n,d,w," +
//        "w,w,w," +
//        "s,w,w," +
//        "w,e,n," +
//        "n,e,s," +
//        "e,n,w," +
//        "n,n,e," +
//        "n,e,n," +
//        "e,e,n," +
//        "e,s,s," +
//        "w,n,w," +
//        "w,n,w," +
//        "s,w,s," + //correct
//        "w,s,n," +
//        "w,n,w," +
//        "w,n,w," +
//        "s,s,e," +
//        "n,e,e," +
//        "n,e,s," +
//        "e,n,n," +
//        "w,e,e," + // correct
//        "n,e,u," +
//        "e,w,w," +
//        "e,n,w," +
//        "e,e,w," +
//        "n,w,e," +
//        "e,w,n," +
//        "w,e,e," +
//        "w,s,s,s,d,w,s,s,e," +
//        "d,w,w,e,n,w,e,n,w,e,n,w,e,n,s,e,n,s,s,n,e,n,s,s,n,e,n,s,e,n,s,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s,w,e,e,w,s," +
//        "w,e,e,w,s,u,s,s,w,w,w,n,n,n,n,n,e,e,e,n,d,w,w,w,w,s,w,w,w,e,n,n,e,s,e,n,w,n,n,e,n,e,n,e,e,n,e,s,s,w,n,w,w,n," +
//        "w,s,w,s,w,s,n,w,n,w,w,n,w,s,s,e,n,e,e,n,e,s,e,n,n,w,e,n,n,e,u,e,w,w,e,n,w,e,e,w,n,w,e,e,w,n,w,e,e,w,s,s,s,d," +
//        "w,s,s,e,d,e,s,n,n,s,e,s,n,n,s,e,s,n,n,s,e,n,s,e,e,w,s,e,w,s,e,w,w,w,e,s,w,e,e"
    path.split(",").toList
  }

  override val paths = Map("default" -> defaultPath)
}
