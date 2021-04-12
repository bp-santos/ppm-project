import java.awt.Color

/*sealed*/ trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

case class BitMap(value: Array[Array[Int]])

case class Gallery[A](myField: QTree[A]){
  //def makeQTree(b:BitMap):QTree[A] = Gallery.makeQTree(b)
  //def makeBitMap():BitMap = Gallery.makeBitMap(this.myField)
  def scale(scale:Double):QTree[A] = Gallery.scale(scale,this.myField)
  def maxTreeCoord[A](qt:QTree[A]):Int = Gallery.maxTreeCoord(this.myField)
  def mirrorV():QTree[A] = Gallery.mirrorV(this.myField)
  def mirrorH():QTree[A] = Gallery.mirrorH(this.myField)
  //def rotateL():QTree[A] = Gallery.rotateL(this.myField)
  //def rotateR():QTree[A] = Gallery.rotateR(this.myField)*/
  def colorComponentInRange(component: Int):Int = Gallery.colorComponentInRange(component)
  def sepia(c: Color):Color = Gallery.sepia(c)
  def noise(c: Color):Color = Gallery.noise(c)
  def changeColor(c: Color, value:Double):Color = Gallery.changeColor(c,value)
  def contrast(c: Color):Color = Gallery.contrast(c)
  def mapColorEffect(f:Color => Color):QTree[A] = Gallery.mapColorEffect(f,this.myField)
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
  def scale[A](sc:Double, qt:QTree[A]):QTree[A] = {
    qt match{
      case QNode(value, one, two, three, four) =>
        QNode(value, scale(sc,one), scale(sc,two), scale(sc,three), scale(sc,four))
      case QLeaf((cd: Coords, color)) =>
        QLeaf(((cd._1._1*sc.round.toInt,cd._1._2*sc.round.toInt),
          (cd._2._1*sc.round.toInt,cd._2._2*sc.round.toInt)),color)
      case _ => QEmpty
    }
  }

  /** Given a QTree it calculates the max coordinate value. */
  def maxTreeCoord[A](qt:QTree[A],value:Int):Int = {
    qt match {
      case QLeaf((cd:Coords,color)) =>
        if(value == 1 ) cd._2._1
        else cd._2._2
      case QNode(cd:Coords, _, _, _, _) =>
        if (value == 1 ) cd._2._1
        else cd._2._2
      case _ => 0
    }
  }

  /** Vertical mirroring operation. */
  def mirrorV[A](qt:QTree[A]):QTree[A] = {
    val max = maxTreeCoord(qt,1)
    def aux[A](qt:QTree[A],max:Int):QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(two,max), aux(one,max), aux(four,max), aux(three,max))
        case QLeaf((cd:Coords, color)) =>
          QLeaf(new Coords ((max - cd._2._1,cd._1._2),(max - cd._1._1, cd._2._2)), color)
        case _ => QEmpty
      }
    }
    aux(qt,max)
  }

  /** Horizontal mirroring operation. */
  def mirrorH[A](qt:QTree[A]):QTree[A] = {
    val max = maxTreeCoord(qt,0)
    def aux (qt:QTree[A],max:Int):QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(three,max), aux(four,max), aux(one,max), aux(two,max))
        case QLeaf((cd: Coords, color)) =>
          QLeaf(new Coords ((cd._1._1,max - cd._2._2),(cd._2._1, max - cd._1._2)), color)
        case _ => QEmpty
      }
    }
    aux(qt,max)
  }

  /** 90 degree rotation to the left. */
  //def rotateL[A](qt:QTree[A]):QTree[A] = {}

  /** 90 degree rotation to the right. */
  //def rotateR[A](qt:QTree[A]):QTree[A] = {}

  /** Checks whether the given value is valid as an RGB component. */
  def colorComponentInRange(component: Int):Int = {
    if (component > 255) 255
    else if (component < 0) 0
    else component
  }

  /** Obtains the sepia of a RGB color. */
  def sepia(c: Color):Color = {
    val avg = (c.getRed+c.getGreen+c.getBlue)/3
    val depth = 20
    val intensity = 30
    val red = colorComponentInRange(avg+(2*depth))
    val green = colorComponentInRange(avg+depth)
    val blue  = colorComponentInRange(c.getBlue-intensity)
    new Color (red,green,blue)
  }

  /** Obtains the noise value of a RGB color. */
  def noise(c: Color):Color = {
    val random = new scala.util.Random
    val noise = random.nextFloat
    if (noise < 0.01) Color.white //pq branco e nao preto
    else c
  }

  /** Change the color components given according to a provided factor. */
  def changeColor(c: Color, value:Double):Color = {
    val r = colorComponentInRange((c.getRed * value).toInt)
    val g = colorComponentInRange((c.getGreen * value).toInt)
    val b = colorComponentInRange((c.getBlue * value).toInt)
    new Color (r,g,b)
  }

  /** Obtains the contrast of a RGB color. */
  def contrast(c: Color):Color = {
    val red = c.getRed
    val green = c.getGreen
    val blue = c.getBlue
    val luminance = ImageUtil.luminance(red,green,blue)
    if (luminance < 128) changeColor(c,0.8)
    else changeColor(c,1.2)
  }

  /** Uniform mapping of a function onto the entire image. */
  def mapColorEffect[A](f:Color => Color, qt:QTree[A]):QTree[A] = {
    qt match{
      case QNode(value, one, two, three, four) =>
        QNode(value, mapColorEffect(f,three), mapColorEffect(f,one), mapColorEffect(f,four), mapColorEffect(f,two))
      case QLeaf((value,color: Color)) => QLeaf((value,f(color)))
      case _ => QEmpty
    }
  }
}