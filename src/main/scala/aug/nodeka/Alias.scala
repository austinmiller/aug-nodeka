package aug.nodeka

import java.util.regex.{MatchResult, Pattern}

import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

case class Alias(regex: String, function: (MatchResult, String) => Unit) {
  private val pattern = Pattern.compile(regex)

  pattern.matcher("").toMatchResult

  def execute(cmd: String) : Boolean = {
    val matcher = pattern.matcher(cmd)
    if(matcher.matches()) {
      Try {
        function(matcher.toMatchResult, cmd)
      } match {
        case Failure(e) =>
          Profile.error(s"error executing alias $regex, exception ${e.getMessage}")
          e.printStackTrace()
        case _ =>
      }
      true
    } else false
  }
}

object Alias extends Initable {
  private val aliases = ListBuffer[Alias]()

  def add[T](regex: String, f: => T) : Alias = {
    val a = Alias(regex, (MatchResult, String) => f)
    aliases += a
    a
  }

  def add[T](regex: String, f: (MatchResult) => T) : Alias = {
    val a = Alias(regex, (m: MatchResult, s: String) => f(m))
    aliases += a
    a
  }

  def add[T](regex: String, f: (MatchResult, String) => T) : Alias = {
    val a = Alias(regex, (m: MatchResult, s: String) => f(m, s))
    aliases += a
    a
  }

  def handle(cmd: String) = aliases.exists(_.execute(cmd))

  override def init(client: NodekaClient): Unit = {
    add("^alias count$", Profile.info(s"There are ${aliases.size} aliases."))
  }
}
