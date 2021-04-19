import Gallery.{Coords, Section, Point}
import java.awt.Color

object Main extends App {
    val l1: QLeaf[Coords, Section] = QLeaf((((0,0):Point,(1,1):Point):Coords, Color.red):Section)
    val l2: QLeaf[Coords, Section] = QLeaf((((1,0):Point,(2,1):Point):Coords, Color.blue):Section)
    val l3: QLeaf[Coords, Section] = QLeaf((((0,1):Point,(1,2):Point):Coords, Color.yellow):Section)
    val l4: QLeaf[Coords, Section] = QLeaf((((1,1):Point,(2,2):Point):Coords, Color.green):Section)

    val qt: QTree[Coords] = QNode(((0,0),(2,2)), l1, l2, l3, l4)

    println(qt)
    //println(Gallery.mirrorV(qt))
    println(Gallery.rotateL(qt))

    //val myList = Array(Array(1.9, 2.9, 3.4, 3.5))
    //println(Gallery.toList(myList))
}