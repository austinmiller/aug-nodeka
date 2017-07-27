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
    spells.filter(sp => !Spells.isActive(sp) && Spells(sp).able).foreach(Spells.cast)
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

  var char : BaseChar = DefaultChar

  def kill(target: String): Unit = {
    val found = char.attackSpells.exists {ap =>
      val sp = Spells(ap)
      if(sp.prev == "" || !Prevs.isActive(sp.prev)) {
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

    if (!found) {
      Profile.send(s"kill $target")
    }
  }

  def onCombat: Unit = {
    if (!inCombat) onEnteringCombat
  }

  def onCombatPrompt: Unit = {
    onCombat
    if (round) {
      round = false
      char.onRound()
    }
  }

  def onEnteringCombat: Unit = {
    inCombat = true
  }

  def onEnteringRoom: Unit = {
    enteredRoom = false
    Run.onEnteringRoom
  }

  def onKill: Unit = {
    onCombat
  }

  def onLeavingCombat: Unit = {
    inCombat = false
    char.spellup()
    Run.onLeavingCombat
  }

  def onPrompt: Unit = {
    if (inCombat) {
      onLeavingCombat
    } else if (enteredRoom) {
      onEnteringRoom
    }
  }

  def reportString: String = {
    val max = mstr + mcon + mdex + magi + mint + mwis + mwil + mspe
    val base = str + con + dex + agi + int + wis + wil + spe
    s"level: $level stats: $max/$base"
  }

  def sendStats(chan: String) = {
    Profile.send(s"$chan " +
      s"str $mstr/$str dex $mdex/$dex " +
      s"agi $magi/$agi con $mcon/$con " +
      s"int $mint/$int wis $mwis/$wis " +
      s"wil $mwil/$wil spe $mspe/$spe "
      )
  }

  override def init(client: NodekaClient): Unit = {
    char = chars(name) // might have been reloaded

    Trigger.addFrag("^-->> ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)$", (m: MatchResult) => {
      hp = m.group(1).toInt
      sp = m.group(2).toInt
      mn = m.group(3).toInt
      nd = m.group(4).toInt
      gold = m.group(5).toInt
      lag = m.group(6).toInt

      onPrompt
    })

    Trigger.addFrag("^>-->> ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)", (m: MatchResult) => {
      hp = m.group(1).toInt
      sp = m.group(2).toInt
      mn = m.group(3).toInt
      nd = m.group(4).toInt
      gold = m.group(5).toInt
      lag = m.group(6).toInt

      onCombatPrompt
    })

    Trigger.add("^You receive ([0-9,]*) \\(\\+([0-9,]*) learn, \\+([0-9,]*) rp\\) exp\\.$", {
      onKill
    })

    Trigger.add("^You land \\[ [0-9]+ of [0-9]+ \\] attacks on (a|an) .*: .* damage(\\.|\\!+)$", round = true)

    Trigger.add(".* \\[ exits: .*\\]$", {
      Run.clear
      enteredRoom = true
    })

    Alias.add("^info$", {
      Profile.metric.echo(s"name: $name, hp: $hp, mn: $mn, sp: $sp, nd: $nd, gold: $gold, xp: $xp, lag: $lag, level: $level")
    })

    Alias.add("^sp$", char.spellup())

    Alias.add("^gi$", Profile.send(s"invoke 'greater invigoration'"))
  }
}
