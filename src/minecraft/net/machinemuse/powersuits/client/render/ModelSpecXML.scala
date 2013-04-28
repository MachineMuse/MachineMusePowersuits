package net.machinemuse.powersuits.client.render

import net.machinemuse.general.geometry.Colour
import scala.xml.XML


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:44 AM, 4/28/13
 */
object ModelSpecXML {
  def parseFile(file: String) {
    val xml = XML.loadFile(file)

  }


  class BodyAttachment(val bodypart: String)

  class ItemAttachment(val itemSlot: String)

  class ModelFile(val filepath: String)

  class ModelPart(val partname: String)

  class DefaultColour(val colour: Colour)

  class DefaultGlow(val glow: Boolean)

}
