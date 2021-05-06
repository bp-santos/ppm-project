package Iscte.GUI

import Iscte.GUI.FxApp.album
import Iscte.{BitMap, ImageUtil, QTreeUtil}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.{Button, Slider}
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.TilePane
import javafx.scene.transform.Rotate

import java.io.{File, FileInputStream}

class Grid {

  @FXML
  private var homeButton: Button = _

  @FXML
  private var tilePane1: TilePane = _

  @FXML
  private var slider1: Slider = _

  def onHomeButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    homeButton.getScene.setRoot(mainViewRoot)
  }

  def showGrid(): Unit = {
    for( index <- 0 until album.content.length-1) {
      val qt = album.content.apply(index)._2
      val bm: BitMap = BitMap.makeBitMap(QTreeUtil.scale(20,qt))
      ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
      val file = new File("src/Iscte/temp/temp.png")
      val isImage = new FileInputStream(file)
      val imageView = new ImageView()
      imageView.setImage(new Image(isImage))
      imageView.setPreserveRatio(true)
      tilePane1.getChildren.add(imageView)
    }
  }

  def onDragDropped(): Unit = {
    tilePane1.setRotationAxis(Rotate.Y_AXIS)
    tilePane1.setRotate(slider1.getValue)
  }
}
