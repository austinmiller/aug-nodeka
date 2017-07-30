package aug.nodeka

object Stats extends Initable {

  @Reload private var gold: Long = 0
  @Reload private var xp: Long = 0
  @Reload private var startTime: Long = 0
  @Reload private var lastReport: Long = 0
  @Reload private var kills = 0

  private def initialize(): Unit = {
    if (startTime == 0) {
      startTime = System.currentTimeMillis()
      lastReport = startTime
    }
  }

  def addXp(xp: Long): Unit = {
    initialize()
    this.xp += xp
    kills += 1

    report()
  }

  def addGold(gold: Long): Unit = {
    this.gold += gold
  }

  private def report(): Unit = {
    val time = System.currentTimeMillis()
    if (time - lastReport >= 60*1000) {
      lastReport = System.currentTimeMillis()
      info()
    }
  }

  private def info(): Unit = {
    val time = System.currentTimeMillis()
    val dur = (time - startTime) / (60.0 * 1000)
    val xpm = (xp / 1000000) / dur
    val ppm = (gold / 20000) / dur
    val kpm = kills / dur
    val txp = xp / 1000000.0
    val tp = gold / 20000.0
    val s = f"xp: $txp%.1fM, plats: $tp%.1f, kills: $kills%d, " +
      f"xpm: $xpm%.1fM, ppm: $ppm%.1f, kpm: $kpm%.1f, time: $dur%.1f m"
    Profile.info(s)
  }

  def clear(): Unit = {
    if (startTime > 0) info()
    startTime = 0
    xp = 0
    gold = 0
    kills = 0
    Profile.info("stats cleared")
  }

  override def init(client: NodekaClient): Unit = {
    Alias.add("^stats clear$", clear())
    Alias.add("^stats info$", info())
  }
}
