import QTree.Coords
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{RadioButton, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.HBox
import javafx.stage.{FileChooser, Stage}
import FxApp._
class EditPhoto {

  var qt: QTree[Coords] = QEmpty

  @FXML
  private var photoName: TextField = _

  @FXML
  private var imageView1: ImageView = _

  private def findPhoto(): Unit = {
    val photo = photoName.getText
    if (photo.isEmpty || photo.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty\n")
      else {
        val index = findIndex(photo)
        if (index == -1)
          System.out.println("Error: Image not found\n")
        else {
          println("Success finding photo in album\n")
          println(album.content.apply(index)._2 + "\n")
          this.qt = album.content.apply(index)._2
        }
      }
    }
  }

  private def findIndex(name: String): Int = {
    val lst = album.content.map(_._1)
    lst.indexWhere(element => element == name)
  }

  def originalPhoto(): Unit ={
    findPhoto()
    val bm: BitMap = BitMap.makeBitMap(this.qt)
    ImageUtil.writeImage(bm.value,"src/temp/temp.png","png")
    imageView1.setImage(new Image("/temp/temp.png"))
  }

  def scalePhoto(): Unit ={
    findPhoto()
  }

  def mirroVPhoto(): Unit ={
    findPhoto()
  }

  def mirroHPhoto(): Unit ={
    findPhoto()
  }

  def rotateLPhoto(): Unit ={
    findPhoto()
  }

  def rotateRPhoto(): Unit ={
    findPhoto()
  }

  def pureNoisePhoto(): Unit ={
    findPhoto()
  }

  def noisePhoto(): Unit ={
    findPhoto()
  }

  def contrastPhoto(): Unit ={
    findPhoto()
  }

  def sepiaPhoto(): Unit ={
    findPhoto()
  }

  def savePhoto(): Unit ={

  }
}
