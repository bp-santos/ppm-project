import QTree.Coords
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.TextField
import javafx.scene.image.Image
import javafx.stage.{FileChooser, Stage}

case class Album(name: String, content: List[(String,QTree[Coords])])

class Gallery {

  private var album: Album = Album("Gallery", Nil)

  @FXML
  private var photoNameAdd: TextField = _

  @FXML
  private var photoNameRemove: TextField = _

  @FXML
  private var photoNameFind: TextField = _

  /*def previousPhoto(album: Album, index: Int): QTree[Coords] = {
    if (index <= 0 || index > album.content.length - 1)
      album.content(index)
    else album.content(index - 1)
  }

  def nextPhoto(album: Album, index: Int): QTree[Coords] = {
    if (index < 0 || index >= album.content.length - 1)
      album.content(index)
    else album.content(index - 1)
  }

  def scrollAlbum(f: (Album, Int) => QTree[Coords], album: Album, index: Int): QTree[Coords] = {
    //if (album.content == Nil || album.content.isEmpty) None
    f(album, index)
  }

  def reverse(album: Album): Album = {
    Album(album.name, album.content.reverse)
  }

  def changeAlbumOrder(f: Album => Album, album: Album): Album = {
    //if (album.content == Nil || album.content.isEmpty) None
    f(album)
  }

  def changeAlbumInfo(newName: String, album: Album): Album = {
    if (newName.isEmpty) album
    else Album(newName, album.content)
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

  def onPreviousButtonClicked(): Unit = {

  }

  def onNextButtonClicked(): Unit = {

  }

  def findPhotoInAlbum(): Unit = {
    if (photoNameFind.getText.isEmpty || photoNameFind.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty\n")
      else {
        //val index = findIndex(photoNameFind.getText)
        //val image = album.content.apply(index - 1)
        println("Success finding photo in album\n")
        //println(image + "\n")
        onSlideshowClicked()
      }
    }
  }

  def removePhotoFromAlbum(): Unit = {
    if (photoNameRemove.getText.isEmpty || photoNameRemove.getText.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty\n")
      else {
        //val index = findIndex(photoNameRemove.getText)
        //album = Album(album.name, album.content.take(index - 1) ++ album.content.drop(index))
        println("Success removing photo from album\n")
        println(album + "\n")
      }
    }
  }

 // def findIndex(name: String): Int ={

  //}

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
        case Nil => album = Album(album.name, List((photoNameAdd.getText,qt)))
        case _ => album = Album(album.name, (photoNameAdd.getText,qt) :: album.content)
      }
      println("Success saving photo to album\n")
      println(album + "\n")
    }
  }

  def insertPhotoPath(): String = {
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