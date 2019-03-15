package TopVentes

import Utils.Config._
import org.scalatest.FunSuite
import scala.io.Source

class ventesMagasinsTest extends FunSuite {

  val idMagasinExistant = "e3d54d00-18be-45e1-b648-41147638bafe"
  val dateMagasinExistant = "20170516"

  /**
    * Test la méthode dans le cas ou le dossier existe et qu'il existe au moins
    * un fichiers de reference produit.
    */
  test("testListerMagasins") {
    val iteratorToCompare = Iterator()
    val listMagasins = ventesMagasins.listerMagasins(dirTests, dateMagasinExistant)

    assert(listMagasins.length === 2)
    assert(listMagasins.zip(iteratorToCompare).forall(l => l._2 == l._1))
  }

  /**
    * Test la méthode si les fichiers de reference produit n'existe pas
    * à la date indiqué pour le dossier existant.
    */
  test("testListerMagasinsNoFileInDirectory") {
    val listMagasins = ventesMagasins.listerMagasins(dirTests, "20170519")

    assert(listMagasins.isEmpty)
  }

  /**
    * Test la méthode listerMagasins si le dossier spécifier n'existe pas.
    */
  test("testListerMagasinsDirectoryNotFound") {
    val listMagasins = ventesMagasins.listerMagasins("dirThatDontExist", "20170519")
    assert(listMagasins.isEmpty)
  }

  /**
    * Test si, à partir du fichier de transaction, le filtre
    * des transaction pour un magasin c'est bien déroulé
    */
  test("testFiltrerVentesMagasin") {
    val date = "20170516"
    val fichierTransaction = dirTests + "transactions_" + date + ext

    val transactionsMagasin = ventesMagasins.filtrerVentesMagasin(idMagasinExistant, fichierTransaction)
    val iteratorToCompare = Iterator("1" + sep + "1", "2" + sep + "2", "2" + sep + "3", "1" + sep + "4")

    assert(transactionsMagasin.length == 4)
    assert(transactionsMagasin.zip(iteratorToCompare).forall(l => l._2 == l._1))
  }

  /**
    * Test si la méthode retourne bien in
    */
  test("testFiltrerVentesMagasinDirectoryNotFound") {
    val date = "20170516"
    val fichierTransaction = "dirThatDontExist\\" + "transactions_" + date + ext

    val transactionsMagasin = ventesMagasins.filtrerVentesMagasin(idMagasinExistant, fichierTransaction)

    assert(transactionsMagasin.isEmpty)
  }


  test("testFiltrerVentesMagasinFileNotFound") {
    val date = "20170519"
    val fichierTransaction = dirTests + "transactions_" + date + ext

    val transactionsMagasin = ventesMagasins.filtrerVentesMagasin(idMagasinExistant, fichierTransaction)

    assert(transactionsMagasin.isEmpty)
  }


  test("testEcrireVentesMagasin") {
    val date = "20170516"
    val fichierTransaction = dirTests + "transactions_" + date + ext

    ventesMagasins.ecrireVentesMagasin(idMagasinExistant, dirTests, fichierTransaction, date)

    val ventesMagasin = Source.fromFile(dirTests + dirVentesMagasins + "transaction-" + idMagasinExistant + "_" + date + ext).getLines()
    val iteratorToCompare = Iterator("1|1", "2|2", "3|3", "1|4")

    assert(ventesMagasin.zip(iteratorToCompare).forall(l => l._2 == l._1))
  }


  test("testEcrireVentesMagasinFileNotFound") {
    val date = "20170519"
    val fichierTransaction = dirTests + "transactions_" + date + ext

    assert(!ventesMagasins.ecrireVentesMagasin(idMagasinExistant, dirTests, fichierTransaction, date))
  }


  test("testEcrireVentesParMagasinEcritureFichiers") {
    val date = "20170516"
    val listMagasins = Iterator("dd43720c-be43-41b6-bc4a-ac4beabd0d9b", "e3d54d00-18be-45e1-b648-41147638bafe")

  //  assert(VentesMagasins.ecrireVentesParMagasin(listMagasins, dirTests, dirTests + "transactions_" + date + ext, date))
  }


  test("testEcrireVentesParMagasinVerificationValeurs") {
    val date = "20170516"
    val listMagasins = Iterator("dd43720c-be43-41b6-bc4a-ac4beabd0d9b", "e3d54d00-18be-45e1-b648-41147638bafe")

    val listFichiersAttendus = Array(
      dirTests + dirVentesMagasins + "transaction-dd43720c-be43-41b6-bc4a-ac4beabd0d9b_" + date + ext,
      dirTests + dirVentesMagasins + "transaction-e3d54d00-18be-45e1-b648-41147638bafe_" + date + ext
    )
    val listIteratorAttendu = Array(
      Iterator("1|1", "2|2"),
      Iterator("1|1", "2|2", "3|3", "1|4")
    )

    ventesMagasins.ecrireVentesParMagasin(listMagasins, dirTests, dirTests + "transactions_" + date + ext, date)

    var ventesMagasin = Source.fromFile(listFichiersAttendus(0)).getLines()

    assert(ventesMagasin.zip(listIteratorAttendu(0)).forall(l => l._2 == l._1))

    ventesMagasin = Source.fromFile(listFichiersAttendus(1)).getLines()
    assert(ventesMagasin.zip(listIteratorAttendu(1)).forall(l => l._2 == l._1))
  }


  test("testEcrireVentesParMagasinFileNotFound") {
    val date = "20170519"
    val listMagasins = Iterator("dd43720c-be43-41b6-bc4a-ac4beabd0d9b", "e3d54d00-18be-45e1-b648-41147638bafe_20170516" +
      "")

  //  assert(!VentesMagasins.ecrireVentesParMagasin(listMagasins, dirTests, dirTests + "transactions_" + date + ext, date))
  }



  // TESTS A COMPLETER

  test("testCalculVentesParProduit") {
    val date = "20170516"

    val ventesParProduit = ventesMagasins.calculVentesParProduit(idMagasinExistant, date, dirTests)

  }

  test("testGetTop") {

  }


  test("testEcrireTopMagasin") {

  }

  test("testVentesProduitsAsMap") {

  }

  test("testEcrireVentesParProduit") {

  }


}
