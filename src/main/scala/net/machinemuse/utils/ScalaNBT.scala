package net.machinemuse.utils

import net.minecraft.nbt.NBTTagCompound

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:47 AM, 29/04/13
 */
class ScalaNBT(val nbt: NBTTagCompound) extends NBTTagCompound {
  implicit def wrap(nbt: NBTTagCompound) = new ScalaNBT(nbt)

}
