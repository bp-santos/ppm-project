import QTree.Coords
import java.awt.Color

trait BitMap[+A]

case object BitEmpty extends BitMap[Nothing]

case class ColorMap[A](value: Array[Array[A]]) extends BitMap[A]

object BitMap {
  /** Creation of a BitMap from a given QTree. */
  /*def makeBitMap(qt: QTree[Coords]): BitMap[Int] = {
    qt match {
      case QLeaf((cd: Coords, color: Color)) => paintCords(cd, color)
      case QNode(_, one, two, three, four) => makeBitMap(one) :: makeBitMap(two) :: makeBitMap(three) :: makeBitMap(four)
      case _ => BitEmpty
    }
  }

  private def paintCords(cd: Coords, color: Color): BitMap[Int] = {
    val arr: BitMap[Int]
    val a = arr(cd._1._1)(cd._1._2) = ImageUtil.encodeRgb(color.getRed(), color.getGreen(), color.getBlue())
    val a1 = arr(cd._2._1)(cd._2._2) = ImageUtil.encodeRgb(color.getRed(), color.getGreen(), color.getBlue())
    arr
  }*/
}