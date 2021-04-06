import java.awt.Color

/*sealed*/ trait QTree[+A]

case object QEmpty extends QTree[Nothing]

case class QNode[A](value: A, one: QTree[A], two: QTree[A], three: QTree[A], four: QTree[A]) extends QTree[A]

case class QLeaf[A, B](value: B) extends QTree[A]

case class BitMap(value: List[List[Int]]) {

  def imgToBitmap(arr: Array[Array[Int]]): Unit = {

    //arr match {}
  }



}
// readColorImage -> ler matriz -> decode recursively -> transformar arrays em listas
//lista de integer ? ou Array?
//lista de lista de 3 inteiros ou apenas 1

case class Gallery[A](myField: QTree[A]){
  //def makeQTree(b:BitMap):QTree[A] = Gallery.makeQTree(b)
  //def makeBitMap():BitMap = Gallery.makeBitMap(this.myField)
  def scale(scale:Double):QTree[A] = Gallery.scale(scale,this.myField)
  /*def mirrorV():QTree[A] = Gallery.mirrorV(this.myField)
  def mirrorH():QTree[A] = Gallery.mirrorH(this.myField)
  def rotateL():QTree[A] = Gallery.rotateL(this.myField)
  def rotateR():QTree[A] = Gallery.rotateR(this.myField)*/
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

  /*def mirrorV[A](qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, mirrorV(three), mirrorV(four), mirrorV(one), mirrorV(two))
      case QLeaf(value) => QLeaf(value)
    }
  }

  def mirrorH[A](qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, mirrorH(two), mirrorH(one), mirrorH(four), mirrorH(three))
      case QLeaf(value) => QLeaf(value)
    }
  }

  def rotateL[A](qt:QTree[A]):QTree[A] = {
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
        QNode(value, rotateR(three), rotateR(one), rotateR(four), rotateR(two))
      case QLeaf(value) => QLeaf(value)
    }
  }*/

  def sepia(c: Color):Color = {
    val r = c.getRed
    val g = c.getGreen
    val b = c.getBlue
    val avg = (r+g+b)/3
    val depth = 20
    val rr = avg+(2*depth)
    val gg = avg+depth
    new Color (rr,gg,b)
  }

  /*def noise(c: Color):Color = {
    //Acho q o noise e aquilo de preto + cor , preto + cor
    val r = c.getRed
    val g = c.getGreen
    val b = c.getBlue
    val bl = c.getBlack
  }

  def contrast(c: Color):Color = {
    val r = c.getRed
    val g = c.getGreen
    val b = c.getBlue
    val r = r * 1.3 //n ei q valor colocar para contrat
    val g = g * 1.3
    val b = b * 1.3
    if(b>255) b = 255
    if(r>255) r = 255
    if (g>255)
  }*/

  def mapColorEffect[A](f:Color => Color, qt:QTree[A]):QTree[A] = {
    qt match{
      case QEmpty => QEmpty
      case QNode(value, one, two, three, four) =>
        QNode(value, mapColorEffect(f,three), mapColorEffect(f,one), mapColorEffect(f,four), mapColorEffect(f,two))
      case QLeaf((value,color: Color)) => QLeaf((value,f(color)))
    }
  }
}