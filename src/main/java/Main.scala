import TopVentes.ventesMagasins
import Utils.Config._
import Utils.{Config, Misc, WriteToFile}

import scala.io.Source
import wvlet.log.LogSupport

object Main extends LogSupport {

  def main(args: Array[String]): Unit = {

    //Creation des dossiers contenant les resultats
    Misc.makeResultsDirectory()

    // AJOUTER GESTION DES DATES
    var date = "20170515"
    if (args.length != 0) {
      date = args(0)
    }

    val montestregex = "transaction_blablabla_magasins_truc_20170505.data"
    val montestregex2 = "transction_lalalal_20160505.ext"
    val regex = "^transaction.*" + date + ext + "$".r

    // TETS
    montestregex.split(regex).foreach(elt => println(elt + "\n"))
    regex.replaceAll(montestregex, "")

    //regex(suffix)
    println("MON TEST REGEX : " + montestregex2.matches(regex))

    info("Hello airframe-log!")
    warn("This is a warning message")
    debug("debug messsages will not be shown by default")

    import akka.actor.Actor
    import akka.actor.ActorSystem
    import akka.actor.Props

    // (1) changed the constructor here
    class HelloActor(myName: String) extends Actor {
      def receive = {
        // (2) changed these println statements
        case "hello" => println("hello from %s".format(myName))
        case _ => println("'huh?', said %s".format(myName))
      }
    }


    val system = ActorSystem("HelloSystem")
    // (3) changed this line of code
    val helloActor = system.actorOf(Props(new HelloActor("Fred")), name = "helloactor")
    helloActor ! "hello"
    helloActor ! "buenos dias"


    // !TESTS

    println("1. === Enregistrement de la liste des magasins === \n")

    var listmagasins = ventesMagasins.listerMagasins(dirData, date)

    val listMagasinsFileName = dir + "Liste_magasins" + ext
    WriteToFile.writeToFile(listmagasins, listMagasinsFileName)


    println("2. === Ecriture des fichiers de ventes pour chaque magasins === \n")

    listmagasins = ventesMagasins.listerMagasins(dirData, date)

    val transactionFileName = dirData + "transactions_" + date + Config.ext
    ventesMagasins.ecrireVentesParMagasin(listmagasins, dir, transactionFileName, date)

    println("3. === Calcul des ventes total par produit par magasin === \n")

    listmagasins = ventesMagasins.listerMagasins(dirData, date)

    ventesMagasins.ecrireVentesParProduit(listmagasins, dir, date)


    // TESTS
    // ETAPE 1 : creation 1 fichier par article par magasin
    listmagasins = ventesMagasins.listerMagasins(dirData, date)

    var fileTest = "transaction-2a4b6b81-5aa2-4ad8-8ba9-ae1a006e7d71_20170514"
    var idMaxProduit = 0

    println("Identifiant max du produit: " + idMaxProduit)
    println(" ETAPE 01 : Creation fichiers par produits pour un magasin")


    listmagasins.foreach(
      magasin => {
        fileTest = dir + dirVentesMagasins + "transaction-" + magasin + "_" + date + ext

        idMaxProduit = Source.fromFile(fileTest).getLines()
          .map(_.split('|')(0).toInt).max

        for (i <- 0 to idMaxProduit) {
          WriteToFile.writeToFile(
            Source.fromFile(fileTest).getLines()
              .filter(_.split('|')(0).toInt == i),
            Config.dir + "test_id_produit_pas_magasin\\" + "test_" + magasin + "_" + date + "_id_produit_" + i + ext
          )
        }
      }
    )

    // ETAPE 2 : regrouper l'ensemble des artcile avec un fichier par magasins
    // (articles sans doublons : calcul somme ventes par articles)

    println("Identifiant max du produit: " + idMaxProduit)
    println("ETAPE 02: Creation fichiers de ventes par magasins sans doublons (calcul somme)")

    listmagasins = ventesMagasins.listerMagasins(Config.dirData, date)

    listmagasins.foreach(
      magasin => {
        for (i <- 0 to idMaxProduit) {
          WriteToFile.writeToFile(
            Source.fromFile(dir + "test_id_produit_pas_magasin\\" + "test_" + magasin + "_" + date + "_id_produit_" + i + ext).getLines()
              .map(x => x.split(sep(0)))
              .map(tab => (tab(0), tab(1).toInt)).toList
              .groupBy(_._1)
              .map(line => (line._1, line._2.map(line => line._2).sum)),
            dir + "test_produit_par_magasin_sans_doublons\\" + "test_" + magasin + "_" + date + ext,
            true
          )
        }
      }
    )


    println("4. === Calcul du top 100 des ventes par magasin === \n")

    listmagasins = ventesMagasins.listerMagasins(Config.dirData, date)

    ventesMagasins.ecrireTopMagasin(listmagasins, date)


    println("5. === Calcul du top 100 des ventes de produits (GLOBAL) === \n")

    val fileVentesTotalParProduit = Config.dir + "ventes_total_" + date + Config.ext

    listmagasins = ventesMagasins.listerMagasins(Config.dirData, date)

    println("ventes gloales")
    ventesMagasins.ecrireVentesGlobal(listmagasins, fileVentesTotalParProduit, date)
    println("ecrireTopGLobal")
    ventesMagasins.ecrireTopGlobal(fileVentesTotalParProduit, date)
  }
}