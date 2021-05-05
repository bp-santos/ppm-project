import QTree._
import javafx.fxml.FXML
import javafx.scene.control.TextField
import javafx.scene.image.{Image, ImageView}
import FxApp._
import java.io.{File, FileInputStream}

class EditPhoto {

  var qt: QTree[Coords] = QEmpty

  @FXML
  private var photoName: TextField = _

  @FXML
  private var imageView1: ImageView = _

  private def getIndex(photo: String): Int = {
    if (photo.isEmpty || photo.isBlank) {
      System.out.println("Error: Image name field empty/blank")
      -1
    } else {
      if (album.content == Nil || album.content == List()) {
        System.out.println("Error: Album content is empty")
        -1
      } else {
        val lst = album.content.map(_._1)
        lst.indexWhere(element => element == photo)
      }
    }
  }

  private def findPhoto(function: String): Unit = {
    val index = getIndex(photoName.getText)
    if (index == -1)
      System.out.println("Error: Image not found")
    else {
      function match {
        case "original" =>
          this.qt = album.content.apply(index)._2
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "scale" =>
          this.qt = QTreeUtil.scale(3, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt) //fazer variações
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "mirrorV" =>
          this.qt = QTreeUtil.mirrorV(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "mirrorH" =>
          this.qt = QTreeUtil.mirrorH(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "rotateL" =>
          this.qt = QTreeUtil.rotateL(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "rotateR" =>
          this.qt = QTreeUtil.rotateR(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "pureNoise" =>
          this.qt = QTreeUtil.mapColorEffect_1(MyRandom(2), this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "noise" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.notPureNoise, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "contrast" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.contrast, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
        case "sepia" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.sepia, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/temp/temp.png", "png")
      }
      val file = new File("src/temp/temp.png")
      val isImage = new FileInputStream(file)
      imageView1.setImage(new Image(isImage))
    }
  }

  def originalPhoto(): Unit = findPhoto("original")

  def scalePhoto(): Unit = findPhoto("scale")

  def mirroVPhoto(): Unit = findPhoto("mirrorV")

  def mirroHPhoto(): Unit = findPhoto("mirrorH")

  def rotateLPhoto(): Unit = findPhoto("rotateL")

  def rotateRPhoto(): Unit = findPhoto("rotateR")

  def pureNoisePhoto(): Unit = findPhoto("pureNoise")

  def noisePhoto(): Unit = findPhoto("noise")

  def contrastPhoto(): Unit = findPhoto("contrast")

  def sepiaPhoto(): Unit = findPhoto("sepia")

  def savePhoto(): Unit = {
    val bt: BitMap = BitMap.makeBitMap(this.qt)
    ImageUtil.writeImage(bt.value, "src/Images/new_" + photoName.getText, "png")
    this.qt match {
      case QEmpty => album = album
      case _ =>
        album.content match {
          case Nil => album = Album(album.name, List(("new_" + photoName.getText, qt)))
          case _ => album = Album(album.name, ("new_" + photoName.getText, qt) :: album.content)
        }
    }
  }
}
