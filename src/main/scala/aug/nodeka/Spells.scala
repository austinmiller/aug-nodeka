package aug.nodeka

import java.util.regex.MatchResult

import scala.collection.mutable

case class Spell(name: String, mn: Int, sp: Int, nd: Int, prev: String) {
  import Player._
  def cast() : Unit = {

  }
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

  private val active = mutable.Set[Spell]()

  def cast(name: String) = spells.get(name).foreach(_.cast())
  def isActive(name: String) = active.contains(spells(name))
  def on(name: String) : Unit = active.add(spells(name))
  def off(name: String) : Unit = active.remove(spells(name))

  Trigger.add("^You gently rise off the ground\\.$", on("flight"))
  Trigger.add("^You're already gifted with magical flight\\.$", on("flight"))

  Trigger.add("^You are no longer affected by: (.*)\\.$", (m: MatchResult) => {
    off(m.group(1))
  })

  def apply(name: String) = spells(name)

  override def init(client: NodekaClient): Unit = {
    Alias.add("^spells active$", client.metric.echo(s"active spells: ${active.map(_.name)}"))
  }
}

object Prevs {
  private val active = mutable.Set[String]()

  def isActive(name: String) = active.contains(name)
  def on(name: String) = active.add(name)
  def off(name: String) = active.remove(name)
}
