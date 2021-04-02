import sun.jvm.hotspot.utilities.BitMap
import java.awt.Color

/*sealed*/ trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

case class Example[A](myField: QTree[A]){
  def makeQTree(b:BitMap):QTree[A] = Example.makeQTree(b)
  def makeBitMap():BitMap = Example.makeBitMap(this.myField)
  def scale(scale:Double):QTree[A] = Example.scale(scale,this.myField)
  def mirrorV():QTree[A] = Example.mirrorV(this.myField)
  def mirrorH():QTree[A] = Example.mirrorH(this.myField)
  def rotateL():QTree[A] = Example.rotateL(this.myField)
  def rotateR():QTree[A] = Example.rotateR(this.myField)
  def mapColourEffect(f:Color => Color):QTree[A] = Example.mapColourEffect(f,this.myField)
}

object Example{

  def makeQTree[A](b:BitMap):QTree[A] = {

  }

  def makeBitMap[A](qt:QTree[A]):BitMap = {

  }

  def scale[A](scale:Double, qt:QTree[A]):QTree[A] = {

  }

  def mirrorV[A](qt:QTree[A]):QTree[A] = {

  }

  def mirrorH[A](qt:QTree[A]):QTree[A] = {

  }

  def rotateL[A](qt:QTree[A]):QTree[A] = {

  }

  def rotateR[A](qt:QTree[A]):QTree[A] = {

  }

  def mapColourEffect[A](f:Color => Color, qt:QTree[A]):QTree[A] = {

  }

  def main(args: Array[String]): Unit = {

    type Point = (Int, Int)
    type Coords = (Point, Point)
    type Section = (Coords, Color)

    val l1: QLeaf[Coords, Section] = QLeaf((((0,0):Point,(0,0):Point):Coords, Color.red):Section)
    val l2: QLeaf[Coords, Section] = QLeaf((((1,0):Point,(1,0):Point):Coords, Color.blue):Section)
    val l3: QLeaf[Coords, Section] = QLeaf((((0,1):Point,(0,1):Point):Coords, Color.yellow):Section)
    val l4: QLeaf[Coords, Section] = QLeaf((((1,1):Point,(1,1):Point):Coords, Color.green):Section)

    val qt: QTree[Coords] = QNode(((0,0),(1,1)), l1, l2, l3, l4)
    println(qt)
  }

}