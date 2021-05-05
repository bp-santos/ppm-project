import QTree.Coords
import java.awt.Color

case class BitMap(value: Array[Array[Int]])

object BitMap {

  def makeBitMap(qt: QTree[Coords]): BitMap = {

    def aux(qt1: QTree[Coords]): List[List[Int]] = {
      qt1 match {
        case QEmpty => Nil
        case QLeaf((coords: Coords, color: Color)) => makeListY(coords, color)
        case QNode(_, l1, l2, l3, l4) =>
          mergeY(mergeX(aux(l1), aux(l2)), mergeX(aux(l3), aux(l4)))
      }
    }

    BitMap(toArray(aux(qt)))
  }

  def toArray(lst: List[List[Int]]): Array[Array[Int]] = {
    if (lst.isEmpty || lst == null) Array()
    else (lst map (_.toArray)) toArray
  }

  def mergeX(l1: List[List[Int]], l2: List[List[Int]]): List[List[Int]] = {
    def aux(acc: Int, size: Int): List[List[Int]] = {
      if (l1.isEmpty) l2
      else if (l2.isEmpty) l1
      else if (acc == size - 1)
        l1(acc) ++ l2(acc) :: Nil
       else
        l1(acc) ++ l2(acc) :: aux(acc + 1, size)
    }

    aux(0, l1.size)
  }

  def mergeY(firstHalf: List[List[Int]], secondHalf: List[List[Int]]): List[List[Int]] = {
    (firstHalf foldRight secondHalf) ((x, xs) => x :: xs)
  }

  def makeListY(coords: Coords, c: Color): List[List[Int]] = {
    val width = coords._2._1 - coords._1._1
    val height = coords._2._2 - coords._1._2

    def aux(h: Int): List[List[Int]] = {
      h match {
        case 1 => List(makeListX(width, c))
        case _ => makeListX(width, c) :: aux(h - 1)
      }
    }

    aux(height)
  }

  def makeListX(size: Int, c: Color): List[Int] = {
    size match{
      case 1 => List(encodeRGB(c))
      case _ => encodeRGB(c) :: makeListX(size - 1, c)
    }
  }

  def encodeRGB(c: Color): Int = ImageUtil.encodeRgb(c.getRed, c.getGreen, c.getBlue)
}
