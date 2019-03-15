package Utils

import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import scala.reflect.macros.ParseException

/**
  * Outils permettant la manipulation de dates.
  */
object dateTools {

  /**
    * Permet de converstir une chaine de caractères au format YYYYMMdd
    * en une date.
    *
    * @param value   Date ecrite sous format du "pattern"
    * @param pattern Format du String écrit dans "value" ex: YYYYMMdd, YYYY-MM-dd
    * @return Retourne la value sous forme d'une Date
    */
  def parseDate(value: String, pattern: String = "yyyymmdd") = {
    try {
      Some(new SimpleDateFormat(pattern).parse(value))
    } catch {
      case e: ParseException => None
    }
  }

  /**
    * Permet de retourner la date actuelle sous le format choisi par
    * le paramètre pattern.
    *
    * @param pattern Format du String écrit dans "value" ex: YYYYMMdd, YYYY-MM-dd
    * @return Retourne la date actuelle en String sous le format du pattern
    */
  def getDate(pattern: String): String ={
    val format = new SimpleDateFormat(pattern)
    format.format(Calendar.getInstance().getTime)
  }

  /**
    * Permet de savoir si la date choisit est dans le future ou non.
    *
    * @param value Date avec laquelle on compare le présent
    * @return Retourne TRUE si la date entrée est dans le futur
    *         et FALSE sinon
    */
  def isFuture(value: Date) = value.after(new Date())
}
