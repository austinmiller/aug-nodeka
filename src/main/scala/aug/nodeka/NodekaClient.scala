package aug.nodeka

import java.io.File
import java.util

import aug.script.framework._
import aug.script.framework.reload.{Reload, Reloader}
import org.mongodb.scala.{MongoClient, MongoDatabase}

trait Initable {
  def init(client: NodekaClient) : Unit
}

object Profile extends ProfileInterface with Initable {
  var profile: ProfileInterface = _
  var com: TextWindowInterface = _
  var metric: TextWindowInterface = _

  @Reload var trace = false

  def execute(s: String): Unit = s.split("\\|").foreach(profile.send)

  def info(s: String): Unit = metric.echo(s"INFO: $s")
  def error(s: String): Unit = metric.echo(s"ERROR: $s")
  def trace(s: String): Unit = if (trace) metric.echo(s"TRACE: $s")

  override def init(client: NodekaClient): Unit = {
    Alias.add("^trace on$", {
      trace = true
      info("trace on")
    })

    Alias.add("^trace off$", {
      trace = false
      info("trace off")
    })
  }

  override def getClientDir: File = profile.getClientDir
  override def setWindowGraph(windowReference: WindowReference): java.lang.Boolean = profile.setWindowGraph(windowReference)
  override def getTextWindow(s: String): TextWindowInterface = profile.getTextWindow(s)
  override def sendSilently(s: String): Unit = profile.sendSilently(s)
  override def createTextWindow(s: String): TextWindowInterface = profile.createTextWindow(s)
  override def getWindowNames: util.List[String] = profile.getWindowNames
  override def send(s: String): Unit = profile.send(s)
  override def logColor(on: java.lang.Boolean): Unit = profile.logColor(on)
  override def logText(on: java.lang.Boolean): Unit = profile.logText(on)
  override def disconnect(): Unit = profile.disconnect()
  override def clientRestart(): Unit = profile.clientRestart()
  override def closeProfile(): Unit = profile.closeProfile()
  override def reconnect(): Unit = profile.reconnect()
  override def connect(): Unit = profile.connect()
  override def printException(throwable: Throwable): Unit = profile.printException(throwable)
  override def clientStop(): Unit = clientStop()
  override def getScheduler(runnableReloaders: Array[RunnableReloader[_ <: Runnable]]) = profile.getScheduler(runnableReloaders)
}

class NodekaClient extends ClientInterface {

  private val classes = List(Profile, Trigger, Alias, Player, Spells, Prevs, Run, MobTracker, Stats)
  private val converters = List(CharConverter, RunStateConverter)

  Player.client = this

  private val prompt = new PromptCapture(this)
  private var clientDir : File = _


  // if first boolean is true, don't process further lineHandlers
  // if first is true and latter is true, swallow current line
  val lineHandlers : List[(String, String) => (Boolean, Boolean)] = List[(String, String) => (Boolean, Boolean)](
    Capture.handle,
    Repop.handle,
    Trigger.handle
  )

  override def shutdown(): ReloadData = {
    val rl = new ReloadData
    Reloader.save(rl, classes, converters)
    rl
  }

  override def init(profileInterface: ProfileInterface, reloadData: ReloadData): Unit = {
    val ms = System.currentTimeMillis()

    Profile.profile = profileInterface
    Profile.com = Profile.createTextWindow("com")
    Profile.metric = Profile.createTextWindow("metric")

    Profile.logColor(true)
    Profile.logText(true)

    clientDir = Profile.getClientDir

    val graph = new SplitWindow(
      new WindowReference("console"),
      new SplitWindow(
        new WindowReference("com"),
        new WindowReference("metric"),
        false, 0.8f
      ),
      true)

    Profile.setWindowGraph(graph)

    val exceptions = Reloader.load(reloadData, classes, converters)
    exceptions.foreach(Profile.printException)
    Profile.info(s"exception size ${exceptions.length}")

    classes.foreach(_.init(this))

    Profile.info(s"script loaded in ${System.currentTimeMillis() - ms}")
  }

  override def handleLine(lineEvent: LineEvent): Boolean = {
    lineHandlers.view.map(_(lineEvent.raw, lineEvent.withoutColors)).find(_._1).exists(_._2)
  }

  override def handleFragment(lineEvent: LineEvent): Unit = {
    Trigger.handleFragment(lineEvent.withoutColors)
  }

  override def handleCommand(cmd: String): Boolean = Alias.handle(cmd)

  override def handleGmcp(s: String): Unit = {}

  override def onConnect(id: Long, url: String, port: Int): Unit = Profile.info(s"[$id] connected to $url:$port")

  override def onDisconnect(id: Long): Unit = {
    Profile.info(s"[$id] disconnected")
    Run.stop()
  }

  override def initDB(mongoClient: MongoClient, mongoDatabase: MongoDatabase) = {}
}
