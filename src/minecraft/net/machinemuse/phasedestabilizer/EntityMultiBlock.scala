package net.machinemuse.phasedestabilizer

import net.minecraft.entity.Entity
import net.minecraft.world.World
import net.minecraft.nbt.NBTTagCompound
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.minecraft.client.renderer.Tessellator

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 5:30 PM, 5/6/13
 */
class EntityMultiBlock(val outerWorld:World) extends Entity(outerWorld) {
  val innerWorld:InnerWorld = new InnerWorld(null,null,null,null,null,null)

  protected def entityInit() {}

  protected def readEntityFromNBT(nbttagcompound: NBTTagCompound) {}

  protected def writeEntityToNBT(nbttagcompound: NBTTagCompound) {}

  @SideOnly (Side.CLIENT)
  val tessellator:Tessellator = new Tessellator

}
