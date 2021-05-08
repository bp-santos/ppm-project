package Iscte.GUI

import Iscte._
import Iscte.ImageUtil
import QTree._
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.control.{Button, TextField}
import javafx.scene.image.{Image, ImageView}
import FxApp._
import javafx.scene.Parent

import java.io.{File, FileInputStream}

class EditPhoto {

  var qt: QTree[Coords] = QEmpty

  @FXML
  private var photoName: TextField = _

  @FXML
  private var imageView1: ImageView = _

  @FXML
  private var insertScale: TextField = _

  @FXML
  private var homeButton: Button = _

  def onHomeButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    homeButton.getScene.setRoot(mainViewRoot)
  }

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
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "scale" =>
          if(!insertScale.getText.isBlank && insertScale.getText.nonEmpty) {
            val sc = insertScale.getText.toDouble
            this.qt = QTreeUtil.scale(sc, this.qt)
            val bm: BitMap = BitMap.makeBitMap(this.qt)
            ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
          }
        case "mirrorV" =>
          this.qt = QTreeUtil.mirrorV(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "mirrorH" =>
          this.qt = QTreeUtil.mirrorH(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "rotateL" =>
          this.qt = QTreeUtil.rotateL(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "rotateR" =>
          this.qt = QTreeUtil.rotateR(this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "pureNoise" =>
          this.qt = QTreeUtil.mapColorEffect_1(this.qt,Iscte.GUI.FxApp.r)._1
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "noise" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.notPureNoise, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "contrast" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.contrast, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
        case "sepia" =>
          this.qt = QTreeUtil.mapColorEffect(QTreeUtil.sepia, this.qt)
          val bm: BitMap = BitMap.makeBitMap(this.qt)
          ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
      }
      val file = new File("src/Iscte/temp/temp.png")
      val isImage = new FileInputStream(file)
      imageView1.setImage(new Image(isImage))
    }
  }

  def originalPhoto(): Unit = findPhoto("original")

  def scalePhoto(): Unit = findPhoto("scale")

  def mirrorVPhoto(): Unit = findPhoto("mirrorV")

  def mirrorHPhoto(): Unit = findPhoto("mirrorH")

  def rotateLPhoto(): Unit = findPhoto("rotateL")

  def rotateRPhoto(): Unit = findPhoto("rotateR")

  def pureNoisePhoto(): Unit = findPhoto("pureNoise")

  def noisePhoto(): Unit = findPhoto("noise")

  def contrastPhoto(): Unit = findPhoto("contrast")

  def sepiaPhoto(): Unit = findPhoto("sepia")

  def savePhoto(): Unit = {
    val bt: BitMap = BitMap.makeBitMap(this.qt)
    ImageUtil.writeImage(bt.value, "src/Iscte/Images/" + photoName.getText, "png")
    this.qt match {
      case QEmpty => album = album
      case _ =>
        val index = getIndex(photoName.getText)
        if (index == -1)
          System.out.println("Error: Image not found")
        else
        album = Album(album.name, album.content.take(index) ++ album.content.drop(index + 1))
        album.content match {
          case Nil => album = Album(album.name, List((photoName.getText, qt)))
          case _ => album = Album(album.name, (photoName.getText, qt) :: album.content)
        }
    }
  }
}
