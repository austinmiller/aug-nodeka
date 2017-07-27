package aug.nodeka

import java.io.{File, FileOutputStream}
import java.util.regex.MatchResult

import scala.collection.mutable


object MobTracker extends Initable {
  import Util._

  @Reload
  private var regexes = mutable.Set[String]()

  def add(m: MatchResult, s: String) = regexes.add(removeColors(s))

  def toRegex(s: String): String = {
    s.replaceAll("^", "^")
      .replace("[", "\\\\[")
      .replace("]", "\\\\]")
      .replace(".", "\\\\.")
      .replaceAll(num, num) + "$"
  }

  def write: Unit = {
    val f = new File(Profile.getConfigDir, "mobStrings")
    TryWith(new FileOutputStream(f, true)) { fos =>
      regexes.map(toRegex).foreach { r=>
        val str = "MobTrigger(\"" + r + "\", \"\"),\n"
        fos.write(str.getBytes)
      }
    }
    Profile.info("write mob string file")
  }

  override def init(client: NodekaClient): Unit = {
    Alias.add("^mobs on$", {
      Trigger.addColor(s"^${cc(1,37)}.*${cc(1,36)}.*", add _)
      Trigger.add(s".*\\[ $num \\].*", add _)
      Trigger.addColor(s".*${cc(1,33)}\\[ ${cc(0)}${cc(1,37)}$num${cc(0)} ${cc(1,33)}\\].*", add _)
      Alias.add("^mobs list$", regexes.foreach(Profile.info))
      Alias.add("^mobs$", Profile.info(s"${regexes.size} mob strings"))
      Alias.add("^mobs clear$", regexes.clear)
      Alias.add("^mobs regexes$", regexes.map(toRegex).foreach(Profile.info))
      Alias.add("^mobs write$", write)
    })
  }
}
