package Iscte

import Iscte.QTree._
import Iscte.QTreeUtil._
import java.io.File
import java.util.Scanner
import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

object UserInterface {
  def init(): Unit = {
    println("Qual a imagem que deseja manipular?")
    println("\nImagens: ")
    val images = new File(getClass.getResource("/Iscte/Images").getPath).listFiles.map(_.getName).toList
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
    val image = QTree.makeQTree(BitMap(ImageUtil.readColorImage(getClass.getResource("/Iscte/Images/" + file).getPath)))
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
    val option = Option(scanner.nextLine())
    option match {
      case Some("1") =>
        print("Scale value: ")
        val sc = scanner.nextLine().toDouble
        println(QTreeUtil.scale(sc, image))
      case Some("2") => println(QTreeUtil.mirrorV(image))
      case Some("3") => println(QTreeUtil.mirrorH(image))
      case Some("4") => println(QTreeUtil.rotateL(image))
      case Some("5") => println(QTreeUtil.rotateR(image))
      case Some("6") => effects(scanner, image)
      case _ => return
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
    val option = Option(scanner.nextLine())
    option match {
      case Some("1") => println(mapColorEffect_1(image,Iscte.GUI.FxApp.r))
      case Some("2") => println(mapColorEffect(notPureNoise, image))
      case Some("3") => println(mapColorEffect(contrast, image))
      case Some("4") => println(mapColorEffect(sepia, image))
      case _ =>
    }
  }
}
