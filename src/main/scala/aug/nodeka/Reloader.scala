package aug.nodeka

import java.lang.reflect.Field

import aug.script.shared.ReloadData

import scala.collection.mutable
import scala.util.{Failure, Try}

object Reloader {
  var client : NodekaClient = null

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
        case s: mutable.HashSet[_] =>
          get(rl, f).foreach(s=> f.set(rl, mutable.HashSet[String](s.split("#"):_*)))
        case i: Integer =>
          get(rl, f).foreach(s=> f.set(rl, s.toInt))
        case s: String =>
          get(rl, f).foreach(s=> f.set(rl, s))
        case _ =>
          client.metric.echo(s"${f.getName} is unsupported @reload class: ${t.getClass}!")
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
              client.metric.echo("err: "+ e.getMessage)
              e.printStackTrace()
            case _ =>
          }
        }
      })
    })
  }

  def save(reloadData: ReloadData, objs: List[AnyRef]) = {

    def put(obj: AnyRef, f: Field, s: String) = {
      reloadData.data.put(s"${obj.getClass.getName}#${f.getName}", s)
    }

    withFields(objs, (rl: AnyRef, f: Field) => {
      val t: AnyRef = f.get(rl)
      t match {
        case s: mutable.HashSet[_] =>
          put(rl, f, s.map(_.toString).mkString("#"))
        case i: Integer =>
          put(rl, f, i.toString)
        case s: String =>
          put(rl, f, s)
        case _ =>
          client.metric.echo(s"${f.getName} is unsupported @reload class: ${t.getClass}!")
      }
    })
  }
}