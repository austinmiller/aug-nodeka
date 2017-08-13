package aug.nodeka

import java.util.regex.MatchResult

import aug.nodeka.area._

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

  val areas: Map[String, Area] = List(
    Vestil,
    Amras,
    Quad,
    Things,
    Rats,
    Telg,
    Vunagi
  ).map(a=> a.keyword -> a).toMap

  @Reload private var targets = new mutable.Queue[String]()
  @Reload private var path = new mutable.Queue[String]()
  @Reload private var paused = false
  @Reload private var loop = true
  @Reload private var state : RunState = Stopped
  @Reload private var lastCmd : String = ""
  @Reload private var area : String = ""
  @Reload private var pathName : String = ""
  @Reload private var moving: Boolean = false
  @Reload private var lastNext: Long = 0

  private var mobTriggers : Option[List[Trigger]] = None

  private def kill(): Unit = {
    Player.kill(targets.dequeue)
  }

  private def stop(): Unit = {
    if (state == Stopped) {
      Profile.error(s"You are not running anything.")
    } else {
      state = Stopped
      area = ""
      paused = false
      mobTriggers.foreach(_.foreach(Trigger.remove))
      mobTriggers = None
      Profile.info("Run is stopped.")
      Stats.clear()
    }
  }

  private def next(): Unit = {
    lastNext = System.currentTimeMillis()
    if (!paused && state != Stopped) {

      state match {
        case Walking => if (targets.nonEmpty) kill()
        case Running =>
          if (targets.nonEmpty) {
            kill()
          } else if (path.nonEmpty) {
            lastCmd = path.dequeue
            moving = true
            Profile.execute(lastCmd)
          } else {
            if (loop) {
              loadPath()
              Profile.send("look")
              Profile.info("looping run")
            } else stop()
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

  def pause(): Unit = {
    if (paused) {
      Profile.error("run already paused")
    } else {
      paused = true
      Profile.info("run paused")
    }
  }

  def resume(): Unit = {
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

  private def loadPath(): Unit = {
    path.clear()
    path ++= areas(area).paths(pathName)
  }

  def clear(): Unit = targets.clear

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
    loadPath()

    loadMobTriggers(area)

    Profile.info(s"running ${area.name} with path $pathName")
    Profile.send("look")
  }

  def addTarget(target: String, count: Int = 1): Unit = {
    for(i <- 1 to count) targets += target
  }
  def onEnteringRoom() : Unit = {
    moving = false
    next()
  }
  def onLeavingCombat() : Unit = {
    if (moving) {
      Profile.send(lastCmd)
    } else next()
  }

  override def init(client: NodekaClient): Unit = {
    areas.get(area).foreach(loadMobTriggers)

    Trigger.add("^They aren't here\\.$", {
      if (state == Running) {
        next()
      }
    })

    Trigger.add("^There is no one here by that name\\.$", {
      if (state != Stopped) {
        next()
      }
    })

    Trigger.add("^You lack the means to go there\\.$", {
      if (state == Running && !paused) {
        Profile.send(lastCmd)
      }
    })

    Trigger.add("^The closed (.*) block\\(s\\) your passage (.*)\\.$", (m: MatchResult) => {
      Profile.send(s"open ${m.group(2)}.${m.group(1)}\n${m.group(2)}")
    })

    Alias.add("^run (.*)$", (m: MatchResult) => {
      val n = m.group(1).split(" ")
      if (n.length >= 2) run(n(0), n(1)) else run(n(0), "default")
    })
    Alias.add("^stop$", stop())
    Alias.add("^targets$", Profile.info(s"run targets: $targets"))
    Alias.add("^pause$", pause())
    Alias.add("^resume$", resume())
    Alias.add("^path left$", {
      Profile.info(path.toList.mkString(",").replace("\n", "\\n"))
      Profile.info(s"path size: ${path.size}")
    })

    Alias.add("^reverse$", {
      Profile.com.echo(Telg.paths("default").reverse.map {
        case "n" => "s"
        case "w" => "e"
        case "s" => "n"
        case "e" => "w"
        case "u" => "d"
        case "d" => "u"
      }.mkString(","))
    })

    Profile.getScheduler(Array.empty).every(5000, 10000, () => {
      if (System.currentTimeMillis() - lastNext > 10000) {
        Profile.info("heartbeat next")
        next()
      }
    })
  }
}
