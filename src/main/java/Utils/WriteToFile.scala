package Utils

import java.io.{BufferedWriter, File, FileNotFoundException, FileWriter}
import Utils.Config._

import wvlet.airframe._
import wvlet.log.LogSupport

/**
  * Outils permettant d'ecrire les Iterator, Array et Map dans des fichiers
  */


// case class produit(IdpProd: String, nbVentes: String, magasins: String)

object WriteToFile extends LogSupport {

  /**
    * Ecriture d'un Iterator de Array[String] dans un fichier avec séparateur entre les
    * elements du Array. Le Fichier contiendra un element(Array[String]) par ligne.
    *
    * @param MyIterator Iterator de Array[String] que l'on souhaite enregistrer
    * @param FileName   Nom du fichier avec l'extension dans lequel on souhaite sauvegarder l'Iterator
    * @param sep        Séparateur servant à séparer les elements du Array[String] de l'Iterator
    */
  def iteratorToFile(MyIterator: Iterator[Array[String]], FileName: String, append: Boolean = false) = {
    val file = new File(FileName)
    val writer = new BufferedWriter(new FileWriter(file))

    try {
      for (line <- MyIterator) {
        writer.write(line.mkString(sep) + "\n")
      }
    } catch {
      case ex: FileNotFoundException =>
        debug("Les fichiers" + FileName + " est inexistant")
        throw ex
    }
    finally {
      writer.close()
    }
  }

  /**
    * Ecriture d'un Iterator de String dans un fichier qui contiendra
    * un element(String) par ligne.
    *
    * @param MyIterator Iterator de String que l'on souhaite enregistrer
    * @param fileName   Nom du fichier avec l'extension dans lequel on souhaite sauvegarder l'Iterator
    */
  def iteratorToFile2(MyIterator: Iterator[String], fileName: String, append: Boolean = false) = {
    val file = new File(fileName)
    val writer = new BufferedWriter(new FileWriter(file))

    try {
      for (line <- MyIterator) {
        if (append) writer.append(line + "\n") else writer.write(line + "\n")
      }
    } catch {
      case ex: FileNotFoundException =>
        debug("Les fichiers" + fileName + " est inexistant")
        throw ex
    } finally {
      writer.close()
    }
  }

  /**
    * Permet d'enregistrer un Array de String dans un fichier
    * qui contienra un element par ligne.
    *
    * @param MyArray  Array de String que l'on souhaite écrire
    * @param fileName Nom du fichier avec l'extension dans lequel on souhaite sauvegarder l'Iterator
    */
  def arrayToFile(MyArray: Array[String], fileName: String, append: Boolean = false) = {
    val writer = new BufferedWriter(new FileWriter(fileName))

    try {
      MyArray.foreach(elt => writer.write(elt + "\n"))
    } catch {
      case ex: FileNotFoundException =>
        debug("Les fichiers" + fileName + " est inexistant")
        throw ex
    } finally {
      writer.close()
    }
  }

  /**
    * Permet d'enregistrer une Map de [String, Int] dans un fichier contenant
    * un element par ligne
    * exemple : Pour séparateur '|'
    * 452|12
    * 123|3
    * 12|523
    *
    * @param MyMap    Map que l'on souhaite écrire
    * @param fileName Nom du fichier avec l'extension dans lequel on souhaite sauvegarder l'Iterator
    * @param sep      Séparateur permettant de séparer pour chaque element de la Map le
    *                 String du Int
    */
  def mapToFile(MyMap: Map[String, Int], fileName: String, append: Boolean = false) = {
    val writer = new BufferedWriter(new FileWriter(fileName, append))

    try {
      MyMap.foreach(elt => writer.write(elt._1 + sep + elt._2.toString + "\n"))
    } catch {
      case ex: FileNotFoundException =>
        debug("Les fichiers" + fileName + " est inexistant")
        throw ex
    } finally {
      writer.close()
    }
  }

  /**
    *
    * @param objectToSave
    * @param fileName
    * @param append
    * @tparam T
    * @return
    */
  def writeToFile(objectToSave: Any, fileName: String, append: Boolean = false) = objectToSave match {
    case p: Array[String] => arrayToFile(p, fileName, append)
    case p: Iterator[String] => iteratorToFile2(p, fileName, append)
    case p: Iterator[Array[String]] => iteratorToFile(p, fileName, append)
    case p: Map[String, Int] => mapToFile(p, fileName, append)
    case _ => None ; warn("Type incorrect")
  }
}
