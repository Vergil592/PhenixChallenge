package TopVentes

import java.io.{File, FileNotFoundException}

import scala.io.Source
import Utils.{Misc, WriteToFile}
import Utils.Config._
import wvlet.log.LogSupport

object ventesMagasins extends LogSupport {

  /**
    * Permet de récuperer la liste des magasins dans un Iterator[String]
    * à partir du dossier contenant les references produits par magasin
    *
    * @param dir  Nom du fichier
    * @param date Date à laquelle on souhaite récuperer
    *             l'ensemble des identifiants des magasins
    * @return Retourne un Iterator[String] contenant la liste des magasins
    */
  def listerMagasins(dir: String, date: String): Iterator[String] = {
    val regex = "reference_prod-"

    val file = new File(dir)

    try {
      file.listFiles.filter(_.isFile)
        .filter(_.getName.startsWith(regex))
        .filter(_.getName.endsWith("_" + date + ext))
        .map(_.getPath)
        .toIterator
        //     .map(line => line.replaceFirst(regex, ))
        .map(line => line.replace(dir + regex, "").replace("_" + date + ext, ""))

    } catch {
      case ex: NullPointerException =>
        debug("Dossier " + dir + " reference_prod sont manquant pour la date voulue.")
        throw ex
    }
  }


  /**
    * Permet de filtrer les ventes d'un magasin. A partir d'un fichier de transaction d'une journée donnée,
    * on récupère le numéro des produits et les quantités vendues du jour
    * pour le magasin voulu pour chaque transaction.
    *
    * @param magasin  Identifiant du magasin dont on souahaite filtrer
    * @param fileName Nom avec le chemin du fichier contenant les l'ensemble des
    *                 transactions d'une journée
    * @return Retourne un Iterator[String] contenant les numéros de produits et les quantité vendues
    *         pour un magasin donné
    */
  def filtrerVentesMagasin(magasin: String, fileName: String) = {
    try {
      val source = Source.fromFile(fileName)

      source.getLines()
        .filter(line => line.contains(magasin) && (!line.endsWith("0")))
        .map(x => Array(x.split(sep(0))(3), x.split(sep(0))(4)).mkString(sep))
    } catch {
      case ex: FileNotFoundException =>
        println("Le fichier " + fileName + " est inexistant")
        throw ex
      case ex: NullPointerException =>
        debug("L'un des dossiers du chemin" + fileName + " est inexistant.")
        throw ex
    }
  }


  /**
    *
    * Permet d'ecrire l'ensemble des transactions d'une journée en créant un fichier
    * par magasin. Chaque fichier ne contiendra que le numéro de produit et le nombre de produit
    * vendu pour un jour donné.
    *
    * @param listMagasins Iterator contenant la liste des magasins
    * @param dir          Dossier contenant les resultats
    * @param fileName     Nom du fichier contenant l'ensemble des ventes pour un jour donné pour tous les magasins
    * @param date         Date des transactions pour le fichier indiqué dans fileName
    */
  def ecrireVentesParMagasin(listMagasins: Iterator[String], dir: String, fileName: String, date: String) {
    if (listMagasins.isEmpty) {
      false
    } else {
      var isFilesWrite = true
      listMagasins.foreach(magasin =>
        isFilesWrite = isFilesWrite && this.ecrireVentesMagasin(magasin, dir, fileName, date)
      )
      isFilesWrite
    }
  }


  /**
    * Permet d'ecrire l'ensemble des transactions d'une journée pour un magasin en créant un fichier
    * par magasin. Chaque fichier ne contiendra que le numéro de produit et le nombre de produit
    * vendu pour un jour donné.
    *
    * @param magasin  Identifiant du magasin
    * @param dir      Dossier contenant les resultats
    * @param fileName Nom du fichier contenant l'ensemble des vente pour un jour donné
    * @param date     Date souhaité
    */
  def ecrireVentesMagasin(magasin: String, dir: String, fileName: String, date: String) = {
    try {
      val source = Source.fromFile(fileName)

      WriteToFile.writeToFile(
        this.filtrerVentesMagasin(magasin, fileName)
        , dir + dirVentesMagasins + "transaction-" + magasin + "_" + date + ext
      )
      source.close
      true
    } catch {
      case ex: FileNotFoundException =>
        debug("Le fichier " + fileName + " est inexistant")
        throw ex
      case ex: NullPointerException =>
        debug("L'un des dossiers du chemin" + fileName + " est inexistant.")
        throw ex
    }
  }


  /**
    *
    * Permet de calculer, pour un magasin, le total de ventes par produit
    * à partir des fichiers de transactions par magasin.
    * Le calcul de l'ensemble des ventes pour un magasin se fait à partir du
    * fichier contenant l'ensemble des transactions pour un magasin donné.
    * Ces fichiers sont contenus dans le dossier dirVentesMagasins
    *
    * @param magasin Identifiant du magasin
    * @param date    Date à laquelle on souhaite calculer l'ensemble des ventes par produit pour le magasin
    * @param dir     Dossier contenant les resultats
    * @return Retourne une Map contenant comme clé le numero de produit et comme valeur le total de vente pour le magasin
    */
  def calculVentesParProduit(magasin: String, date: String, dir: String): Predef.Map[String, Int] = {
    val FileName = dir + dirVentesMagasins + "transaction-" + magasin + "_" + date + ext

    try {
      val source = Source.fromFile(FileName)

      source.getLines()
        .map(x => x.split(sep(0)))
        .map(tab => (tab(0), tab(1).toInt)).toList
        .groupBy(_._1)
        .map(line => (line._1, line._2.map(line => line._2).sum))
    } catch {
      case ex: FileNotFoundException => {
        debug("Les fichiers" + FileName + "contenant les ventes du jour pour un magasin sont manquants")
        throw ex
      }
    }
  }

  /**
    * Permet, à partir des fichiers comportant l'ensemble des transactions par magasin obtenus
    * (méthode ecrireVentesParMagasin), d'enregistrer le total des ventes par produits
    * pour chaque magasin
    *
    * @param listmagasins liste des magasins dont on souhaite obtenir le total des ventes par produit
    * @param dir          Dossier contenant les resultats
    * @param date         Date à laquelle on souhaite calculer l'ensemble des ventes par produit pour les magasins indiqués
    */
  def ecrireVentesParProduit(listmagasins: Iterator[String], dir: String, date: String) {
    listmagasins.foreach(
      magasin =>
        WriteToFile.writeToFile(
          calculVentesParProduit(magasin, date, dir),
          dir + dirProduitsParMagasins + magasin + "-prod_" + date + ext
        )
    )
  }


  /**
    *
    * Permet d'écrire le top 100 pour l'ensemble des magasins indiqués dans listMagasins.
    *
    * @param listMagasins Liste de l'ensemble des magasins dont on souhaite obtenir
    *                     le top 100 des ventes
    * @param date         Date à laquelle on souhaite le top 100 des ventes
    */
  def ecrireTopMagasin(listMagasins: Iterator[String], date: String): Unit = {
    listMagasins.foreach(
      magasin =>
        WriteToFile.writeToFile(
          getTop(dir + dirProduitsParMagasins + magasin + "-prod_" + date + ext, date),
          dir + dirTop100 + "top_100_ventes_" + magasin + "_" + date + ext
        )
    )
  }

  /**
    * Permet de récuperer sous forme d'un Array[String] le top 100
    * de ventes pour un fichier contenant les numeros de produits
    * et les quantités vendues ne présentant pas de doublons de numero de produit.
    *
    * @param fromFile Fichier à partir duquelle on calcule le top 100
    *                 se fichiers doit etre au format: numero_produit|nombre_vente
    *                 sans contenir de doublons pour le numero_produit
    * @param date     Date à laquelle on souhaite calculer le top 100
    * @return Retourne le top 100 des ventes pour un magasin ou GLOBAL
    */
  private def getTop(fromFile: String, date: String): Array[String] = {

    val source = Source.fromFile(fromFile)

    try {
      source.getLines().toArray.sortWith(
        (elt1, elt2) =>
          Misc.sortByNbVentes(elt1.split(sep(0))(1).toInt, elt2.split(sep(0))(1).toInt)
      ).slice(0, 100)
    } catch {
      case ex: FileNotFoundException =>
        debug("Le(s) fichier(s) " + fromFile + " ne sont pas présents ")
        throw ex

    } finally {
      source.close()
    }
  }

  /**
    * Permet de récuperer, pour un magasin, sous forme d'une Map l'ensemble des numéros de produits (clé)
    * et le nombre de ventes correspondant à partir des fichiers contenant les produits vendus par magasins
    * sans doublons (dossier dirProduitsParMagasins)
    *
    * @param magasin Identifiant du magasin
    * @param myMap   Map contenant les produits (clé) et le nombre de nombre de ventes (valeur) pour
    *                un ou plusieurs magasins autre que celui renseigné dans magasin
    * @param date    Date à laquelle ou souhaite obtenir les ventes
    */
  def ventesProduitsAsMap(magasin: String, myMap: scala.collection.mutable.Map[String, Int], date: String): Unit = {
    val source = Source.fromFile(dir + dirProduitsParMagasins + magasin + "-prod_" + date + ext)

    source.getLines().foreach(
      line => {
        var tabLine = line.split(sep(0))
        if (myMap.isDefinedAt(tabLine(0))) {
          myMap(tabLine(0)) += line(1)
        } else {
          myMap += (tabLine(0) -> tabLine(1).toInt)
        }
      }
    )
  }

  /**
    * Permet d'ecrire dans un fichier filVentesTotalParProduit les quantités de ventes par produits
    * à partir des fichiers de ventes de produits par magasins obtenu par la méthode ecrireVentesParProduit
    *
    * @param listMagasins              Liste des magasins
    * @param fileVentesTotalParProduit Fichier qui va contenir l'ensemble des ventes (numerode produit et nombre de ventes)
    *                                  pour l'ensemble des magasins dans listMagasins
    * @param date                      Date à laquelle ou souhaite obtenir les ventes global
    */
  def ecrireVentesGlobal(listMagasins: Iterator[String], fileVentesTotalParProduit: String, date: String): Unit = {

    var myMap = scala.collection.mutable.Map[String, Int]()
    var myArray = scala.collection.mutable.MutableList[String]()

    listMagasins.foreach(
      magasin =>
        ventesProduitsAsMap(magasin, myMap, date)
    )
    WriteToFile.writeToFile(myMap.toMap, fileVentesTotalParProduit)
  }

  /**
    * Permet d'ecrire le top 100 des ventes global pour une date donnée une fois
    * que le fichier dirVentesTotalParProduit contenant les quanités de ventes par produits
    * obtenu par la méthode ecrireVentesGlobal
    *
    * @param dirVentesTotalParProduit Fichier contenant l'ensemble des ventes (numerode produit et nombre de ventes)
    *                                 pour un ensemble de magasins
    * @param date                     Date à laquelle on souhaite obtenir le top 100 des ventes global
    */
  def ecrireTopGlobal(dirVentesTotalParProduit: String, date: String): Unit = {
    val topVentes = getTop(dirVentesTotalParProduit, date)

    WriteToFile.writeToFile(topVentes, dir + dirTop100 + "top_100_ventes_GLOBAL_" + date + ext)
  }

}
