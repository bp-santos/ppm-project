package Iscte.GUI

import Iscte._
import QTree._
import Iscte.GUI.ImageUtil
import javafx.fxml.{FXML, FXMLLoader}
import javafx.scene.{Node, Parent, Scene}
import javafx.scene.control.{Button, RadioButton, TextField}
import javafx.stage.{FileChooser, Stage}
import FxApp._
import javafx.scene.image.{Image, ImageView}
import javafx.scene.layout.HBox

import java.io._

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

  @FXML
  private var slideshowButton: Button = _

  @FXML
  private var gridButton: Button = _

  @FXML
  private var editPhotoButton: Button = _

  def onSlideshowClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Slideshow.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    slideshowButton.getScene.setRoot(mainViewRoot)
    val txtImage: Node = mainViewRoot.getChildrenUnmodifiable.get(0).getScene.lookup("#actualImage")
    val imageField: ImageView = txtImage.asInstanceOf[ImageView]
    val bm: BitMap = BitMap.makeBitMap(album.content.head._2)
    Iscte.GUI.ImageUtil.writeImage(bm.value, "src/Iscte/temp/temp.png", "png")
    val file = new File("src/Iscte/temp/temp.png")
    val image = new FileInputStream(file)
    imageField.setImage(new Image(image))
  }

  def onGridClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("Grid.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    gridButton.getScene.setRoot(mainViewRoot)
  }

  def onManipulateClicked(): Unit = {
    val fxmlLoader = new FXMLLoader(getClass.getResource("EditPhoto.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    editPhotoButton.getScene.setRoot(mainViewRoot)
  }

  private def findIndex(name: String): Int = {
    val lst = album.content.map(_._1)
    lst.indexWhere(element => element == name)
  }

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
        ImageUtil.writeImage(bt.value, "src/Iscte/Images/" + newName, "png")
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
    else {
      album = Album(newName, album.content)
      val pw = new PrintWriter(new File("src/Iscte/GUI/album_info.txt"))
      pw.write(album.name)
      pw.close()
    }
  }

  def changeAlbumOrder(): Unit = {
    if (album.content.isEmpty || album.content == null)
      System.out.println("Error: Album content is empty")
    else if (reverseOrder.isSelected)
      album = Album(album.name, album.content.reverse)
    else System.out.println("Error: Reverse order not selected")
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
    val file = new File("src/Iscte/Images/" + imageName)
    val isImage = new FileInputStream(file)
    val imageStage: Stage = new Stage()
    imageStage.setTitle("Image - " + imageName)
    imageStage.getIcons.add(new Image("Iscte/icon_gallery.png"))
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
          val file = new File("src/Iscte/Images/" + photo)
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
        case _ => album = Album(album.name, album.content :+ (photo, qt))
      }
      val bt: BitMap = BitMap.makeBitMap(qt)
      ImageUtil.writeImage(bt.value, "src/Iscte/Images/" + photo, "png")
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