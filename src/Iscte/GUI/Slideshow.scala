package Iscte.GUI

import Iscte._
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.image.{Image, ImageView}
import FxApp._
import Iscte.QTree.Coords
import javafx.scene.Parent
import javafx.scene.control.Button

import java.io.{File, FileInputStream}

class Slideshow {

  private var index: Int = -1

  @FXML
  private var actualImage: ImageView = _

  @FXML
  private var homeButton: Button = _

  private def saveBitMap(qt: QTree[Coords]): Unit = {
    val bm: BitMap = BitMap.makeBitMap(qt)
    Iscte.GUI.ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
    val file = new File("src/Iscte/temp/temp.png")
    val isImage = new FileInputStream(file)
    actualImage.setImage(new Image(isImage))
  }

  def onHomeButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    homeButton.getScene.setRoot(mainViewRoot)
  }

  def onPreviousButtonClicked(): Unit = {
    if (this.index - 1 >= 0) {
      saveBitMap(album.content.apply(index - 1)._2)
      index = index - 1
    }
    else {
      index = album.content.length
      onPreviousButtonClicked()
    }
  }

  def onNextButtonClicked(): Unit = {
    if (this.index + 1 <= album.content.length - 1) {
      saveBitMap(album.content.apply(index + 1)._2)
      index = index + 1
    }
    else {
      index = -1
      onNextButtonClicked()
    }
  }
}