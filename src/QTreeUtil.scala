import java.awt.Color
import scala.annotation.tailrec

case class QTreeUtil[A](qt: QTree[A]){
  def scale(scale:Double):QTree[A] = QTreeUtil.scale(scale,this.qt)
  def mirrorV():QTree[A] = QTreeUtil.mirrorV(this.qt)
  def mirrorH():QTree[A] = QTreeUtil.mirrorH(this.qt)
  def rotateL():QTree[A] = QTreeUtil.rotateL(this.qt)
  def rotateR():QTree[A] = QTreeUtil.rotateR(this.qt)
  def mapColorEffect(f:Color => Color):QTree[A] = QTreeUtil.mapColorEffect(f,this.qt)
  def noise(c: Color):Color = QTreeUtil.noise(c)
  def contrast(c: Color):Color = QTreeUtil.contrast(c)
  def sepia(c: Color):Color = QTreeUtil.sepia(c)
}

object QTreeUtil {

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  private def toList[A](matrix:Array[Array[A]]):List[List[A]] = {
    if (matrix.length == 0 || matrix == null) Nil
    else (matrix map (_.toList)) toList
  }

  private def equalColorList(lst: List[Int]) : Boolean = lst.distinct.length == 1

  @tailrec
  private def equalColor(matrix:List[List[Int]]) : Boolean = {
    matrix match {
      case Nil => true
      case x::xs => equalColorList(x) && equalColor(xs)
    }
  }

  //quando temos apenas um pixel sabemos que é uma folha
  //pensar para quando é impar (somos nos a decidir qual dos quadrantes o maior)
  //qnode com 2 subarvores tem de ter dois empty
  //nao percorrer varias vezes pixeis ja percorridos
  private def convertToQuadrants[A](matrix:List[List[Int]], cords: Coords):QTree[Coords] = {
    val ini_x = cords._1._1
    val ini_y = cords._1._2
    val width = cords._2._1
    val height = cords._2._2
    val medium_width = width/2
    val medium_height = height/2

    val matrixToAnalyze = matrix.map()

    matrix match{
      case Nil => QEmpty
      case _ =>
        if (equalColor(matrix)) QLeaf((cords, ImageUtil.decodeRgb(matrix.head.head)))
        else /*if (width % 2 == 0 && height % 2 == 0) {*/
          QNode(cords, convertToQuadrants(matrix, (cords._1, (medium_width, medium_height))),
            convertToQuadrants(matrix, ((medium_width, ini_y), (width, medium_height))),
            convertToQuadrants(matrix, ((ini_x, medium_height), (medium_width, height))),
            convertToQuadrants(matrix, ((medium_width, medium_height), cords._2)))
      //else{
      //  QNode(cords, convertToQuadrants(matrix, ((,),(,)) ), convertToQuadrants(matrix, ((,),(,)) ), convertToQuadrants(matrix, ((,),(,)) ), convertToQuadrants(matrix, ((,),(,)) ))
      //}
    }
  }

  /** Creation of a QTree from a given bitmap. */
  def makeQTree[A](b:Array[Array[Int]] /*b:BitMap*/):QTree[Coords] = {
    val matrix = toList(b /*b.value*/)
    val width = matrix.head.length-1
    val height = matrix.length-1
    convertToQuadrants(matrix,((0,0),(width,height)))
  }

  /** Creation of a BitMap from a given QTree. */
  //percorrer, cda vez que encontra uma folha retira as coordenadas e depois pinta da mesma cor
  //matriz imutavel
  //def makeBitMap[A](qt:QTree[A]):BitMap = {}

  /** Magnification/reduction operation on an image, according to the factor provided. */
  //ajustamos as coordenadas mas nao fazemos nada sobre a cor mas em algumas situações necessário (média da cor dos pixeis de grande para pequeno ou gradiente de pequeno para grande)
  def scale[A](sc: Double, qt: QTree[A]): QTree[A] = {
    qt match {
      case QNode(value, one, two, three, four) =>
        QNode(value, scale(sc, one), scale(sc, two), scale(sc, three), scale(sc, four))
      case QLeaf((cd: Coords, color)) =>
        QLeaf(((cd._1._1 * sc.round.toInt, cd._1._2 * sc.round.toInt),
          (cd._2._1 * sc.round.toInt, cd._2._2 * sc.round.toInt)), color)
      case _ => QEmpty
    }
  }

  /** Given a QTree it calculates the max coordinate value. */
  private def maxTreeCord[A](qt: QTree[A], value: Int): Int = {
    qt match {
      case QLeaf((cd: Coords, _)) =>
        if (value == 1) cd._2._1
        else cd._2._2
      case QNode(cd: Coords, _, _, _, _) =>
        if (value == 1) cd._2._1
        else cd._2._2
      case _ => 0
    }
  }

  /** Vertical mirroring operation. */
  def mirrorV[A](qt: QTree[A]): QTree[A] = {
    val max = maxTreeCord(qt, 1)
    def aux(qt: QTree[A], max: Int): QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(two, max), aux(one, max), aux(four, max), aux(three, max))
        case QLeaf((cd: Coords, color)) =>
          QLeaf(new Coords((max - cd._2._1, cd._1._2), (max - cd._1._1, cd._2._2)), color)
        case _ => QEmpty
      }
    }
    aux(qt, max)
  }

  //fazer tambem para quando os quadrantes sao de tamanho diferente
  /** Horizontal mirroring operation. */
  def mirrorH[A](qt: QTree[A]): QTree[A] = {
    val max = maxTreeCord(qt, 0)
    def aux(qt: QTree[A], max: Int): QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(three, max), aux(four, max), aux(one, max), aux(two, max))
        case QLeaf((cd: Coords, color)) =>
          QLeaf(new Coords((cd._1._1, max - cd._2._2), (cd._2._1, max - cd._1._2)), color)
        case _ => QEmpty
      }
    }
    aux(qt, max)
  }

  /** 90 degree rotation to the left. */
  def rotateL[A](qt:QTree[A]):QTree[A] = {
    val max_x = maxTreeCord(qt,1)
    def aux (qt:QTree[A],max_x:Int):QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(two,max_x), aux(four,max_x), aux(one,max_x), aux(three,max_x))
        case QLeaf((cd: Coords, color)) =>
          QLeaf(new Coords ((cd._1._2,max_x - cd._2._1),(cd._2._2,max_x - cd._1._1)), color)
        case _ => QEmpty
      }
    }
    aux(qt,max_x)
  }

  /** 90 degree rotation to the right. */
  def rotateR[A](qt:QTree[A]):QTree[A] = {
   val max_y = maxTreeCord(qt,0)
   def aux (qt:QTree[A],max_y:Int):QTree[A] = {
     qt match {
       case QNode(value, one, two, three, four) =>
          QNode(value, aux(three,max_y), aux(one,max_y), aux(four,max_y), aux(two,max_y))
       case QLeaf((cd: Coords, color)) =>
          QLeaf(new Coords ((max_y - cd._2._2,cd._1._1),(max_y - cd._1._2,cd._2._1)), color)
       case _ => QEmpty
     }
   }
   aux(qt,max_y)
 }

  /** Checks whether the given value is valid as an RGB component. */
  private def colorComponentInRange(component: Int): Int = {
    if (component > 255) 255
    else if (component < 0) 0
    else component
  }

  /** Change the color components given according to a provided factor. */
  private def changeColor(c: Color, value: Double): Color = {
    val r = colorComponentInRange((c.getRed * value).toInt)
    val g = colorComponentInRange((c.getGreen * value).toInt)
    val b = colorComponentInRange((c.getBlue * value).toInt)
    new Color(r, g, b)
  }

  /** Uniform mapping of a function onto the entire image. */
  def mapColorEffect[A](f: Color => Color, qt: QTree[A]): QTree[A] = {
    qt match {
      case QNode(value, one, two, three, four) =>
        QNode(value, mapColorEffect(f, three), mapColorEffect(f, one), mapColorEffect(f, four), mapColorEffect(f, two))
      case QLeaf((value, color: Color)) => QLeaf((value, f(color)))
      case _ => QEmpty
    }
  }

  /** Obtains the noise value of a RGB color. */
    //fazer random puro
  def noise(c: Color): Color = {
    val random = new scala.util.Random
    val noise = random.nextFloat
    if (noise < 0.5) Color.black
    else c
  }

  /** Obtains the contrast of a RGB color. */
  def contrast(c: Color): Color = {
    val red = c.getRed
    val green = c.getGreen
    val blue = c.getBlue
    val luminance = ImageUtil.luminance(red, green, blue)
    if (luminance < 128) changeColor(c, 0.8)
    else changeColor(c, 1.2)
  }

  /** Obtains the sepia of a RGB color. */
  def sepia(c: Color): Color = {
    val avg = (c.getRed + c.getGreen + c.getBlue) / 3
    val depth = 20
    val intensity = 30
    val red = colorComponentInRange(avg + (2 * depth))
    val green = colorComponentInRange(avg + depth)
    val blue = colorComponentInRange(c.getBlue - intensity)
    new Color(red, green, blue)
  }

}