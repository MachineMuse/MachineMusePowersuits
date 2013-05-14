package net.machinemuse.powersuits.client.render.modelspec

import scala.xml.{NodeSeq, XML}
import net.machinemuse.general.MuseLogger
import net.minecraft.util.Vec3
import net.machinemuse.general.geometry.Colour
import java.awt.Color
import net.machinemuse.utils.MuseStringUtils
import java.net.URL


/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:44 AM, 4/28/13
 */
object ModelSpecXMLReader {
  def parseFile(file: URL) = {
    val xml = XML.load(file)
    (xml \\ "model") foreach {
      modelnode => parseModel(modelnode)
    }
  }

  def parseModel(modelnode: NodeSeq) = {
    val file = (modelnode \ "@file").text
    val textures = (modelnode \ "@textures").text.split(",")
    val offset = parseVector((modelnode \ "@offset").text)
    val rotation = parseVector((modelnode \ "@rotation").text)

    ModelRegistry.loadModel(file) match {
      case Some(m) => {
        val modelspec = new ModelSpec(m, textures, offset, rotation, file)
        val existingspec = ModelRegistry.put(MuseStringUtils.extractName(file), modelspec)
        (modelnode \ "binding").foreach {
          bindingnode => parseBinding(bindingnode, existingspec)
        }
      }
      case None => MuseLogger logError "Model file " + file + " not found! D:"
    }

  }

  def parseBinding(bindingnode: NodeSeq, modelspec: ModelSpec) = {
    val slot = parseInt((bindingnode \ "@slot").text)
    val target = parseTarget((bindingnode \ "@target").text)
    slot.map(slot => {
      target.map(target =>
        (bindingnode \ "part").foreach {
          partnode =>
            parseParts(partnode, modelspec, slot, target)
        })
    })
  }

  def parseParts(partNode: NodeSeq, modelspec: ModelSpec, slot: Int, target: MorphTarget) = {
    val defaultcolor = parseColour((partNode \ "@defaultcolor").text)
    val defaultglow = parseBool((partNode \ "@defaultglow").text)
    val name = (partNode \ "@name").text
    val polygroup = validatePolygroup((partNode \ "@polygroup").text, modelspec)
    polygroup.map(polygroup => {
      val partspec = new ModelPartSpec(modelspec, target, polygroup, slot, 0, defaultglow.getOrElse(false), name)
      modelspec.put(polygroup, partspec)
    })
  }

  def validatePolygroup(s: String, m: ModelSpec): Option[String] = {
    val it = m.model.groupObjects.iterator
    while (it.hasNext) {
      if (it.next().name.equals(s)) return Some(s)
    }
    return None
  }

  def parseBool(s: String): Option[Boolean] = {
    try Some(s.toBoolean) catch {
      case _: Throwable => None
    }
  }

  def parseColour(s: String): Option[Colour] = {
    try {
      val c = Color.decode(s)
      Some(new Colour(c.getRed, c.getGreen, c.getBlue, c.getAlpha))
    } catch {
      case _: Throwable => None
    }
  }

  def parseTarget(s: String): Option[MorphTarget] = {
    s.toLowerCase match {
      case "head" => Some(Head)
      case "body" => Some(Body)
      case "leftarm" => Some(LeftArm)
      case "rightarm" => Some(RightArm)
      case "leftleg" => Some(LeftLeg)
      case "rightleg" => Some(RightLeg)
      case "cloak" => Some(Cloak)
      case _ => None
    }
  }

  def parseInt(s: String): Option[Int] = {
    try Some(s.toInt) catch {
      case _: Throwable => None
    }
  }

  def parseVector(s: String): Option[Vec3] = {
    try {
      val ss = s.split(",")
      val x = ss(0).toDouble
      val y = ss(1).toDouble
      val z = ss(2).toDouble
      Some(Vec3.createVectorHelper(x, y, z))
    } catch {
      case _: Throwable => None
    }
  }
}
