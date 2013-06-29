package net.machinemuse.powersuits.common

import net.minecraft.util.StringTranslate
import java.io._
import net.machinemuse.general.MuseLogger
import java.util.Properties
import cpw.mods.fml.common.registry.LanguageRegistry

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:34 PM, 6/28/13
 */
object Localization {
  val LANG_PATH = "/mods/mmmPowersuits/lang/"
  var extractedLanguage = ""

  def getCurrentLanguage = StringTranslate.getInstance().getCurrentLanguage

  def loadCurrentLanguage() {
    val lang = getCurrentLanguage
    try {
      val inputStream: InputStream = this.getClass.getResourceAsStream(LANG_PATH + lang + ".lang")
      val langPack: Properties = new Properties
      langPack.load(inputStream)
      LanguageRegistry.instance.addStringLocalization(langPack, lang)
    } catch {
      case e: Exception => {
        e.printStackTrace()
        MuseLogger.logError("Couldn't read MPS localizations for language " + lang + " :(")
      }
    }
  }
}
