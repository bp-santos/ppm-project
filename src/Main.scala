import QTree.{Coords, Point, Section}
import java.awt.Color

object Main extends App {
  val l1: QLeaf[Coords, Section] = QLeaf((((0, 0): Point, (1, 2): Point): Coords, Color.red): Section)
  val l2: QLeaf[Coords, Section] = QLeaf((((1, 0): Point, (2, 1): Point): Coords, Color.blue): Section)
  val l3: QLeaf[Coords, Section] = QLeaf((((0, 2): Point, (1, 3): Point): Coords, Color.yellow): Section)
  val l4: QLeaf[Coords, Section] = QLeaf((((1, 1): Point, (2, 3): Point): Coords, Color.green): Section)

  val qt: QTree[Coords] = QNode(((0, 0), (2, 3)), l1, l2, l3, l4)

  val bm: ColorMap[Int] = ColorMap(ImageUtil.readColorImage(getClass.getResource("/Images/5x4px.png").getPath))
  println(QTree.makeQTree(bm))
}