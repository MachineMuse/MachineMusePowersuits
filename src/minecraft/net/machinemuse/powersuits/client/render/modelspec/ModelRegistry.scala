package net.machinemuse.powersuits.client.render.modelspec

import net.minecraftforge.client.model.obj.WavefrontObject
import net.minecraftforge.client.model.AdvancedModelLoader
import net.machinemuse.utils.{MuseRegistry, MuseStringUtils}
import net.minecraft.util.Vec3
import net.machinemuse.general.geometry.Colour
import net.machinemuse.general.MuseLogger

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:44 AM, 4/28/13
 */
object ModelRegistry extends MuseRegistry[ModelSpec] {
  def loadModel(resource: String): Option[WavefrontObject] = {
    val name = MuseStringUtils.extractName(resource)
    get(name) match {
      case Some(i) => Some(i.model)
      case None => wrap(resource)
    }
  }

  def wrap(resource: String): Option[WavefrontObject] = {
    MuseLogger.logDebug("Loading " + resource + " as " + MuseStringUtils.extractName(resource))
    AdvancedModelLoader.loadModel(resource) match {
      case m: WavefrontObject => Some(m)
      case _ => MuseLogger.logError("Model loading failed :( " + resource)
    }
  }

}

class ModelSpec(val model: WavefrontObject,
                val textures: Array[String],
                val offset: Option[Vec3],
                val rotation: Option[Vec3]
                 ) extends MuseRegistry[ModelPartSpec] {
  def applyOffsetAndRotation = {

  }

}

class ModelPartSpec(val modelSpec: ModelSpec,
                    val morph: MorphTarget,
                    val partName: String,
                    val slot: Int,
                    val defaultcolour: Colour = Colour.WHITE,
                    val defaultglow: Boolean = false,
                    val displayName: String) {
}