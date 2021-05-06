package Iscte.GUI

import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.Parent
import javafx.scene.control.Button

class Grid {

  @FXML
  private var homeButton: Button = _

  def onHomeButtonClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    homeButton.getScene.setRoot(mainViewRoot)
  }



}
