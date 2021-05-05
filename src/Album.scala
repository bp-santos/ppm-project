import QTree.Coords

case class Album(name: String, content: List[(String, QTree[Coords])])

object Album {
  def makeAlbum(images:List[String]): List[(String,QTree[Coords])] ={
    images match {
      case x::xs =>
        (x,QTree.makeQTree(BitMap(ImageUtil.readColorImage(getClass.getResource("/Images/" + x).getPath))))::makeAlbum(xs)
      case x::Nil => List((x,QTree.makeQTree(BitMap(ImageUtil.readColorImage(getClass.getResource("/Images" + x).getPath)))))
      case Nil => Nil
    }
  }
}