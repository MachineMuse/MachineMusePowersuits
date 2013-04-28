package net.machinemuse.powersuits.client.render


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:44 AM, 4/28/13
 */
object ModelSpecParser {
  def parseFile(file: String) {
    val fileElems = scala.xml.XML.loadFile(file)

  }

  class Item(val name: String)

  class Filename(val path: String)

  class Anchor(val bodypart: String)

}
