package aug.nodeka

import java.util.regex.MatchResult

case class BaseChar(
                     name: String,
                     spells: List[String] = List.empty,
                     attackSpells: List[String] = List.empty,
                     keepMn: Int = 100,
                     keepSp: Int = 100,
                     keepNd: Int = 100
                   ) {
  def spellup() : Unit = {
    spells.filter(!Spells.isActive(_)).foreach(Spells.cast(_))
  }
  def onRound() : Unit = {}
}

object JibaChar extends BaseChar(
  "jiba",
  List(
    "armor",
    "flight",
    "minor reign of speed",
    "minor reign of strength",
    "sentinel dominance",
    "reign of resistance",
    "reign of speed",
    "reign of strength",
    "koloq",
    "ataghan of inheritance",
    "lorhu's claymore",
    "reign of spirit"
    ),
  List(
    "mental blast",
    "mental tempest",
    "bash",
    "keiiken",
    "striking fist",
    "magic arrow",
    "star of the green flame",
    "winged arc-bolt",
    "elemental malediction",
    "oblique pattern"
  ), 1000, 1000, 150) {
  import Player._
  override def spellup(): Unit = {
    super.spellup()
    if (sp > 400 && nd < 250) Spells.cast("greater invigoration")

  }
}

object DefaultChar extends BaseChar("default")

object Player extends Initable {

  private val chars = List(DefaultChar, JibaChar).map(c=>c.name -> c).toMap

  var client : NodekaClient = null
  var mn = 0
  var hp = 0
  var sp = 0
  var nd = 0
  var gold = 0
  var xp = 0
  var lag = 0

  var char : BaseChar = DefaultChar

  override def init(client: NodekaClient): Unit = {
    Trigger.addFrag("^-->> ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)", (m: MatchResult) => {
      hp = m.group(1).toInt
      sp = m.group(2).toInt
      mn = m.group(3).toInt
      nd = m.group(4).toInt
      gold = m.group(5).toInt
      lag = m.group(6).toInt
    })
  }
}
