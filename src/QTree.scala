import java.awt.Color
import scala.annotation.tailrec

trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

object QTree {

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  /** Creation of a QTree from a given bitmap. */
  def makeQTree(b: BitMap): QTree[Coords] = {
    b match {
      case BitMap(value) =>
        val matrix = toList(value)
        val width = matrix.head.length
        val height = matrix.length
        convertToQuadrants(matrix, ((0, 0), (width, height)))
      case _ => QEmpty
    }
  }

  def toList[A](matrix: Array[Array[A]]): List[List[A]] = {
    if (matrix.length == 0 || matrix == null) Nil
    else (matrix map (_.toList)) toList
  }

  private def convertToQuadrants(matrix: List[List[Int]], cords: Coords): QTree[Coords] = {
    val ini_x = cords._1._1
    val ini_y = cords._1._2
    val width = cords._2._1
    val height = cords._2._2
    val medium_width = (width + ini_x) / 2
    val medium_height = (height + ini_y) / 2
    matrix match {
      case Nil => QEmpty
      case _ =>
        val quadrant = getQuadrant(matrix, cords)
        quadrant match {
          case Nil => QEmpty
          case _ =>
            if (equalColor(quadrant, quadrant.head)) {
              val c = ImageUtil.decodeRgb(quadrant.head).toList
              QLeaf((cords, new Color(c.head, c(1), c(2))))
            }
            else {
              if ((width - ini_x) % 2 == 0 && (height - ini_y) % 2 == 0)
                QNode(cords, convertToQuadrants(matrix, ((ini_x, ini_y), (medium_width, medium_height))),
                  convertToQuadrants(matrix, ((medium_width, ini_y), (width, medium_height))),
                  convertToQuadrants(matrix, ((ini_x, medium_height), (medium_width, height))),
                  convertToQuadrants(matrix, ((medium_width, medium_height), (width, height))))
              else if ((width - ini_x) % 2 != 0 && (height - ini_y) % 2 == 0)
                QNode(cords, convertToQuadrants(matrix, ((ini_x, ini_y), (medium_width + 1, medium_height))),
                  convertToQuadrants(matrix, ((medium_width + 1, ini_y), (width, medium_height))),
                  convertToQuadrants(matrix, ((ini_x, medium_height), (medium_width + 1, height))),
                  convertToQuadrants(matrix, ((medium_width + 1, medium_height), (width, height))))
              else if ((width - ini_x) % 2 == 0 && (height - ini_y) % 2 != 0)
                QNode(cords, convertToQuadrants(matrix, ((ini_x, ini_y), (medium_width, medium_height + 1))),
                  convertToQuadrants(matrix, ((medium_width, ini_y), (width, medium_height + 1))),
                  convertToQuadrants(matrix, ((ini_x, medium_height + 1), (medium_width, height))),
                  convertToQuadrants(matrix, ((medium_width, medium_height + 1), (width, height))))
              else {
                QNode(cords, convertToQuadrants(matrix, ((ini_x, ini_y), (medium_width + 1, medium_height + 1))),
                  convertToQuadrants(matrix, ((medium_width + 1, ini_y), (width, medium_height + 1))),
                  convertToQuadrants(matrix, ((ini_x, medium_height + 1), (medium_width + 1, height))),
                  convertToQuadrants(matrix, ((medium_width + 1, medium_height + 1), (width, height))))
              }
            }
        }
    }
  }

  private def getQuadrant(matrix: List[List[Int]], cords: Coords): List[Int] = {
    val quadrant = matrix.splitAt(cords._1._2)._2.splitAt(cords._2._2 - cords._1._2)._1
      .map(_.splitAt(cords._1._1)._2.splitAt(cords._2._1 - cords._1._1)._1)
    concatMultiDimensionalList(quadrant)
  }

  @tailrec
  private def equalColor(lst: List[Int], c: Int): Boolean = {
    lst match {
      case Nil => true
      case x :: xs => if (x.equals(c)) equalColor(xs, c) else false
    }
  }

  private def concatMultiDimensionalList(matrix: List[List[Int]]): List[Int] = {
    matrix match {
      case Nil => Nil
      case x :: xs => x ::: concatMultiDimensionalList(xs)
    }
  }
}