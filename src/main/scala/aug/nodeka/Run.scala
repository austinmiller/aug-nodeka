package aug.nodeka

import java.util.regex.MatchResult

import aug.nodeka.area.{Amras, Vestil}

import scala.collection.mutable

case class MobTrigger(regex: String, keyword: String)

abstract class Area {
  val name : String
  val keyword: String
  val mobTriggers : List[MobTrigger]
  val paths : Map[String, List[String]]
}

sealed trait RunState

case object Stopped extends RunState
case object Running extends RunState
case object Walking extends RunState


object Run extends Initable {

  val areas: Map[String, Area] = List(Vestil, Amras).map(a=> a.keyword -> a).toMap

  @Reload
  private var targets = new mutable.Queue[String]()

  @Reload
  private var path = new mutable.Queue[String]()

  @Reload
  private var paused = false

  @Reload
  private var loop = true

  @Reload
  private var state : RunState = Stopped

  @Reload
  private var lastCmd : String = ""

  @Reload
  private var area : String = ""

  @Reload
  private var pathName : String = ""

  private var mobTriggers : Option[List[Trigger]] = None

  private def kill : Unit = {
    Player.kill(targets.dequeue)
  }

  private def stop : Unit = {
    if (state == Stopped) {
      Profile.error(s"You are not running anything.")
    } else {
      state = Stopped
      area = ""
      paused = false
      mobTriggers.foreach(_.foreach(Trigger.remove))
      mobTriggers = None
    }
  }

  private def next : Unit = {
    if (!paused) {
      state match {
        case Walking => if (!targets.isEmpty) kill
        case Running =>
          if (!targets.isEmpty) {
            kill
          } else if (!path.isEmpty) {
            lastCmd = path.dequeue
            Profile.execute(lastCmd)
          } else {
            if (loop) {
              loadPath
              Profile.send("look")
              Profile.info("looping run")
            } else stop
          }

        case _ =>
      }

    }
  }

  private def handleMobTrigger(mobTrigger: MobTrigger)(m: MatchResult): Unit = {
    val count = if (m.groupCount() >= 1) {
      m.group(1).toInt
    } else 1
    addTarget(mobTrigger.keyword, count)
  }

  def pause : Unit = {
    if (paused) {
      Profile.error("run already paused")
    } else {
      paused = true
      Profile.info("run paused")
    }
  }

  def resume : Unit = {
    if (paused) {
      paused = false
      Profile.send("look")
      Profile.info("resuming run")
    } else {
      Profile.error("run is not paused")
    }
  }

  private def loadMobTriggers(area: Area) {
    mobTriggers = Some(area.mobTriggers.map(mt => Trigger.add(mt.regex, handleMobTrigger(mt)(_))))
  }

  private def loadPath : Unit = {
    path.clear()
    path ++= areas(area).paths(pathName)
  }

  def clear: Unit = targets.clear

  def run(areaName: String, pathName: String): Unit = {
    if (!areas.contains(areaName)) {
      Profile.error(s"area $areaName not found")
      return
    }

    if (state != Stopped) {
      Profile.error("you're already running something")
      return
    }

    val area = areas(areaName)

    if (!area.paths.contains(pathName)) {
      Profile.error(s"area $areaName does not have path $pathName")
      return
    }

    this.area = areaName
    this.pathName = pathName
    state = Running
    loadPath

    loadMobTriggers(area)

    Profile.info(s"running $areaName with path $pathName")
    Profile.send("look\n")
  }

  def addTarget(target: String, count: Int = 1) = {
    for(i <- 1 to count) targets += target
  }
  def onEnteringRoom : Unit = next
  def onLeavingCombat : Unit = next

  override def init(client: NodekaClient): Unit = {
    areas.get(area).foreach(loadMobTriggers)

    Trigger.add("^They aren't here\\.$", {
      if (state == Running) {
        next
      }
    })

    Alias.add("^run (.*)$", (m: MatchResult) => {
      val n = m.group(1).split(" ")
      if (n.length >= 2) run(n(0), n(1)) else run(n(0), "default")
    })
    Alias.add("^stop$", stop)
    Alias.add("^targets$", Profile.info(s"run targets: $targets"))
    Alias.add("^pause$", pause)
    Alias.add("^resume$", resume)
  }
}