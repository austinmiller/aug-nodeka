package aug.nodeka

import java.util.regex.{MatchResult, Pattern}

import scala.collection.mutable.ListBuffer

case class Trigger(regex: String, f: (MatchResult, String) => Unit,
                   consume: Boolean = false, eraseLine: Boolean = false) {
  val pattern = Pattern.compile(regex)

  def execute(line: String) : (Boolean, Boolean) = {
    val matcher = pattern.matcher(line)
    if (matcher.matches()) {
      f(matcher.toMatchResult, line)
      (consume, eraseLine)
    } else (false, false)
  }
}

object Trigger extends Initable {

  private val triggers = new ListBuffer[Trigger]()
  private val colorTriggers = new ListBuffer[Trigger]()
  private val fragmentTriggers = new ListBuffer[Trigger]()

  def init(client: NodekaClient): Unit = {

  }

  def handle(line: String, withoutColors: String): (Boolean, Boolean) = {
    val r = colorTriggers.view.map(_.execute(line)).find(_._1).getOrElse((false, false))
    if (!r._1) {
      triggers.view.map(_.execute(withoutColors)).find(_._1).getOrElse((false, false))
    } else r
  }

  def handleFragment(fragment: String): (Boolean, Boolean) = {
    fragmentTriggers.view.map(_.execute(fragment)).find(_._1).getOrElse((false, false))
  }

  def add[T](regex: String, f: => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f)
    triggers += t
    t
  }

  def add[T](regex: String, f: (MatchResult) => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f(m))
    triggers += t
    t
  }

  def add[T](regex: String, f: (MatchResult, String) => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f(m, s))
    triggers += t
    t
  }


  def addFrag[T](regex: String, f: => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f)
    fragmentTriggers += t
    t
  }

  def addFrag[T](regex: String, f: (MatchResult) => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f(m))
    fragmentTriggers += t
    t
  }

  def addFrag[T](regex: String, f: (MatchResult, String) => T): Trigger = {
    val t = Trigger(regex, (m: MatchResult, s: String) => f(m, s))
    fragmentTriggers += t
    t
  }
}
