import QTree.Coords
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{RadioButton, TextField}
import javafx.scene.image.{Image, ImageView}
import javafx.stage.{FileChooser, Stage}

case class Album(name: String, content: List[(String, QTree[Coords])])

class Gallery {

  var album: Album = Album("Gallery", Nil)

  @FXML
  private var photoNameAdd: TextField = _

  @FXML
  private var photoNameRemove: TextField = _

  @FXML
  private var photoNameFind: TextField = _

  @FXML
  private var reverseOrder: RadioButton = _

  @FXML
  private var insertNameChange: TextField = _

  //@FXML
  //private var imageView1: ImageView = _

  /*def previousPhoto(album: Album, index: Int): QTree[Coords] = {
    if (index <= 0 || index > album.content.length - 1)
      album.content(index)
    else album.content(index - 1)
  }

  def nextPhoto(album: Album, index: Int): QTree[Coords] = {
    if (index < 0 || index >= album.content.length - 1)
      album.content(index)
    else album.content(index - 1)
  }*/

  def onSlideshowClicked(): Unit = {
    val secondStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("Slideshow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    secondStage.setTitle("Slideshow - Projeto de Programação Multiparadigma")
    secondStage.getIcons.add(new Image("/images/icon_gallery.png"))
    secondStage.setScene(scene)
    secondStage.show()
  }

  def onPreviousButtonClicked(): Unit = {}

  def onNextButtonClicked(): Unit = {}

  def onGridClicked(): Unit = {
    val thirdStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("Grid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    thirdStage.setTitle("Grid - Projeto de Programação Multiparadigma")
    thirdStage.getIcons.add(new Image("/images/icon_gallery.png"))
    thirdStage.setScene(scene)
    thirdStage.show()
  }

  def changeAlbumInfo(): Unit = {
    if (insertNameChange.getText.isEmpty || insertNameChange.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      album = Album(insertNameChange.getText, album.content)
      println(album + "\n")
    }
  }

  def changeAlbumOrder(): Unit = {
    if (album.content.isEmpty || album.content == null)
      System.out.println("Error: Album content is empty\n")
    else if (reverseOrder.isSelected) {
      album = Album(album.name, album.content.reverse)
      println(album + "\n")
    } else System.out.println("Error: Reverse order not selected\n")
  }

  //mostrar imagem encontrada
  def findPhotoInAlbum(): Unit = {
    if (photoNameFind.getText.isEmpty || photoNameFind.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty\n")
      else {
        val index = findIndex(photoNameFind.getText)
        if (index == -1)
          System.out.println("Error: Image not found\n")
        else {
          println("Success finding photo in album\n")
          println(album.content.apply(index) + "\n")
          //showImage(index)
        }
      }
    }
  }

  /*def showImage(index: Int): Unit = {
    imageView1.setImage(new Image())
  }*/

  def removePhotoFromAlbum(): Unit = {
    if (photoNameRemove.getText.isEmpty || photoNameRemove.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty\n")
      else {
        val index = findIndex(photoNameRemove.getText)
        if (index == -1)
          System.out.println("Error: Image not found\n")
        else {
          album = Album(album.name, album.content.take(index) ++ album.content.drop(index + 1))
          println("Success removing photo from album\n")
          println(album + "\n")
        }
      }
    }
  }

  private def findIndex(name: String): Int = {
    val lst = album.content.map(_._1)
    lst.indexWhere(element => element == name)
  }

  def addPhotoToAlbum(): Unit = {
    val path = insertPhotoPath()
    if (path.isEmpty || path.isBlank || path == null)
      System.out.println("Error: Image path field empty/blank\n")
    else if (photoNameAdd.getText.isEmpty || photoNameAdd.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      val bm: ColorMap[Int] = ColorMap(ImageUtil.readColorImage(path))
      val qt: QTree[Coords] = QTree.makeQTree(bm)
      album.content match {
        case Nil => album = Album(album.name, List((photoNameAdd.getText, qt)))
        case _ => album = Album(album.name, (photoNameAdd.getText, qt) :: album.content)
      }
      println("Success saving photo to album\n")
      println(album + "\n")
    }
  }

  private def insertPhotoPath(): String = {
    val stage: Stage = new Stage()
    val fileChooser = new FileChooser()
    fileChooser.setTitle("Upload File Path")
    fileChooser.getExtensionFilters.addAll(
      new FileChooser.ExtensionFilter("Images Files", "*.jpg", "*.png")
    )
    val file = fileChooser.showOpenDialog(stage)
    println(file.getAbsolutePath + "\n")
    file.getAbsolutePath
  }
}