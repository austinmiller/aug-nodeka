package aug.nodeka

import java.util.regex.MatchResult

import scala.collection.mutable

case class Spell(name: String, mn: Int, sp: Int, nd: Int, prev: String) {
  def cast(target: String = "") : Unit = {
    if (prev == "" || !Prevs.isActive("prevs")) {
      if (mn > 0 && Player.mn > mn + 100) {
        Profile.send(s"cast '$name' $target")
      } else if (sp > 0 && Player.sp > sp + 100) {
        Profile.send(s"invoke '$name' $target")
      } else if (nd > 0 && Player.nd > sp + 100) {
        Profile.send(s"$name $target")
      }
    }
  }

  def able : Boolean = prev == "" || !Prevs.isActive(prev)
}

object Spells extends Initable {

  val spells = List(
    Spell("armor", 57, 114, 0, ""),
    Spell("ataghan of inheritance", 0, 150, 0, "item creation - basic level"),
    Spell("bash", 0, 0, 23, "impairment - iah"),
    Spell("berserkers focus", 0, 0, 150, ""),
    Spell("concentrated attack", 0, 0, 130, ""),
    Spell("dark protection", 30, 60, 0, ""),
    Spell("demonic affirmation", 157, 303, 0, ""),
    Spell("elemental malediction", 300, 300, 0, ""),
    Spell("flight", 50, 100, 0, ""),
    Spell("greater invigoration", 100, 0, 0, ""),
    Spell("haste", 50, 100, 0, ""),
    Spell("keiiken", 0, 0, 57, "hand form - advanced skill"),
    Spell("koloq", 238, 587, 0, "aura - oe grei"),
    Spell("lorhu's claymore", 0, 682, 0, "item creation - intermediate level"),
    Spell("magic arrow", 6, 9, 0, "magic arrow - basic level"),
    Spell("mental blast", 0, 0, 17, "mental attack - basic level"),
    Spell("mental tempest", 0, 0, 56, "mental attack - intermediate level"),
    Spell("minor reign of speed", 100, 200, 0, ""),
    Spell("minor reign of strength", 100, 200, 0, ""),
    Spell("nefarious shift", 50, 100, 0, ""),
    Spell("oblique pattern", 0, 0, 123, "mystical pattern - intermediate skill"),
    Spell("reign of resistance", 197, 388, 0, ""),
    Spell("reign of speed", 177, 372, 0, ""),
    Spell("reign of spirit", 511, 511, 0, "spirit power, basic"),
    Spell("reign of strength", 183, 382, 0, ""),
    Spell("sentinel dominance", 100, 200, 0, ""),
    Spell("shadow cast", 40, 40, 0, ""),
    Spell("star of the green flame", 54, 101, 0, "fire generation - basic skill"),
    Spell("striking fist", 0, 0, 31, "hand form - basic skill"),
    Spell("valkyrie's agility", 0, 0, 100, ""),
    Spell("vehemence", 0, 0, 72, ""),
    Spell("winged arc-bolt", 125, 233, 0, "mystical pattern - basic skill")
  ).map(s => s.name -> s).toMap

  @Reload
  private val active = mutable.Set[String]()

  def clear(): Unit = active.clear
  def cast(name: String) = spells.get(name).foreach(_.cast())
  def isActive(name: String) = active.contains(name)
  def on(name: String) : Unit = {
    val spell = spells(name)
    active.add(name)
    if (spell.prev != "") Prevs.on(spell.prev)
  }
  def off(name: String) : Unit = active.remove(name)


  def apply(name: String) = spells(name)

  override def init(client: NodekaClient): Unit = {
    Alias.add("^active spells$", Profile.metric.echo(s"active spells: ${active}"))

    Trigger.add("^the ataghan of Jiba you carry withers into dust\\.$", {
      Profile.send("wear cogline, left wield")
      off("ataghan of inheritance")
    })

    Trigger.add("^Jiba's claymore of Lorhu you carry withers into dust\\.$", {
      Profile.send("wear cogline, right wield")
      off("lorhu's claymore")
    })

    Trigger.add("^The ataghan is yours\\.$", {
      Profile.send("remove all, left wield")
      Profile.send("wear ataghan, left wield")
      on("ataghan of inheritance")
    })

    Trigger.add("^Lorhu's claymore finds its way to you\\.$", {
      Profile.send("remove all, right wield")
      Profile.send("wear lorhu, right wield")
      on("lorhu's claymore")
    })

    Trigger.add("^You gently rise off the ground\\.$", on("flight"))
    Trigger.add("^You're already gifted with magical flight\\.$", on("flight"))
    Trigger.add("^The gods strengthen your armor\\.$", on("armor"))
    Trigger.add("^The gods have already blessed you with armor protection\\.$", on("armor"))
    Trigger.add("^The reigns of the quick are yours\\.$", on("minor reign of speed"))
    Trigger.add("^You already possess the reigns of the quick\\.$", on("minor reign of speed"))
    Trigger.add("^The reigns of the strong are yours\\.$", on("minor reign of strength"))
    Trigger.add("^You already possess the reigns of the strong\\.$", on("minor reign of strength"))
    Trigger.add("^You focus your energies into offensive tactics\\.$", on("concentrated attack"))
    Trigger.add("^Your concentration is already focused\\.$", on("concentrated attack"))
    Trigger.add("^Solidarity within you grows\\.$", on("sentinel dominance"))
    Trigger.add("^The sentinel within you is already exposed\\.$", on("sentinel dominance"))
    Trigger.add("^You now possess Valkyrian Agility\\.$", on("valkyrie's agility"))
    Trigger.add("^Valkyrian agility is yours already\\.$", on("valkyrie's agility"))
    // hum of valkyrie's bastion
    //  Trigger.add("^Your humming invokes a bastion of resistance around you\\.$", on(""))
    //  Trigger.add("^You already have the valkyrie's bastion protecting you\\.$", on(""))
    Trigger.add("^Your decrepit hands quiver from incantatory speed\\.$", on("haste"))
    Trigger.add("^The incantatory haste already endows you\\.$", on("haste"))
    Trigger.add("^You cackle evilly as your magic brings life to a mindless deadite\\.$", on("demonic affirmation"))
    Trigger.add("^Your mind is not strong enough to control another\\.$", on("demonic affirmation"))
    Trigger.add("^A mindless deadite is no longer affected by: demonic affirmation\\.$", off("demonic affirmation"))
    Trigger.add("^Protection of dark magic lines your soul\\.$", on("dark protection"))
    Trigger.add("^Dark magic is currently lining your soul\\.$", on("dark protection"))
    Trigger.add("^A wicked darkness shades you\\.$", on("nefarious shift"))
    Trigger.add("^Wickedness already shadows your soul\\.$", on("nefarious shift"))
    Trigger.add("^Your shadow raises from the ground and encompasses your body\\.$", on("shadow cast"))
    Trigger.add("^Your shadow already surrounds you\\.$", on("shadow cast"))
    Trigger.add("^You are already vehement\\.$", on("vehemence"))
    Trigger.add("^The fire of your vehemence burns in your blood\\.$", on("vehemence"))
    Trigger.add("^The berserker within you rises\\.$", on("berserkers focus"))
    Trigger.add("^Focus on the true berserker within you is already apparent\\.$", on("berserkers focus"))
    Trigger.add("^The reigns of the resistant are yours\\.$", on("reign of resistance"))
    Trigger.add("^You already possess the reigns of the resistant\\.$", on("reign of resistance"))
    Trigger.add("^The reigns of the swift are yours\\.$", on("reign of speed"))
    Trigger.add("^You already possess the reigns of the swift\\.$", on("reign of speed"))
    Trigger.add("^The reigns of the mighty are yours\\.$", on("reign of strength"))
    Trigger.add("^You already possess the reigns of the mighty\\.$", on("reign of strength"))
    Trigger.add("^You will time to bend, wind-runner\\.$", on("koloq"))
    Trigger.add("^The reigns of angelic spiritual strength are yours\\.$", on("reign of spirit"))

    Trigger.add("^You are no longer affected by: (.*)\\.$", (m: MatchResult) => {
      off(m.group(1))
    })
  }
}

object Prevs extends Initable {
  import Util._

  @Reload
  private val active = mutable.Set[String]()

  def isActive(name: String) = active.contains(name)
  def on(name: String) = active.add(name)
  def off(name: String) = active.remove(name)
  def clear(): Unit = active.clear

  override def init(client: NodekaClient): Unit = {
    Alias.add("^active prevs$", Profile.metric.echo(s"active prevs: ${active}"))
    Alias.add("^clear prevs$", {
      active.clear
      Profile.metric.echo(s"prevs cleared")
    })

    Trigger.add("^You may again perform (.*) abilities\\.$", (m: MatchResult) => off(m.group(1)))
    Trigger.add("^You cannot perform (.*) abilities again yet \\(type 'prevention'\\)\\.$", (m: MatchResult) => on(m.group(1)))

    Trigger.add("^You decay (a|an) .* mind, (\\(resisted\\) |)(he|she|it) screams in pain as you .* (her|him|it)(\\.|\\Q!\\E+)$", on("mental attack - basic level"))
    Trigger.add("^You break your concentration of mental attack and miss (a|an) .* with its blast\\Q!\\E$", on("mental attack - basic level"))
    Trigger.add("^You try to bash (a|an) .* but mistime your thrust\\Q!\\E$", on("impairment - iah"))
    Trigger.add("^Partially resisted, you still manage to knock (a|an) .* off balance with your forced BASH\\Q!\\E$", on("impairment - iah"))
    Trigger.add("^You drive (a|an) .* off balance with a forced BASH\\Q!\\E$", on("impairment - iah"))
    Trigger.add("^With a tempest, you .* (a|an) .* with your mental warfare(\\Q!\\E+|\\.)$", on("mental attack - intermediate level"))
    Trigger.add("^With an unfocused tempest, you .* (a|an) .* with your mental warfare(\\Q!\\E+|\\.)$", on("mental attack - intermediate level"))
    Trigger.add("^Your mental tempest misses (a|an) .*\\Q!\\E$", on("mental attack - intermediate level"))
    Trigger.add("^A black arrow discharges from your (mystical|decrepit) hands .* (a|an) .*(\\Q!\\E+|\\.)$", on("magic arrow - basic level"))
    Trigger.add("^A black arrow, imperfectly aimed, discharges from your (mystical|decrepit) hands .* (a|an) .*(\\Q!\\E+|\\.)$", on("magic arrow - basic level"))
    Trigger.add("^Your arrow flies astray missing (a|an) .* ... nice shot archer man\\Q!\\E$", on("magic arrow - basic level"))
    Trigger.add("^Your green fire-star .* (a|an) .*(\\Q!\\E+|\\.)$", on("fire generation - basic skill"))
    Trigger.add("^You have run out of premonitions \\(see 'preventions' for more details\\)\\.$", on("clairvoyance"))
    Trigger.add("^Your striking fist .* (a|an) .*(\\.|\\Q!\\E+)$", on("hand form - basic skill"))
    Trigger.add("^Your kick (.*) (a|an) .*(\\.|\\Q!\\E+)$", on("kick - basic skill level"))
    Trigger.add("^Your kick TRIPS (a|an) .* which sets (him|her|it) off balance(\\.|\\Q!\\E+)$", on("trip - basic skill level"))
    Trigger.add("^Your glancing strike .* (a|an) .*(\\.|\\Q!\\E+)$", on("adroit combat feat - basic skill"))
    Trigger.add("^You have run out of basic hums \\(see 'preventions' for more details\\)\\.$", on("basic hum"))
    Trigger.add("^Your unholy scathing .* (a|an) .*(\\.|\\Q!\\E+)$", on("mystical pattern - basic skill"))
    Trigger.add("Your arc-bolt of quickness .* (a|an) .*(\\.|\\Q!\\E+)$", on("mystical pattern - basic skill"))
    Trigger.add("^You formulate a plan - only losers feel pain\\.$", on("aura - kyf naj'k"))
    Trigger.add("^Your charging smite .* (a|an) .*(\\.|\\Q!\\E+)$", on("combat initiative - basic skill"))
    Trigger.add("^You .* (a|an) .* with your open-palmed strike(\\Q!\\E+|\\.)$", on("hand form - advanced skill"))
    Trigger.add("^Your mystical oblique energy .* (a|an) .*(\\.|\\Q!\\E+)$", on("mystical pattern - intermediate skill"))
    Trigger.add("^The ataghan is yours\\.$", on("item creation - basic level"))
    Trigger.add("^You can wrinkle time no more \\(see 'preventions' for more details\\)\\.$", on("the warlock wrinkle"))
  }
}
