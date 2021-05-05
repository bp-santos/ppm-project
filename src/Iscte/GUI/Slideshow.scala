package Iscte.GUI

import Iscte._
import javafx.fxml.FXML
import javafx.scene.image.{Image, ImageView}
import FxApp._
import java.io.{File, FileInputStream}

class Slideshow {

  private var index: Int = -1

  @FXML
  private var actualImage: ImageView = _

  def onPreviousButtonClicked(): Unit = {
    if (this.index - 1 >= 0) {
      val qt = album.content.apply(index - 1)._2
      val bm: BitMap = BitMap.makeBitMap(qt)
      ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
      val file = new File("src/Iscte/temp/temp.png")
      val isImage = new FileInputStream(file)
      actualImage.setImage(new Image(isImage))
      index = index - 1
      println("previous: " + index)
    }
    else {
      index = album.content.length
      onPreviousButtonClicked()
    }
  }

  def onNextButtonClicked(): Unit = {
    if (this.index + 1 <= album.content.length - 1) {
      val qt = album.content.apply(index + 1)._2
      val bm: BitMap = BitMap.makeBitMap(qt)
      ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
      val file = new File("src/Iscte/temp/temp.png")
      val isImage = new FileInputStream(file)
      actualImage.setImage(new Image(isImage))
      index = index + 1
    }
    else {
      index = -1
      onNextButtonClicked()
    }
  }
}
