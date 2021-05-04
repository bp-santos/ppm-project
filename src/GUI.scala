import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

class GUI extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Gallery - Projeto de Programação Multiparadigma")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.getIcons.add(new Image("/images/icon_gallery.png"))
    primaryStage.setScene(scene)
    primaryStage.show()
  }

  def close(): Unit = {

  }

}

object FxApp {
  var album: Album = Album("Gallery", Nil)

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GUI], args: _*)
  }
}
