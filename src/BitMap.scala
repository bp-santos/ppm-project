import QTree.Coords
import java.awt.Color

trait BitMap[+A]

case object BitEmpty extends BitMap[Nothing]

case class ColorMap[A](value: Array[Array[A]]) extends BitMap[A]

object BitMap {
  /*def makeBitMap(qt: QTree[Coords]): BitMap[Int] = {
    def aux():List[List[Int]] {
      qt match {
        case QEmpty => Nil
        case QLeaf(((x1,x2),(y1,y2)),color) => makeList
        case QNode(value, l1, l2, l3, l4) => aux(l1)::aux(l2)::aux(l3)::aux(l4)
      }
    }
  toBitMap()
  }*/

  //FAZER ISTO COM LIST LIST INT SENAO NAO VAI DAR
  /*def toBitMap (lst:List[List[Int]]): BitMap[Int] = {
    if (lst.length == 0 || lst == null) BitEmpty
    else (lst map (_.toArray)) toArray
  }*/

/*
  /** Creation of a BitMap from a given QTree. */
  def makeBitMap(qt: ,s:Int[Coords]): BitMap[Int] = {
  qt match {
    case QLeaf((cd: Coords, color: Color)) => QTree.toList(paintCords(cd, color))
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