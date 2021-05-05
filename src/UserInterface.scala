import QTree._
import QTreeUtil._
import java.io._
import java.util.Scanner
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object UserInterface {
  def init(): Unit = {
    println("Qual a imagem que deseja manipular?")
    println("\nImagens: ")
    val images = new File(getClass.getResource("/Images").getPath).listFiles.map(_.getName).toList
    print(images + "\n\nInput: ")
    val scanner = new Scanner(System.in)
    val file = scanner.nextLine()
    fileExists(file, images) match {
      case Success(_) => menu(scanner, file)
      case Failure(_) => println("\nImagem inserida não existe")
    }
  }

  private def fileExists(file: String, images: List[String]): Try[Unit] = Try(images.contains(file))

  @tailrec
  private def menu(scanner: Scanner, file: String): Unit = {
    val image = QTree.makeQTree(BitMap(ImageUtil.readColorImage(getClass.getResource("/Images/" + file).getPath)))
    println("\nOriginal: " + image)
    print("\nMenu: " +
      "\n1 - Aplicar scale" +
      "\n2 - Aplicar mirror vertical" +
      "\n3 - Aplicar mirror horizontal" +
      "\n4 - Aplicar rotate left" +
      "\n5 - Aplicar rotate right" +
      "\n6 - Aplicar efeito" +
      "\n0 - Sair" +
      "\n\nInput: ")
    val option = scanner.nextLine()
    option match {
      case "1" =>
        print("Scale value: ")
        val sc = scanner.nextLine().toDouble
        println(QTreeUtil.scale(sc, image))
      case "2" => println(QTreeUtil.mirrorV(image))
      case "3" => println(QTreeUtil.mirrorH(image))
      case "4" => println(QTreeUtil.rotateL(image))
      case "5" => println(QTreeUtil.rotateR(image))
      case "6" => effects(scanner, image)
      case "0" => return
    }
    menu(scanner, file)
  }

  private def effects[A](scanner: Scanner, image: QTree[Coords]): Unit = {
    print("\nEfeitos: " +
      "\n1 - Pure Noise" +
      "\n2 - NotPure Noise" +
      "\n3 - Contrast" +
      "\n4 - Sepia" +
      "\n0 - Sair" +
      "\nInput: ")
    val option = scanner.nextLine()
    option match {
      case "1" => println(mapColorEffect_1(MyRandom(2), image))
      case "2" => println(mapColorEffect(notPureNoise, image))
      case "3" => println(mapColorEffect(contrast, image))
      case "4" => println(mapColorEffect(sepia, image))
      case "0" =>
    }
  }
}
