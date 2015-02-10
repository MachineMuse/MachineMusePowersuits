package net.machinemuse.powersuits.client.render.item

import net.minecraft.nbt.NBTTagCompound

object ArmorModel {
  def instance: ArmorModel = ArmorModelInstance.getInstance()
}

trait ArmorModel {
  var renderSpec: NBTTagCompound = null
  var visibleSection: Int = 0
}
