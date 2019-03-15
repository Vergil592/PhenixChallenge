package Utils

import Utils.Config._

import java.io.File

/**
  * Outils diverses
  */
object Misc {

  /**
    * Permet de créer le dossier avec le nom spécifier dans dirNames
    *
    * @param dirName Nim du dossier à créer
    * @return Retourne true si le dossier existe ou à bien été crée,
    *         false sinon
    */
  def makeDirectory(dirName: String) ={
    val dir = new File(dirName)

    if (!dir.exists()) dir.mkdir() else true
  }

  /**
    * Crée l'ensemble des dossier contenant les resulstats ventes
    */
  def makeResultsDirectory() ={

    makeDirectory(dir)

    makeDirectory(dir + dirProduitsParMagasins)
    makeDirectory(dir + dirVentesMagasins)
    makeDirectory(dir + dirVentesParMagasins)
    makeDirectory(dir + dirTop100)
  }

  /**
    * Permet de comparer deux element.
    * Retourne vrai si le premier element est strictement
    * plus grand que le second.
    *
    * @param elt1 Premier entier à comparer
    * @param elt2 Second entier à comparer
    * @return Retourne true si elt1 > elt2 et false sinon
    */
  def sortByNbVentes(elt1: Int, elt2: Int) = {
    elt1.toInt > elt2.toInt
  }

}
