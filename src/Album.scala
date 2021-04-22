case class Album[A](name: String, content:List[QTree[A]]) {
  def addPhoto[A](qt: QTree[A], album: Album[A]): Option[Album[A]] = Album.addPhoto(qt, album)
  def removePhoto[A](index: Int, album: Album[A]): Option[Album[A]] = Album.removePhoto(index, album)
  //def scrollAlbum[A](album: Album[A]):Option[Album[A]] = Album.scrollAlbum(album)
  def findPhoto[A](index:Int, album: Album[A]): Option[QTree[A]] = Album.findPhoto(index,album)
  def reverse[A](album:Album[A]): Option[Album[A]] = Album.reverse(album)
  def changeAlbumOrder[A](f:Album[A] => Album[A], album: Album[A]): Option[Album[A]] = Album.changeAlbumOrder(f,album)
  def changeAlbumInfo[A](newName: String, album: Album[A]): Option[Album[A]] = Album.changeAlbumInfo(newName,album)
}

object Album {

  def addPhoto[A](qt: QTree[A], album: Album[A]): Option[Album[A]] = {
    if (album.content == Nil) None
    else if (album.content.contains(qt)) Some(album)
    else Some(Album(album.name, qt :: album.content))
  }

  def removePhoto[A](index: Int, album: Album[A]): Option[Album[A]] = {
    if (album.content == Nil || album.content.isEmpty || (index - 1) > album.content.length) None
    else Some(Album(album.name, album.content.take(index) ++ album.content.drop(index + 1)))
  }

  //def scrollAlbum[A](album: Album[A]):Option[Album[A]] = {}

  def findPhoto[A](index:Int, album: Album[A]): Option[QTree[A]] = {
    if (album.content == Nil || album.content.isEmpty || (index-1) > album.content.length) None
    else Some(album.content.apply(index-1))
  }

  def reverse[A](album:Album[A]): Option[Album[A]] = {
    Some(Album(album.name, album.content.reverse))
  }

  def changeAlbumOrder[A](f:Album[A] => Album[A], album: Album[A]): Option[Album[A]] = {
    if (album.content == Nil || album.content.isEmpty) None
    else Some(f(album))
  }

  def changeAlbumInfo[A](newName: String, album: Album[A]): Option[Album[A]] = {
    if (newName.isEmpty) Some(album)
    else Some(Album(newName,album.content))
  }

}