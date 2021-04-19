import java.awt.Color

/*sealed*/ trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

case class BitMap(value: Array[Array[Int]])

case class Album[A](name: String, content:List[QTree[A]])

case class Gallery[A](qt: QTree[A]){
  //def makeQTree(b:BitMap):QTree[A] = Gallery.makeQTree(b)
  //def makeBitMap():BitMap = Gallery.makeBitMap(this.qt)
  def scale(scale:Double):QTree[A] = Gallery.scale(scale,this.qt)
  def maxTreeCord(value:Int):Int = Gallery.maxTreeCord(this.qt,value)
  def mirrorV():QTree[A] = Gallery.mirrorV(this.qt)
  def mirrorH():QTree[A] = Gallery.mirrorH(this.qt)
  def rotateL():QTree[A] = Gallery.rotateL(this.qt)
  def rotateR():QTree[A] = Gallery.rotateR(this.qt)
  def colorComponentInRange(component: Int):Int = Gallery.colorComponentInRange(component)
  def changeColor(c: Color, value:Double):Color = Gallery.changeColor(c,value)
  def mapColorEffect(f:Color => Color):QTree[A] = Gallery.mapColorEffect(f,this.qt)
  def noise(c: Color):Color = Gallery.noise(c)
  def contrast(c: Color):Color = Gallery.contrast(c)
  def sepia(c: Color):Color = Gallery.sepia(c)
}

object Gallery {

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  //converter array de array em listas de listas

  //dividir a matriz em quadrados(função) dada uma matriz saber qual o conteudo de cada quadrante
  //função que diga quais sao as coordenadas dos quatro quadrantes
  //recursividade até ser folha ou vazio
  //quando temos apenas um pixel sabemos que é uma folha
  //quanto temos todos os pixeis do quadrante com a mesma cor (função auxiliar) verificar se sao todos da mesma cor (criar qnode ou qleaf)
  //pensar para quando é impar (somos nos a decidir qual dos quadrantes o maior) ->
  //qnode com 2 subarvores tem de ter dois empty

  /*def toList[A](matrix:Array[Array[A]]):List[List[A]] = {
    if (matrix.length == 0 || matrix == null) Nil
    else (matrix map (_.toList)) toList
  }

  def same(x1: Int, x2: Int):Boolean = x1.equals(x2)

  def notSame( lst: List[Int]) : Boolean = { (lst foldRight true) ((m1,m2,lst)=> !same(m1) && lst) }

  def equalColor(matrix:List[List[Int]]):Boolean = {
    val color = matrix(0)(0)
    matrix match {
      case  List(x) => {
        (x foldRight true) ( (e1, e2) ) =>
          if ( ImageUtil.decodeRgb(e1) != ImageUtil.decodeRgb(e2)) false
      }
    }
    true
  }

  def convertToQuadrant(matrix:List[List[Int]]):List[List[Int]] = {
    val max = matrix.length
    val avg = max/2
    matrix match {
     case h::t =>  {
        if (h(0) == avg)
      }
      case _ => Nil
    }
  }*/

  /** Creation of a QTree from a given bitmap. */
  /*def makeQTree[A](b:BitMap):QTree[A] = {
    val matrix = toList(b.value)
    matrix match {
      case Nil => QEmpty
      case h::t =>
    }
  }*/

  //percorrer, cda vez que encontra uma folha retira as coordenadas e depois pinta da mesma cor
  //matriz imutavel

  /** Creation of a BitMap from a given QTree. */
  //def makeBitMap[A](qt:QTree[A]):BitMap = {}

  /** Magnification/reduction operation on an image, according to the factor provided. */
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
  def maxTreeCord[A](qt: QTree[A], value: Int): Int = {
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
  def colorComponentInRange(component: Int): Int = {
    if (component > 255) 255
    else if (component < 0) 0
    else component
  }

  /** Change the color components given according to a provided factor. */
  def changeColor(c: Color, value: Double): Color = {
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

  /** ...................................................................................................... */

  def addPhoto[A](qt: QTree[A], album: Album[A]): Option[Album[A]] = {
    if (album.content == Nil) None
    else if (album.content.contains(qt)) Some(album)
    else Some(Album(album.name, qt :: album.content))
  }

  def removePhoto[A](index: Int, album: Album[A]): Option[Album[A]] = {
    if (album.content == Nil || album.content.isEmpty || (index - 1) > album.content.length) None
    else Some(Album(album.name, album.content.take(index) ++ album.content.drop(index + 1)))
  }

  //def scrollAlbum[A](album: Album[A]):Option[Album[A]] = {}

  def findPhoto[A](index:Int, album: Album[A]):Option[QTree[A]] = {
    if (album.content == Nil || album.content.isEmpty || (index-1) > album.content.length) None
    else Some(album.content.apply(index-1))
  }

  def changeAlbumOrder[A](f:Album[A] => Album[A], album: Album[A]):Option[Album[A]] = {
    if (album.content == Nil || album.content.isEmpty) None
    else Some(f(album))
  }

  def reverse[A](album:Album[A]):Option[Album[A]] = {
    Some(Album(album.name, album.content.reverse))
  }

  def changeAlbumInfo[A](newName: String, album: Album[A]):Option[Album[A]] = {
    if (newName.isEmpty) Some(album)
    else Some(Album(newName,album.content))
  }
}