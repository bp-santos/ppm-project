import QTree.Coords
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{RadioButton, TextField}
import javafx.stage.{FileChooser, Stage}
import FxApp._

class Gallery {

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

  @FXML
  private var oldNameChange: TextField = _

  @FXML
  private var newNameChange: TextField = _

  def onSlideshowClicked(): Unit = {
    val secondStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("Slideshow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    secondStage.setTitle("Slideshow - Projeto de Programação Multiparadigma")
    //secondStage.getIcons.add(new Image("/images/icon_gallery.png"))
    secondStage.setScene(scene)
    secondStage.show()
  }

  def onGridClicked(): Unit = {
    val thirdStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("Grid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    thirdStage.setTitle("Grid - Projeto de Programação Multiparadigma")
    //thirdStage.getIcons.add(new Image("/images/icon_gallery.png"))
    thirdStage.setScene(scene)
    thirdStage.show()
  }

  def onManipulateClicked(): Unit = {
    val fourthStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("EditPhoto.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    fourthStage.setTitle("Edit Image - Projeto de Programação Multiparadigma")
    //fourthStage.getIcons.add(new Image("/images/icon_gallery.png"))
    fourthStage.setScene(scene)
    fourthStage.show()
  }

  private def findIndex(name: String): Int = {
    val lst = album.content.map(_._1)
    lst.indexWhere(element => element == name)
  }

  def changePhotoInfo(): Unit = {
    val newName = newNameChange.getText
    val oldName = oldNameChange.getText
    if (oldName.isEmpty || oldName.isBlank)
      System.out.println("Error: Old image name field empty/blank\n")
    else if (newName.isEmpty || newName.isBlank)
      System.out.println("Error: New image name field empty/blank\n")
    else {
      val index = findIndex(oldName)
      if (index == -1)
        System.out.println("Error: Image not found\n")
      else {
        album = Album(album.name, (newName, album.content.apply(index)._2) :: album.content)
        photoNameRemove.setText(oldName)
        removePhotoFromAlbum()
        photoNameRemove.setText("")
        showImage(0)
      }
    }
  }

  def changeAlbumInfo(): Unit = {
    val newName = insertNameChange.getText
    if (newName.isEmpty || newName.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      album = Album(newName, album.content)
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

  def findPhotoInAlbum(): Unit = {
    val photo = photoNameFind.getText
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
          println(album.content.apply(index) + "\n")
          showImage(index)
        }
      }
    }
  }

  //quando o makeBitMap estiver concluído é necessário alterar esta função
  def showImage(index: Int): Unit = {
    val imageName = album.content.apply(index)._1
    val imageStage: Stage = new Stage()
    imageStage.setTitle("Image - " + imageName)
    //imageStage.getIcons.add(new Image("/images/icon_gallery.png"))
    //imageStage.setScene(new Scene(new HBox(4, new ImageView(new Image("/icon_gallery.png")))))
    imageStage.show()
  }

  def removePhotoFromAlbum(): Unit = {
    val photo = photoNameRemove.getText
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
          album = Album(album.name, album.content.take(index) ++ album.content.drop(index + 1))
          println("Success removing photo from album\n")
          println(album + "\n")
        }
      }
    }
  }

  def addPhotoToAlbum(): Unit = {
    val photo = photoNameAdd.getText
    val path = insertPhotoPath()
    if (path.isEmpty || path.isBlank || path == null)
      System.out.println("Error: Image path field empty/blank\n")
    else if (photo.isEmpty || photo.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      val qt: QTree[Coords] = QTree.makeQTree(BitMap(ImageUtil.readColorImage(path)))
      album.content match {
        case Nil => album = Album(album.name, List((photo, qt)))
        case _ => album = Album(album.name, (photo, qt) :: album.content)
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