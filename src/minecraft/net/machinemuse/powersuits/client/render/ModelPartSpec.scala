package net.machinemuse.powersuits.client.render

import net.machinemuse.general.geometry.Colour
import net.minecraftforge.client.model.obj.WavefrontObject
import net.machinemuse.utils.MuseRenderer
import net.minecraft.nbt.{NBTTagString, NBTTagList, NBTTagCompound}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:06 AM, 4/28/13
 */
class ModelPartSpec(
                     val model: WavefrontObject,
                     val partNames: Array[String],
                     val colour: Colour = Colour.WHITE,
                     val glow: Boolean = false,
                     val displayName: String) {
  def this(nbt: NBTTagCompound) = {
    this(
      ModelRegistry.getModel(nbt.getString("model")),
      convert.toStringArray(nbt.getTagList("parts")),
      new Colour(nbt.getInteger("colour")),
      nbt.getBoolean("glow"),
      nbt.getString("name")
    )
  }

  def render = {
    if (glow) MuseRenderer.glowOn
    if (colour != null) colour.doGL
    for (i <- partNames) {
      model.renderPart(i)
    }
    if (colour != null) Colour.WHITE.doGL
    if (glow) MuseRenderer.glowOff
  }

  def toNBT: NBTTagCompound = {
    val nbt = new NBTTagCompound()
    nbt.setBoolean("glow", glow)
    nbt.setTag("parts", convert.toTagList(partNames))
    nbt.setInteger("colour", colour.getInt)
    nbt.setString("model", ModelRegistry.getName(model))
    nbt.setString("name", displayName)
    nbt

  }
}

object convert {
  def toStringArray(list: NBTTagList): Array[String] = {
    val strings = new Array[String](list.tagCount())
    for (i <- 0 until list.tagCount()) {
      strings(i) = list.tagAt(i).asInstanceOf[NBTTagString].data
    }
    strings
  }

  def toTagList(list: Array[String]): NBTTagList = {
    val strings = new NBTTagList
    for (i <- 0 until list.length) {
      strings.appendTag(new NBTTagString(list(i)))
    }
    strings
  }
}