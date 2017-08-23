package aug.nodeka

object Nodeka {

  def enhanceCost(level: Int, remort: Int = 3): Option[Int] = {
    if (level >= 100 && level < 200) {
      Some(2*level + 450)
    } else if (level >= 200 && level < 270) {
      Some(3*level + 450)
    } else None
  }



}
