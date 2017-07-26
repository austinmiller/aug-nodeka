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
  @Reload var mn = 0
  @Reload var hp = 0
  @Reload var sp = 0
  @Reload var nd = 0
  @Reload var gold = 0
  @Reload var xp = 0
  @Reload var lag = 0
  @Reload var level = 0
  @Reload var name = "jiba"

  var char : BaseChar = DefaultChar

  override def init(client: NodekaClient): Unit = {
    char = chars(name) // might have been reloaded

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
