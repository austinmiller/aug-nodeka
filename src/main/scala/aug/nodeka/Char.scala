package aug.nodeka

import java.util.regex.{MatchResult, Pattern}

import scala.annotation.tailrec

case class BaseChar(
                     name: String,
                     spells: List[String] = List.empty,
                     attackSpells: List[String] = List.empty,
                     keepMn: Int = 100,
                     keepSp: Int = 100,
                     keepNd: Int = 100
                   ) {
  def spellup() : Unit = {
    Profile.trace(s"$name is spelling up")
    spells.filter(sp => !Spells.isActive(sp) && Spells(sp).get.able).foreach(Spells.cast)
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

object XiaomingChar extends BaseChar(
  "xiaoming",
  List(
    "radical defiance",
    "globe of fluctuation",
    "stance of symmetry",
    "annulment stance"
//    "mantis stance"
  ),
  List(
    "trip",
    "kick",
    "striking fist",
    "oblique pattern",
    "keiiken",
////    "ashi barai kick",
    "yikwon hand form"
//    "vicious fist"
  ),
  400, 400, 400) {
  import Player._

  override def spellup(): Unit = {
    super.spellup()
    if (sp > 400 && nd < 3500) Spells.cast("invigorate")
    if (sp > 400 && hp < 5000) Spells.cast("meditative healing")
  }
}

object MacabreChar extends BaseChar(
  "macabre",
  List(
    "armor",
    "flight",
    "dark protection",
    "haste",
    "shadow cast",
//    "nefarious shift",
  ),
  List(
    "mental blast",
    "magic arrow",
  ),
  200, 200, 200) {
  import Player._

  override def spellup(): Unit = {
    super.spellup()
    if (mn > 200 && nd < 550) Spells.cast("regalement")
//    if (sp > 400 && hp < 5000) Spells.cast("meditative healing")
  }
}

object DefaultChar extends BaseChar("default")

object Player extends Initable {
  val chars: Map[String, BaseChar] = List(DefaultChar, JibaChar, XiaomingChar, MacabreChar).map(c=>c.name -> c).toMap

  var client : NodekaClient = _

  @Reload var mn = 0
  @Reload var hp = 0
  @Reload var sp = 0
  @Reload var nd = 0
  @Reload var gold = 0
  @Reload var xp = 0
  @Reload var lag = 0
  @Reload var level = 0
  @Reload var align = 0
  @Reload var race = ""
  @Reload var gameClass = ""
  @Reload var round = false
  @Reload var inCombat = false
  @Reload var enteredRoom = false

  @Reload var str = 0
  @Reload var con = 0
  @Reload var dex = 0
  @Reload var agi = 0
  @Reload var int = 0
  @Reload var wis = 0
  @Reload var wil = 0
  @Reload var spe = 0

  @Reload var mstr = 0
  @Reload var mcon = 0
  @Reload var mdex = 0
  @Reload var magi = 0
  @Reload var mint = 0
  @Reload var mwis = 0
  @Reload var mwil = 0
  @Reload var mspe = 0

  @Reload var char : BaseChar = DefaultChar

  def kill(target: String): Unit = {
    val found = char.attackSpells.exists {ap =>
      Spells(ap).exists { sp =>
        if (sp.prev == "" || !Prevs.isActive(sp.prev)) {
          if (sp.mn > 0 && mn > char.keepMn) {
            Profile.send(s"cast '${sp.name}' $target")
            true
          } else if (sp.sp > 0 && this.sp > char.keepSp) {
            Profile.send(s"invoke '${sp.name}' $target")
            true
          } else if (sp.nd > 0 && this.nd > char.keepSp) {
            Profile.send(s"${sp.name} $target")
            true
          } else {
            false
          }
        } else false
      }
    }

    if (!found) {
      Profile.send(s"kill $target")
    }
  }

  def onCombat(): Unit = {
    if (!inCombat) onEnteringCombat()
  }

  def onCombatPrompt(): Unit = {
    onCombat()
    if (round) {
      round = false
      char.onRound()
    }
  }

  def onEnteringCombat(): Unit = {
    inCombat = true
  }

  def onEnteringRoom(): Unit = {
    enteredRoom = false
    Run.onEnteringRoom()
  }

  def onKill(): Unit = {
    onCombat()
  }

  def onLeavingCombat(): Unit = {
    inCombat = false
    char.spellup()
    Run.onLeavingCombat()
  }

  def onPrompt(): Unit = {
    if (inCombat) {
      onLeavingCombat()
    } else if (enteredRoom) {
      onEnteringRoom()
    }
  }

  private def reportString: String = {
    val max = mstr + mcon + mdex + magi + mint + mwis + mwil + mspe
    val base = str + con + dex + agi + int + wis + wil + spe
    s"level: $level stats: $max/$base"
  }

  private def sendStats(chan: String): Unit = {
    Profile.send(s"$chan " +
      s"str $mstr/$str dex $mdex/$dex " +
      s"agi $magi/$agi con $mcon/$con " +
      s"int $mint/$int wis $mwis/$wis " +
      s"wil $mwil/$wil spe $mspe/$spe "
      )
  }

  private def setCharName(name: String): Unit = {
    val correctChar = chars.getOrElse(name.toLowerCase, DefaultChar)

    if (char.name != correctChar.name) {
      char = correctChar
      Spells.clear()
      Prevs.clear()
      Profile.info(s"loaded char $char")
    }
  }

  def speedwalk(string: String): Unit = {
    val list = List.newBuilder[String]

    val pattern = Pattern.compile(s"^(${Util.num})(n|e|s|w|u|d)(.*)$")

    @tailrec
    def process(string: String): Unit = {
      if (string.length > 0) {
        val c = string.charAt(0)
        c match {
          case 'n' | 's' | 'e' | 'u' | 'd' | 'w' =>
            list += c.toString
            process(string.substring(1))

          case _ =>
            val m = pattern.matcher(string)
            if (!m.matches()) {
              Profile.error(s"couldn't process speedwalk substring: $string")
            } else {
              for (i <- 0 until m.group(1).toInt) list += m.group(2)
              process(m.group(3))
            }
        }
      }
    }

    process(string)

    Profile.send(list.result().mkString("\n"))
  }

  override def init(client: NodekaClient): Unit = {
    import Util._
    Trigger.add("^\\[ ([A-Za-z]{2,30}) \\]: Welcome back to Nodeka and thank you for returning\\!$", (m: MatchResult) => {
      setCharName(m.group(1).trim)
    })

    Trigger.add("^You have enough experience to level \\(type gain to increase in level\\)\\!$", {
      if (level < 99) {
        Profile.send("gain\nscore")
      }
    })

    Trigger.add("^ *Name *([A-Z][a-z]{1,30}) *Cln *.*", (m: MatchResult) => {
      val name = m.group(1).trim
      Profile.trace(s"caught name $name")
      setCharName(name)
    })

    Trigger.addFrag("^-->> ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)$", (m: MatchResult) => {
      hp = m.group(1).toInt
      sp = m.group(2).toInt
      mn = m.group(3).toInt
      nd = m.group(4).toInt
      gold = m.group(5).toInt
      lag = m.group(6).toInt

      onPrompt()
    })

    Trigger.addFrag("^>-->> ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)", (m: MatchResult) => {
      hp = m.group(1).toInt
      sp = m.group(2).toInt
      mn = m.group(3).toInt
      nd = m.group(4).toInt
      gold = m.group(5).toInt
      lag = m.group(6).toInt

      onCombatPrompt()
    })

    Trigger.add("^You receive ([0-9,]+) \\(\\+([0-9,]*) learn, \\+([0-9,]*) rp\\) exp out of ([0-9,]+) total exp\\.$",
      (m: MatchResult) => {
      onKill()

      val xp = Util.stripCommas(m.group(1)).toLong +
        Util.stripCommas(m.group(2)).toLong +
        Util.stripCommas(m.group(3)).toLong

      Stats.addXp(xp)
    })

    Trigger.add("^You get a pile of ([0-9,]+) gold\\.(Your inventory is full\\.|)$", (m: MatchResult) => {
      Stats.addGold(Util.stripCommas(m.group(1)).toLong)
    })

    Trigger.add("^You receive ([0-9,]*) \\(\\+([0-9,]*) learn, \\+([0-9,]*) rp\\) exp\\.$", (m: MatchResult) => {
      onKill()

      val xp = Util.stripCommas(m.group(1)).toLong +
        Util.stripCommas(m.group(2)).toLong +
        Util.stripCommas(m.group(3)).toLong

      Stats.addXp(xp)
    })

    Trigger.add("^You land \\[ [0-9]+ of [0-9]+ \\] attacks on (a|an) .*: .* damage(\\.|\\!+)$", round = true)

    Trigger.add(".* \\[ exits: .*\\]$", {
      Run.clear()
      enteredRoom = true
    })

    Trigger.add("^   Race    (.*) Lvl      (.*)   Str ([ 0-9]*)/([ 0-9]*) Int  ([ 0-9]*)/([ 0-9]*)$", (m: MatchResult) => {
      race = m.group(1).trim
      level = m.group(2).trim.toInt
      mstr = m.group(3).trim.toInt
      str = m.group(4).trim.toInt
      mint = m.group(5).trim.toInt
      int = m.group(6).trim.toInt
    })

    Trigger.add("^   Class   (.*) Algn  (.*) Con  ([ 0-9]*)/([ 0-9]*) Wis ([ 0-9]*)/([ 0-9]*)$", (m: MatchResult) => {
      gameClass = m.group(1).trim
      align = m.group(2).trim.toInt
      mcon = m.group(3).trim.toInt
      con = m.group(4).trim.toInt
      mwis = m.group(5).trim.toInt
      wis = m.group(6).trim.toInt
    })

    Trigger.add("^   Cls 2 (.*) Gems (.*) Dex  ([ 0-9]*)/([ 0-9]*) Wil ([ 0-9]*)/([ 0-9]*)$", (m: MatchResult) => {
      mdex = m.group(3).trim.toInt
      dex = m.group(4).trim.toInt
      mwil = m.group(5).trim.toInt
      wil = m.group(6).trim.toInt
    })

    Trigger.add("^   Cls 3 (.*) Prcs ([ 0-9]*) Agi ([ 0-9]*)/([ 0-9]*) Spe ([ 0-9]*)/([ 0-9]*)$", (m: MatchResult) => {
      magi = m.group(3).trim.toInt
      agi = m.group(4).trim.toInt
      mspe = m.group(5).trim.toInt
      spe = m.group(6).trim.toInt
      Profile.info(reportString)
    })

    Trigger.add("^You land \\[ [0-9]+ of [0-9]+ \\] attacks on .*: .* damage(\\.|\\Q!\\E+)$", {
      onEnteringCombat()
    })

    Alias.add("^info$", {
      Profile.metric.echo(s"name: ${char.name}, hp: $hp, mn: $mn, sp: $sp, nd: $nd, gold: $gold, xp: $xp, lag: $lag, level: $level")
    })

    Alias.add("^ss (.*)$", (m: MatchResult) => sendStats(m.group(1)))

    Alias.add("^sp$", char.spellup())

    Alias.add("^gi$", Profile.send(s"invoke 'greater invigoration'"))
    Alias.add("^in$", Profile.send(s"invoke 'invigorate'"))
    Alias.add(s"^#($num) (.*)$$", (m: MatchResult) => {
      val strings = for (i <- 1 to m.group(1).toInt) yield m.group(2)
      Profile.send(strings.mkString("\n"))
    })

    Alias.add(s"^\\.(.*)$$", (m: MatchResult) => speedwalk(m.group(1)))

    Alias.add("^unsplit$", {
      import scala.collection.JavaConverters._
      Profile.getWindowNames.asScala.map(Profile.getTextWindow).foreach(_.unsplit())
    })
  }
}
