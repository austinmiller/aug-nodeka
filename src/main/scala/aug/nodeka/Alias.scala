package aug.nodeka

import java.util.regex.{MatchResult, Pattern}

import scala.collection.mutable.ListBuffer

case class Alias(regex: String, function: (MatchResult, String) => Unit) {
  private val pattern = Pattern.compile(regex)

  pattern.matcher("").toMatchResult

  def execute(cmd: String) : Boolean = {
    val matcher = pattern.matcher(cmd)
    if(matcher.matches()) {
      function(matcher.toMatchResult, cmd)
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
  def add(regex: String, f: (String) => Unit) : Alias = {
    val a = Alias(regex, (m: MatchResult, s: String) => f(s))
    aliases += a
    a
  }

  def handle(cmd: String) = aliases.exists(_.execute(cmd))

  override def init(client: NodekaClient): Unit = {
    add("^alias count$", client.metric.echo(s"There are ${aliases.size} aliases."))
  }
}
