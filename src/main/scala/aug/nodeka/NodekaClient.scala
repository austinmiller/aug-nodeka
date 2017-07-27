package aug.nodeka

import java.io.File
import java.util
import java.util.regex.Pattern

import aug.script.shared._

trait Initable {
  def init(client: NodekaClient) : Unit
}

object Profile extends ProfileInterface {
  var profile: ProfileInterface = null
  var com: TextWindowInterface = null
  var metric: TextWindowInterface = null

  override def getConfigDir: File = profile.getConfigDir
  override def setWindowGraph(windowReference: WindowReference): Boolean = profile.setWindowGraph(windowReference)
  override def getTextWindow(s: String): TextWindowInterface = profile.getTextWindow(s)
  override def sendSilently(s: String): Unit = profile.sendSilently(s)
  override def createTextWindow(s: String): TextWindowInterface = profile.createTextWindow(s)
  override def getWindowNames: util.List[String] = profile.getWindowNames
  override def send(s: String): Unit = profile.send(s)

  def execute(s: String): Unit = s.split("\\|").foreach(profile.send)

  def info(s: String): Unit = metric.echo(s"INFO: $s")
  def error(s: String): Unit = metric.echo(s"ERROR: $s")
}

class NodekaClient extends ClientInterface {

  val classes = List(Trigger, Alias, Player, Spells, Prevs, Run, MobTracker)

  Player.client = this

  val prompt = new PromptCapture(this)
  var clientDir : File = null


  // if first boolean is true, don't process further lineHandlers
  // if first is true and latter is true, swallow current line
  val lineHandlers : List[(String, String) => (Boolean, Boolean)] = List[(String, String) => (Boolean, Boolean)](
    Capture.handle,
    Trigger.handle
  )

  override def shutdown(): ReloadData = {
    val rl = new ReloadData
    Reloader.save(rl, classes)
    rl
  }

  override def init(profileInterface: ProfileInterface, reloadData: ReloadData): Unit = {
    val ms = System.currentTimeMillis()

    Profile.profile = profileInterface
    Profile.com = Profile.createTextWindow("com")
    Profile.metric = Profile.createTextWindow("metric")

    clientDir = Profile.getConfigDir

    val graph = new SplitWindow(
      new WindowReference("console"),
      new SplitWindow(
        new WindowReference("com"),
        new WindowReference("metric"),
        false, 0.8f
      ),
      true)

    Profile.setWindowGraph(graph)

    Reloader.load(reloadData, classes)

    classes.foreach(_.init(this))

    val matches = "!!!!!".matches("\\Q!\\E+")
    Profile.info(s"$matches ${Pattern.quote("!")}")

    Profile.info(s"script loaded in ${System.currentTimeMillis() - ms}")
  }

  override def onConnect(): Unit = {}

  override def handleLine(lineNum: Long, line: String): Boolean = {
    val withoutColors = Util.removeColors(line)
    lineHandlers.view.map(_(line, withoutColors)).find(_._1).exists(_._2)
  }

  override def handleFragment(fragment: String): Unit = {
    Trigger.handleFragment(Util.removeColors(fragment))
  }

  override def onDisconnect(): Unit = {}

  override def handleCommand(cmd: String): Boolean = Alias.handle(cmd)

  override def handleGmcp(s: String): Unit = {}
}
