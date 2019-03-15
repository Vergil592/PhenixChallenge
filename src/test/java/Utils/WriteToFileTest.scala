package Utils

import java.io.{File, FileNotFoundException}

import org.scalatest.FunSuite

import scala.io.Source
import Utils.Config._

class WriteToFileTest extends FunSuite {

  val directory = new File(dirTests)
  val dirWriteToFileTest = dirTests + "testWriteToFile\\"
  val sep = ";"

  test("testFileNotFound") {
    val filePath = dirWriteToFileTest + "testFilell\\myFile.data"
    val MyIterator = Iterator(Array("123", "50"), Array("1", "65"), Array("9999", "666"))

    assertThrows[FileNotFoundException] {
      WriteToFile.writeToFile(MyIterator, filePath)
    }
  }

  /**
    * Permet de tester si l'enregistrement d'une Map s'effectue correctement
    *
    */
  test("testMapToFile") {
    val filePath = dirWriteToFileTest + "testMapToFile.data"

    val myMap = Map("123" -> 58, "50" -> 666, "1" -> 1)
    val myArray = Array("123" + sep + "58", "50" + sep + "666", "1" + sep + "1")

    if (!directory.exists()) directory.mkdir()

    assert(directory.exists(), "Le dossier de test est introuvable")

    WriteToFile.writeToFile(myMap, filePath)

    val savedArray = Source.fromFile(filePath).getLines().toArray

    assert(savedArray.length == myArray.length, "Les deux Iterator doivent avoir la même taille")

    assert(savedArray.zip(myArray).forall(l => l._2 == l._1))


  }

  /**
    * Permet de tester si l'enregistrement d'un Array s'effectue correctement
    *
    */
  test("testArrayToFile") {
    val filePath = dirWriteToFileTest + "testArrayToFile.data"

    // Construction d'un Array que l'on  va convertir en Iterator
    val myArray = Array("123|58", "50|666", "1|1")

    if (!directory.exists()) directory.mkdir()

    WriteToFile.writeToFile(myArray, filePath)

    assert(directory.exists(), "Le dossier de test est introuvable")

    val savedArray = Source.fromFile(filePath).getLines().toArray

    assert(savedArray.length == myArray.length, "Les deux Array doivent avoir la même taille")

    assert(savedArray.zip(myArray).forall(l => l._2 == l._1))
  }

  /**
    * Permet de tester si l'enregistrement d'un Iterator s'effectue correctement
    *
    */
  test("testIteratorToFile") {
    val filePath = dirWriteToFileTest + "testIteratorToFile.data"

    // Construction d'un Array que l'on  va convertir en Iterator
    val myArray = Array(Array("123", "50"), Array("1", "65"), Array("9999", "666"))

    if (!directory.exists()) directory.mkdir()

    WriteToFile.writeToFile(myArray.toIterator, filePath)

    assert(directory.exists(), "Le dossier de test est introuvable")

    val savedIterator = Source.fromFile(filePath).getLines()

    assert(savedIterator.length == myArray.toIterator.length, "Les deux Iterator doivent avoir la même taille")

    assert(savedIterator.zip(myArray.toIterator).forall(l => l._2.mkString(sep) == l._1))
  }

}
