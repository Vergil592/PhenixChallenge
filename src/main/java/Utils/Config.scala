package Utils

import com.typesafe.config.ConfigFactory

/**
  * Objet contenant l'ensemble des parametres de configuration du
  * fichier application.conf
  */
object Config {

  val Config = ConfigFactory.load("application.conf")

  /**
    * Dossier ou sont stockées les données initiales
    */
  val dirData = Config.getString("config.dirData")

  /**
    *   Séparateur pour les fichiers
     */
  val sep = Config.getString("config.sep")

  /**
    * Extension des fichiers
    */
  val ext = Config.getString("config.ext")

  /**
    * Dossier ou seront stockés tous les résultats
    */
  val dir = Config.getString("config.dirResults")

  /**
    * Dossiers contenant les resulstats intermediaires
    */
  val dirVentesMagasins = Config.getString("config.dirVentesMagasins")
  val dirVentesParMagasins = Config.getString("config.dirVentesMagasins")
  val dirProduitsParMagasins = Config.getString("config.dirProduitsParMagasins")

  /**
    *
    * Dossier contenant les Top 100 des ventes
    */
  val dirTop100 = Config.getString("config.dirTop100")

  /**
    * Dossier pour effectuer les tests
    */
  val dirTests = Config.getString("config.dirTests")
}
