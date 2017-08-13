package aug.nodeka.area

import aug.nodeka.{Area, MobTrigger}

object Vestil extends Area {
  private val defaultPath = List(
    "n","n","n","w","w","n","n","e","e","e","e","e","e","s","e","e","e","e","e","d","e","u","e","e",
    "e","e","n","n","e","n","e","e","s","s","s","e","e","e","n","w","n","w","e","e","n","n","n","w","w",
    "e","e","n","n","w","w","n","n","s","e","e","n","n","n","w","w","e","e","n","n","n","n","w","w","e",
    "e","n","n","n","n","n","n","n","n","w","s","s","s","s","w","n","n","n","n","w","s","s","s","s","s",
    "s","w","s","e","s","w","s","e","s","w","s","e","s","w","s","e","s","w","e","s","s","w","w","w","e",
    "s","e","e","s","s","s","w","w","s","s","n","e","e","s","s","n","n","w","w","w","n","n","n","n","w",
    "w","n","w","s","w","n","e","e","s","e","e","n","n","n","w","w","n","w","s","n","n","n","n","e","s",
    "s","s","s","e","e","n","n","n","n","w","w","e","e","n","n","w","w","w","n","n","n","e","s","s","s",
    "e","e","n","n","n","w","w","e","e","n","n","w","e","n","w","w","w","w","w","d","w","u","s","s","s",
    "s","s","s","s","s","s","s","s","s","s","s","e","s","w","s","e","s","w","s","s","s","s","s","e","w",
    "s","w","n","n","n","n","w","n","w","e","e","n","n","n","n","n","n","n","n","n","n","n","n","n","n",
    "n","n","n","n","s","s","s","s","s","s","s","s","w","s","s","n","w","w","s","w","n","w","s","e","s",
    "s","e","s","e","e","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","w","n",
    "e","e","w","n","w","n","e","e","n","w","w","e","e","e","e","e","e","e","s","e","e","w","s","s","w",
    "e","e","w","n","n","n","e","e","e","s","e","e","w","s","w","w","s","w","w","e","e","e","e","e","w",
    "n","n","e","n","e","e","e","e","s","w","w","e","s","s","w","e","e","w","n","n","n","e","e","e","e",
    "s","s","s","s","s","w","w","w","w","w","w","w","w","w","w","n","w","w","w","w","s","w","n","w","w",
    "w","s","w","w","n","n","n","n","n","n","n","w","n","e","n","n","n","n","e","e","e","e","e","e","e",
    "n","w","e","e","n","n","n","n","w","s","s","s","s","s","s","s","s","s","s","s","s","s","e","s","s",
    "s","w","w","s","w","s","e","e","s","w","s","w","s","s","s","s","w","s","w","s","n","n","e","n","n",
    "n","n","n","n","w","s","s","s","s","s","s","s","w","n","n","n","n","n","n","n","n","n","n","w","s",
    "s","s","s","s","s","s","s","s","w","s","s","n","n","n","n","n","n","n","n","n","n","n","n","n","n",
    "e","s","s","s","s","w","s","s","s","w","n","s","w","w","s","e","w","s","s","e","w","s","e","e","e",
    "w","s","s","w","s","w","e","e","n","n","e","e","e","e","e","e","e","e","e","e","e","e","s","s","s",
    "s","e","e","s","s","s"
  )

  override val mobTriggers: List[MobTrigger] = List(
    MobTrigger("^A bull lizard bathes lazily in the sun\\.$", "lizard"),
    MobTrigger("^A group of \\[ ([1-9][0-9]{0,}) \\] beetles dig a new hovel\\.$", "beetle"),
    MobTrigger("^A lone vulture circles, searching for decaying scraps\\. $", "vulture"),
    MobTrigger("^A small black beetle burrows in the sand\\.$", "beetle"),
    MobTrigger("^A tanned traveler rests here, looking up from his seat on the sand\\.$", "traveler"),
//    MobTrigger("^A white light emanates from within this spirit\\.  $", "spirit"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] desert lizards run through the sand\\.$", "lizard"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] female lizards jostle their way across the desert\\. $", "lizard"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] hunters silently stalk their prey\\.$", "hunter"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] male lizards look drowsily at you\\.$", "lizard"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] travelers are taking a rest from their desert trek\\.$", "traveler"),
    MobTrigger("^These \\[ ([1-9][0-9]{0,}) \\] vultures scour the desert for sustenance\\. $", "vulture"),
    MobTrigger("^This female lizard lumbers across the scorching sand\\. $", "lizard"),
    MobTrigger("^This pack of \\[ ([1-9][0-9]{0,}) \\] scrawny dogs bound through the dunes\\.$", "dog"),
    MobTrigger("^This scrawny dog lopes across the desert\\.$", "dog"),
    MobTrigger("^This silent hunter pads across the sand\\.$", "hunter"),
    MobTrigger("^This small lizard skitters across the sand\\.$", "lizard")
  )
  override val paths: Map[String, List[String]] = Map("default" -> defaultPath)
  override val name: String = "The Vestil Desert"
  override val keyword: String = "vestil"
}
