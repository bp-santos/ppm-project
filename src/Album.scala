import QTreeUtil.Coords

case class Album(name: String, content:List[QTree[Coords]]) {
  def addPhoto(qt: QTree[Coords], album: Album): Option[Album] = Album.addPhoto(qt, album)
  def removePhoto(index: Int, album: Album): Option[Album] = Album.removePhoto(index, album)
  def previousPhoto (album:Album, index: Int):Option[QTree[Coords]] = Album.previousPhoto(album,index)
  def nextPhoto (album:Album, index: Int):Option[QTree[Coords]] = Album.nextPhoto(album,index)
  def scrollAlbum(f:(Album,Int) => QTree[Coords], album: Album, index:Int):Option[QTree[Coords]] = Album.scrollAlbum(f,album,index)
  def findPhoto[A](index:Int, album: Album): Option[QTree[Coords]] = Album.findPhoto(index,album)
  def reverse[A](album:Album): Option[Album] = Album.reverse(album)
  def changeAlbumOrder[A](f:Album => Album, album: Album): Option[Album] = Album.changeAlbumOrder(f,album)
  def changeAlbumInfo[A](newName: String, album: Album): Option[Album] = Album.changeAlbumInfo(newName,album)
}

object Album {

  def addPhoto(qt: QTree[Coords], album: Album): Option[Album] = {
    if (album.content == Nil) None
    else if (album.content.contains(qt)) Some(album)
    else Some(Album(album.name, qt :: album.content))
  }

  def removePhoto(index: Int, album: Album): Option[Album] = {
    if (album.content == Nil || album.content.isEmpty || (index - 1) > album.content.length) None
    else Some(Album(album.name, album.content.take(index) ++ album.content.drop(index + 1)))
  }

  def previousPhoto (album:Album, index: Int):Option[QTree[Coords]] = {
    if (album.content.length-1 < index+1)
      Some(album.content(index-2))
    else Some(album.content(index-1))
  }

  def nextPhoto (album:Album, index: Int):Option[QTree[Coords]] = {
    if (album.content.length-1 < index+1)
      Some(album.content(index))
    else Some(album.content(index-1))
  }

  def scrollAlbum(f:(Album,Int) => QTree[Coords], album: Album, index:Int):Option[QTree[Coords]] = {
    if (album.content == Nil || album.content.isEmpty) None
    else Some(f(album,index))
  }

  def findPhoto(index:Int, album: Album): Option[QTree[Coords]] = {
    if (album.content == Nil || album.content.isEmpty || (index-1) > album.content.length) None
    else Some(album.content.apply(index-1))
  }

  def reverse(album:Album): Option[Album] = {
    Some(Album(album.name, album.content.reverse))
  }

  def changeAlbumOrder(f:Album => Album, album: Album): Option[Album] = {
    if (album.content == Nil || album.content.isEmpty) None
    else Some(f(album))
  }

  def changeAlbumInfo(newName: String, album: Album): Option[Album] = {
    if (newName.isEmpty) Some(album)
    else Some(Album(newName,album.content))
  }

}