package Iscte.GUI

import Iscte._
import QTree._
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.image.Image
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage

import java.io.File
import scala.io.{BufferedSource, Source}

class GUI extends Application {
  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("Gallery - Projeto de Programação Multiparadigma")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Gallery.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    primaryStage.getIcons.add(new Image("Iscte/icon_gallery.png"))
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object FxApp {
  val images: List[(String, QTree[Coords])] = Iscte.Album.makeAlbum(new File("src/Iscte/Images").listFiles.map(_.getName).toList)

  val source: BufferedSource = Source.fromFile("src/Iscte/GUI/album_info.txt")
  val lines: List[String] = source.getLines.toList
  source.close()

  var album = Album(lines(0), images)
  var r = MyRandom(2)

  def main(args: Array[String]): Unit = {
    Application.launch(classOf[GUI], args: _*)
  }
}