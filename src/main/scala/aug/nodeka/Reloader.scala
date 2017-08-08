package aug.nodeka

import java.lang.reflect.Field

import aug.script.framework.ReloadData

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.{Failure, Try}

object Reloader {
  import scala.reflect.runtime.universe
  val runtimeMirror = universe.runtimeMirror(getClass.getClassLoader)

  def getStaticObj(s: String): Any = {
    val module = runtimeMirror.staticModule(s)
    runtimeMirror.reflectModule(module).instance
  }

  def load(reloadData: ReloadData, objs: List[AnyRef]): Unit = {
    def get(obj: AnyRef, f: Field): Option[String] = {
      val k = s"${obj.getClass.getName}#${f.getName}"
      if(!reloadData.data.containsKey(k)) {
        None
      } else if (reloadData.data.get(k) == "") {
        None
      } else Some(reloadData.data.get(k))
    }

    withFields(objs, (rl: AnyRef, f: Field) => {
      val t: AnyRef = f.get(rl)
      t match {
        case s: mutable.Queue[_] =>
          get(rl, f).foreach(s=> f.set(rl, mutable.Queue[String](s.split("#"):_*)))
        case s: mutable.HashSet[_] =>
          get(rl, f).foreach(s=> f.set(rl, mutable.HashSet[String](s.split("#"):_*)))
        case l: ListBuffer[_] =>
          get(rl, f).foreach(s=> f.set(rl, ListBuffer[String](s.split("#"):_*)))
        case i: Integer =>
          get(rl, f).foreach(s=> f.set(rl, s.toInt))
        case l: java.lang.Long =>
          get(rl, f).foreach(s=> f.set(rl, s.toLong))
        case s: String =>
          get(rl, f).foreach(s=> f.set(rl, s))
        case b: java.lang.Boolean =>
          get(rl, f).foreach(s=> f.set(rl, s.toBoolean))
        case s: RunState =>
          get(rl, f).foreach(s=> f.set(rl, getStaticObj(s)))
        case char: BaseChar =>
          get(rl, f).foreach(s=> f.set(rl, Player.chars(s)))
        case _ =>
          Profile.error(s"${f.getName} is unsupported @reload class: ${t.getClass}!")
      }
    })
  }

  def save(reloadData: ReloadData, objs: List[AnyRef]) = {

    def put(obj: AnyRef, f: Field, s: String) = {
      reloadData.data.put(s"${obj.getClass.getName}#${f.getName}", s)
    }

    withFields(objs, (rl: AnyRef, f: Field) => {
      val t: AnyRef = f.get(rl)
      t match {
        case s: mutable.Queue[_] =>
          put(rl, f, s.map(_.toString).mkString("#"))
        case s: mutable.HashSet[_] =>
          put(rl, f, s.map(_.toString).mkString("#"))
        case l: ListBuffer[_] =>
          put(rl, f, l.map(_.toString).mkString("#"))
        case i: Integer =>
          put(rl, f, i.toString)
        case l: java.lang.Long =>
          put(rl, f, l.toString)
        case s: String =>
          put(rl, f, s)
        case b: java.lang.Boolean =>
          put(rl, f, b.toString)
        case s: RunState =>
          put(rl, f, s.getClass.getName)
        case char: BaseChar =>
          put(rl, f, char.name)
        case _ =>
          Profile.error(s"${f.getName} is unsupported @reload class: ${t.getClass}!")
      }
    })
  }

  private def withFields(objs: List[AnyRef], funk: (AnyRef, Field) => Unit): Unit = {
    objs.foreach(rl => {
      rl.getClass.getDeclaredFields.foreach(f => {
        if (f.getAnnotation(classOf[Reload]) != null) {
          Try {
            val access = f.isAccessible
            if (!access) f.setAccessible(true)
            funk(rl, f)
            if (!access) f.setAccessible(false)
          } match {
            case Failure(e) =>
              Profile.error("err: "+ e.getMessage)
              e.printStackTrace()
            case _ =>
          }
        }
      })
    })
  }
}