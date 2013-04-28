package net.machinemuse.powersuits.client.render

import net.minecraftforge.client.model.obj.WavefrontObject
import net.minecraftforge.client.model.AdvancedModelLoader
import java.util

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 */
object ModelRegistry {
  val nameMap = new util.HashMap[String, WavefrontObject]
  val objMap = new util.HashMap[WavefrontObject, String]

  def getModel(name: String) = nameMap.get(name)


  def setModel(name: String, model: WavefrontObject) = {
    nameMap.put(name, model)
    objMap.put(model, name)
  }

  def getName(model: WavefrontObject) = objMap.get(model)

  def loadModel(resource: String) = {
    val name = stripName(resource)
    if (nameMap.containsKey(name)) {
      nameMap.get(name)
    } else {
      val model = AdvancedModelLoader.loadModel(resource).asInstanceOf[WavefrontObject]
      setModel(name, model)
      name
    }

  }

  def stripName(resource: String) = resource.substring(resource.lastIndexOf('/'), resource.lastIndexOf('.'))


}
