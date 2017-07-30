package aug.nodeka

import java.io.Closeable
import java.util.regex.Pattern

import scala.util.{Failure, Try}
import scala.util.control.NonFatal

object TryWith {
  def apply[C <: Closeable, R](resource: => C)(f: C => R): Try[R] =
    Try(resource).flatMap(resourceInstance => {
      try {
        val returnValue = f(resourceInstance)
        Try(resourceInstance.close).map(_ => returnValue)
      }  catch {
        case NonFatal(exceptionInFunction) =>
          try {
            resourceInstance.close
            Failure(exceptionInFunction)
          } catch {
            case NonFatal(exceptionInClose) =>
              exceptionInFunction.addSuppressed(exceptionInClose)
              Failure(exceptionInFunction)
          }
      }
    })
}

object Util {
  def removeColors(string: String): String = string.replaceAll("\u001B\\[.*?m", "")
  def removeEscape(string: String): String = string.replaceAll("\u001B", "")
  def stripCommas(string: String): String = string.replaceAll(",", "")
  def cc(codes: Int*) = Pattern.quote("" + 27.toByte.toChar + "[" + codes.mkString(";") + "m")
  val num = "[1-9][0-9]{0,}"
  val ecl = Pattern.quote("!")
}