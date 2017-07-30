package aug.nodeka

import java.util.regex.Pattern

object Capture {
  val patterns: List[Pattern] = List(
    "^\\[ .* \\]: '.*'$",
    "^.* tells you, '.*'$",
    "^You tell .*, '.*'$",
    "^##.*",
    "^\\[\\*\\] .* (flame|flames) \\[\\*\\] '.*'$",
    "^.* says, '.*'$",
    "^< emote > .*$",
    "^< social > .*$",
    "^\\[ >>> .* <<< \\]$"
  ).map(Pattern.compile)

  def handle(line: String, withoutColors: String) : (Boolean, Boolean) = {

    if (patterns.exists(_.matcher(withoutColors).matches())) {
      Profile.com.echo(line)
      (true, false)
    } else (false, false)
  }
}