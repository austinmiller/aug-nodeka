package aug.nodeka

import java.io.File
import java.util.regex.Pattern

import aug.script.shared._

trait Initable {
  def init(client: NodekaClient) : Unit
}

object Util {
  def removeColors(string: String): String = string.replaceAll("\u001B\\[.*?m", "")
}

object Capture extends Initable {
  val patterns: List[Pattern] = List(
    "^\\[ .* \\]: '.*'$",
    "^.* tells you, '.*'$",
    "^You tell .*, '.*'$",
    "^##.*",
    "^\\[\\*\\] .* (flame|flames) \\[\\*\\] '.*'$",
    "^.* says, '.*'$",
    "^< emote > .*$",
    "^< social > .*$",
    "^\\[ >>> .* <<<\\]$"
  ).map(Pattern.compile)

  private var com : TextWindowInterface = null

  def handle(line: String, withoutColors: String) : (Boolean, Boolean) = {

    if (patterns.exists(_.matcher(withoutColors).matches())) {
      com.echo(line)
      (true, false)
    } else (false, false)
  }

  override def init(client: NodekaClient): Unit = {
    com = client.com
  }
}

class NodekaClient extends ClientInterface {

  Player.client = this
  var profile: ProfileInterface = null
  var com: TextWindowInterface = null
  var metric: TextWindowInterface = null
  val prompt = new PromptCapture(this)
  var clientDir : File = null


  // if first boolean is true, don't process further lineHandlers
  // if first is true and latter is true, swallow current line
  val lineHandlers : List[(String, String) => (Boolean, Boolean)] = List[(String, String) => (Boolean, Boolean)](
    Capture.handle,
    Trigger.handle
  )

  override def shutdown(): Unit = {}

  override def init(profileInterface: ProfileInterface): Unit = {
    profile = profileInterface
    com = profile.createTextWindow("com")
    metric = profile.createTextWindow("metric")
    clientDir = profile.getConfigDir

    val graph = new SplitWindow(
      new WindowReference("console"),
      new SplitWindow(
        new WindowReference("com"),
        new WindowReference("metric"),
        false, 0.8f
      ),
      true)

    profile.setWindowGraph(graph)

    Array(Capture, Trigger, Alias, Player, Spells).foreach(_.init(this))

    metric.echo(s"loaded $clientDir")
  }

  override def onConnect(): Unit = {}

  override def handleLine(lineNum: Long, line: String): Boolean = {
    val withoutColors = Util.removeColors(line)
    lineHandlers.view.map(_(line, withoutColors)).find(_._1).exists(_._2)
  }

  override def handleFragment(fragment: String): Unit = Trigger.handleFragment(fragment)

  override def onDisconnect(): Unit = {}

  override def handleCommand(cmd: String): Boolean = Alias.handle(cmd)

  override def handleGmcp(s: String): Unit = {}
}
