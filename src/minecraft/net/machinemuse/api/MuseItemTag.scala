package net.machinemuse.api

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.item.ItemStack
import java.lang.String

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:58 PM, 8/6/13
 */
object MuseItemTag {
  final val NBTPREFIX: String = "mmmpsmod"

  /**
   * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
   *
   * @param stack
   * @return an NBTTagCompound, may be newly created. If stack is null,
   *         returns null.
   */
  def getMuseItemTag(stack: ItemStack): NBTTagCompound = {
    if (stack == null) {
      return null
    }
    val stackTag: NBTTagCompound = if (stack.hasTagCompound) stack.getTagCompound else new NBTTagCompound
    stack.setTagCompound(stackTag)
    val properties: NBTTagCompound = if (stackTag.hasKey(NBTPREFIX)) stackTag.getCompoundTag(NBTPREFIX) else new NBTTagCompound
    stackTag.setCompoundTag(NBTPREFIX, properties)

    properties
  }

  def getActiveMode(stack: ItemStack): String = {
    val active : String =  getMuseItemTag(stack).getString("Mode")
    active
  }
}
