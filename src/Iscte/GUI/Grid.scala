package Iscte.GUI

import Iscte.GUI.FxApp.album
import Iscte.{BitMap, ImageUtil}
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.Button
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.TilePane
import java.io.{File, FileInputStream}

class Grid {

  @FXML
  private var homeButton: Button = _

  @FXML
  private var tilePane1: TilePane = _

  @FXML
  private var showButton: Button = _

  def onHomeButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    homeButton.getScene.setRoot(mainViewRoot)
  }

  def showGrid(): Unit = {
    tilePane1.setHgap(10)
    tilePane1.setVgap(10)
    for (index <- album.content.indices) {
      val qt = album.content.apply(index)._2
      val bm: BitMap = BitMap.makeBitMap(qt)
      ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
      val file = new File("src/Iscte/temp/temp.png")
      val image = new FileInputStream(file)
      val imageView = new ImageView()
      imageView.setImage(new Image(image))
      imageView.setPreserveRatio(true)
      imageView.setFitWidth(250)
      imageView.setFitHeight(250)
      tilePane1.getChildren.add(imageView)
    }
    showButton.setDisable(true)
  }
}
