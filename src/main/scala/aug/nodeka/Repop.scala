package aug.nodeka

import java.util.regex.Pattern

object Repop {
  val patterns: List[Pattern] = List(
    "^Monkeys screech and howl loudly\\.$"
  ).map(Pattern.compile)

  //"Monkeys screech and howl loudly."
  def handle(line: String, withoutColors: String) : (Boolean, Boolean) = {

    if (patterns.exists(_.matcher(withoutColors).matches())) {
      Profile.metric.echo(s"REPOP [${System.currentTimeMillis()}] $line")
      (true, false)
    } else (false, false)
  }
}
