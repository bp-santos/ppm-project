import java.awt.Color

/*sealed*/ trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

case class BitMap(value: List[List[Int]]) {

  def imgToBitmap(arr: Array[Array[Int]]): Unit = {

    imil match {
      case Nil => Nil
      case List(List(x)) => {
        val a = ImageUtil.decodeRgb(x)

      }
    }
    ImageUtil.writeImage(imiarr, "src/objc2_test.png", "png")
  }
}

// readColorImage -> ler matriz -> decode recursively -> transformar arrays em listas
//lista de integer ? ou Array?
//lista de lista de 3 inteiros ou apenas 1

case class Gallery[A](myField: QTree[A]){
  //def makeQTree(b:BitMap):QTree[A] = Gallery.makeQTree(b)
  //def makeBitMap():BitMap = Gallery.makeBitMap(this.myField)
  def scale(scale:Double):QTree[A] = Gallery.scale(scale,this.myField)
  def mirrorV():QTree[A] = Gallery.mirrorV(this.myField)
  def mirrorH():QTree[A] = Gallery.mirrorH(this.myField)
  //def rotateL():QTree[A] = Gallery.rotateL(this.myField)
  //def rotateR():QTree[A] = Gallery.rotateR(this.myField)*/
  def noise(c: Color):Color = Gallery.noise(c)
  def sepia(c: Color):Color = Gallery.sepia(c)
  //def contrast(c: Color):Color = Gallery.contrast(c)
  def mapColorEffect(f:Color => Color):QTree[A] = Gallery.mapColorEffect(f,this.myField)
}


object Gallery {

  type Point = (Int, Int)
  type Coords = (Point, Point)
  type Section = (Coords, Color)

  /*def makeQTree[A](b:BitMap):QTree[A] = {
    b.value match{
      case Nil => QEmpty
    }
  }*/

  //Coordenadas
  //(0,0) (1,0)
  //(0,1) (1,1)

  def findCoord(l: Int, c: Int):Int = sqrt(l^2 - c^2).toInt

  //converter array de array em listas de listas
  //dividir a matriz em quadrados(função) dada uma matriz saber qual o conteudo de cada quadrante
  //função que diga quais sao as coordenadas dos quatro quadrantes
  //recursividade até ser folha ou vazio
  //quando temos apenas um pixel sabemos que é uma folha
  //quanto temos todos os pixeis do quadrante com a mesma cor (função auxiliar) verificar se sao todos da mesma cor (criar qnode ou qleaf)
  //pensar para quando é impar (somos nos a decidir qual dos quadrantes o maior) ->
  //qnode com 2 subarvores tem de ter dois empty

  //def makeBitMap[A](qt:QTree[A]):BitMap = {}

  //percorrer, cda vez que encontra uma folha retira as coordenadas e depois pinta da mesma cor
  //matriz imutavel

  /** Magnification/reduction operation on an image, according to the factor provided */
  def scale[A](sc:Double, qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, scale(sc,one), scale(sc,two), scale(sc,three), scale(sc,four))
      case QLeaf((cd: Coords, color)) =>
        QLeaf(((cd._1._1*sc.round.toInt,cd._1._2*sc.round.toInt),
          (cd._2._1*sc.round.toInt,cd._2._2*sc.round.toInt)),color)
    }
  }

  /** Given a QTree it calculates the max coordinate value. */
  def maxTreeCoord[A](qt:QTree[A]):Int = {
    qt match {
      case QLeaf((cd:Coords,color)) => cd._2._2
      case QNode(cd:Coords, _, _, _, _) => cd._2._2
      case _ => 0
    }
  }

  /** Given two diagonally opposite points on a square, it calculates the other two points. */
  def oppositeCoords(cd:Coords):Coords = {
    val x1 = cd._1._1
    val y1 = cd._1._2
    val x2 = cd._2._1
    val y2 = cd._2._2
    val xc:Double = (x1 + x2) / 2.0
    val yc:Double = (y1 + y2) / 2.0
    val xd:Double = (x1 - x2) / 2.0
    val yd:Double = (y1 - y2) / 2.0
    val x3 = xc - yd
    val y3 = yc + xd
    val x4 = xc + yd
    val y4 = yc - xd
    new Coords ((x4.toInt,y4.toInt),(x3.toInt,y3.toInt))
   }

  /** Vertical mirroring operation. */
  def mirrorV[A](qt:QTree[A]):QTree[A] = {
    val max = maxTreeCoord(qt)
    def aux[A](qt:QTree[A],max:Int):QTree[A] = {
      qt match {
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(two,max), aux(one,max), aux(three,max), aux(four,max))
        case QLeaf((cd:Coords, color)) =>
          QLeaf((oppositeCoords(new Coords ((max - cd._1._1,cd._1._2),(max - cd._2._1, cd._2._2))), color))
        case _ => QEmpty
      }
    }
    aux(qt,max)
  }

  /** Horizontal mirroring operation. */
  def mirrorH[A](qt:QTree[A]):QTree[A] = {
    val max = maxTreeCoord(qt)
    def aux (qt:QTree[A],max:Int):QTree[A] = {
      qt match {
        case QEmpty => QEmpty
        case QNode(value, one, two, three, four) =>
          QNode(value, aux(three,max), aux(four,max), aux(one,max), aux(two,max))
        case QLeaf((cd: Coords, color)) =>
          QLeaf((oppositeCoords(new Coords ((cd._1._1,max - cd._1._2),(cd._2._1, max - cd._2._2))), color))
      }
    }
    aux(qt,max)
  }

  /*def rotateL[A](qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, rotateL(two), rotateL(four), rotateL(one), rotateL(three))
      case QLeaf(value) => QLeaf(value)
    }
  }

  def rotateR[A](qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, rotateR(two), rotateR(four), rotateR(one), rotateR(three))
      case QLeaf(value) => QLeaf(value)
    }
  }*/

  /** Checks whether the given value is valid as an RGB component */
  def colorComponentInRange(component: Int):Int = {
    if (component > 255) 255
    else if (component < 0) 0
    else component
  }

  /** Obtains the sepia of a RGB color*/
  def sepia(c: Color):Color = {
    val avg = (c.getRed+c.getGreen+c.getBlue)/3
    val depth = 20
    val intensity = 30
    val red = colorComponentInRange(avg+(2*depth))
    val green = colorComponentInRange(avg+depth)
    val blue  = colorComponentInRange(c.getBlue-intensity)
    new Color (red,green,blue)
  }

  /** Obtains the noise value of a RGB color*/
  def noise(c: Color):Color = {
    val random = new scala.util.Random
    val noise = random.nextFloat
    if (noise < 0.01) Color.white //pq branco e nao preto
    else c
  }

  /** Obtains the contrast of a RGB color*/
  def contrast(c: Color):Color = {
    val cont = 2 //n sei q valor colocar para contrast
    val red = colorComponentInRange(c.getRed*cont)
    val green = colorComponentInRange(c.getGreen*cont)
    val blue = colorComponentInRange(c.getBlue*cont)
    new Color (red, green, blue)
  }

  /** Uniform mapping of a function onto the entire image.
   * It should be able to illustrate the application of the effects mentioned above. */
  def mapColorEffect[A](f:Color => Color, qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, mapColorEffect(f,three), mapColorEffect(f,one), mapColorEffect(f,four), mapColorEffect(f,two))
      case QLeaf((value,color: Color)) => QLeaf((value,f(color)))
    }
  }
}