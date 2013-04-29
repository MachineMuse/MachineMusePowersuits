package net.machinemuse.powersuits.client.render.modelspec

import net.machinemuse.general.geometry.Colour
import net.minecraft.nbt.NBTTagCompound
import scala.None

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 4:12 AM, 29/04/13
 */
class ModelPartDisplayPrefs(val partSpec: ModelPartSpec,
                            val texture: Option[String],
                            val colour: Option[Colour],
                            val glow: Option[Boolean]
                             ) {
  def getTexture = {
    texture.getOrElse(partSpec.modelSpec.textures.head)
  }

  def getColour = {
    colour.getOrElse(partSpec.defaultcolour)
  }

  def getGlow = {
    glow.getOrElse(partSpec.defaultglow)
  }
}

object ModelNBTPrefs {
  def read(nbt: NBTTagCompound): Option[ModelPartDisplayPrefs] = {
    val model = nbt.getString("model")
    val part = nbt.getString("part")
    val colour = Option(nbt.getInteger("colour")).flatMap(c => Some(new Colour(c)))
    val glow = Option(nbt.getBoolean("glow"))
    val texture = Option(nbt.getString("texture"))

    ModelRegistry.get(model)
      .flatMap(m => m.get(part))
      .flatMap(p => Some(
      new ModelPartDisplayPrefs(p, texture, colour, glow)
    )
    )

  }

  def write(prefs: ModelPartDisplayPrefs): Option[NBTTagCompound] = {
    val modelspec = prefs.partSpec.modelSpec
    val modelname = ModelRegistry.inverse.getOrElse(modelspec, return None)
    val partname = modelspec.inverse.getOrElse(prefs.partSpec, return None)
    val nbt = new NBTTagCompound
    nbt.setString("model", modelname)
    nbt.setString("part", partname)
    prefs.colour.map(c => nbt.setInteger("colour", c.getInt))
    prefs.glow.map(g => nbt.setBoolean("glow", g))
    prefs.texture.map(t => nbt.setString("texture", t))

    Some(nbt)
  }
}