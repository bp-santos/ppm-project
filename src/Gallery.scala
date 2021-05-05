import QTree._
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Parent, Scene}
import javafx.scene.control.{RadioButton, TextField}
import javafx.stage.{FileChooser, Stage}
import FxApp._
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.HBox
import java.io.{File, FileInputStream}

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
    val fxmlLoader = new FXMLLoader(getClass.getResource("GUI/Slideshow.fxmlfxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    secondStage.setTitle("Slideshow - Projeto de Programação Multiparadigma")
    secondStage.getIcons.add(new Image("icon_gallery.png"))
    secondStage.setScene(scene)
    secondStage.show()
  }

  def onGridClicked(): Unit = {
    val thirdStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("GUI/Grid.fxmlfxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    thirdStage.setTitle("Grid - Projeto de Programação Multiparadigma")
    thirdStage.getIcons.add(new Image("icon_gallery.png"))
    thirdStage.setScene(scene)
    thirdStage.show()
  }

  def onManipulateClicked(): Unit = {
    val fourthStage: Stage = new Stage()
    val fxmlLoader = new FXMLLoader(getClass.getResource("GUI/EditPhoto.fxmlfxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    fourthStage.setTitle("Edit Image - Projeto de Programação Multiparadigma")
    fourthStage.getIcons.add(new Image("icon_gallery.png"))
    fourthStage.setScene(scene)
    fourthStage.show()
  }

  private def findIndex(name: String): Int = {
    val lst = album.content.map(_._1)
    lst.indexWhere(element => element == name)
  }

  //mudar fisicamente
  def changePhotoInfo(): Unit = {
    val newName = newNameChange.getText
    val oldName = oldNameChange.getText
    if (oldName.isEmpty || oldName.isBlank)
      System.out.println("Error: Old image name field empty/blank")
    else if (newName.isEmpty || newName.isBlank)
      System.out.println("Error: New image name field empty/blank")
    else {
      val index = findIndex(oldName)
      if (index == -1)
        System.out.println("Error: Image not found")
      else {
        val qt = album.content.apply(index)._2
        album = Album(album.name, (newName, qt) :: album.content)
        val bt: BitMap = BitMap.makeBitMap(qt)
        ImageUtil.writeImage(bt.value, "src/Images/" + newName, "png")
        photoNameRemove.setText(oldName)
        removePhotoFromAlbum()
        photoNameRemove.setText("")
      }
    }
  }

  def changeAlbumInfo(): Unit = {
    val newName = insertNameChange.getText
    if (newName.isEmpty || newName.isBlank)
      System.out.println("Error: Image name field empty/blank")
    else
      album = Album(newName, album.content)
  }

  def changeAlbumOrder(): Unit = {
    if (album.content.isEmpty || album.content == null)
      System.out.println("Error: Album content is empty")
    else if (reverseOrder.isSelected) {
      album = Album(album.name, album.content.reverse)
    } else System.out.println("Error: Reverse order not selected")
  }

  def findPhotoInAlbum(): Unit = {
    val photo = photoNameFind.getText
    if (photo.isEmpty || photo.isBlank)
      System.out.println("Error: Image name field empty/blank\n")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty")
      else {
        val index = findIndex(photo)
        if (index == -1)
          System.out.println("Error: Image not found")
        else
          showImage(index)
      }
    }
  }

  def showImage(index: Int): Unit = {
    val imageName = album.content.apply(index)._1
    val file = new File("src/Images/" + imageName)
    val isImage = new FileInputStream(file)
    val imageStage: Stage = new Stage()
    imageStage.setTitle("Image - " + imageName)
    imageStage.getIcons.add(new Image("icon_gallery.png"))
    imageStage.setScene(new Scene(new HBox(4, new ImageView(new Image(isImage)))))
    imageStage.show()
  }

  def removePhotoFromAlbum(): Unit = {
    val photo = photoNameRemove.getText
    if (photo.isEmpty || photo.isBlank)
      System.out.println("Error: Image name field empty/blank")
    else {
      if (album.content == Nil || album.content == List())
        System.out.println("Error: Album content is empty")
      else {
        val index = findIndex(photo)
        if (index == -1)
          System.out.println("Error: Image not found")
        else {
          album = Album(album.name, album.content.take(index) ++ album.content.drop(index + 1))
          val file = new File("src/Images/" + photo)
          if (file.exists) file.delete()
        }
      }
    }
  }

  def addPhotoToAlbum(): Unit = {
    val photo = photoNameAdd.getText
    val path = insertPhotoPath()
    if (path.isEmpty || path.isBlank || path == null)
      System.out.println("Error: Image path field empty/blank")
    else if (photo.isEmpty || photo.isBlank)
      System.out.println("Error: Image name field empty/blank")
    else {
      val qt: QTree[Coords] = QTree.makeQTree(BitMap(ImageUtil.readColorImage(path)))
      album.content match {
        case Nil => album = Album(album.name, List((photo, qt)))
        case _ => album = Album(album.name, (photo, qt) :: album.content)
      }
      val bt: BitMap = BitMap.makeBitMap(qt)
      ImageUtil.writeImage(bt.value, "src/Images/" + photo, "png")
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
    file.getAbsolutePath
  }
}