package net.machinemuse.powersuits.powermodule.movement

import net.minecraft.nbt.NBTTagCompound

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:10 AM, 8/7/13
 */
case class AttributeModifier(operation: Int, uuid: UUID, amount: Double, attributeName: String, name: String) {
  def this(nbt: NBTTagCompound) = this(
    nbt.getInteger("Operation"),
    new UUID(nbt),
    nbt.getDouble("Amount"),
    nbt.getString("AttributeName"),
    nbt.getString("Name")
  )

  def toNBT:NBTTagCompound = toNBT(new NBTTagCompound)

  def toNBT(nbt: NBTTagCompound) = {
    nbt.setInteger("Operation", operation)
    uuid.toNBT(nbt)
    nbt.setDouble("Amount", amount)
    nbt.setString("AttributeName", attributeName)
    nbt.setString("Name", name)
    nbt
  }
}

case class UUID(least:Long,most:Long) {
  def this(nbt: NBTTagCompound) = this(
    nbt.getLong("UUIDLeast"),
    nbt.getLong("UUIDMost")
  )

  def toNBT(nbt: NBTTagCompound) = {
    nbt.setLong("UUIDLeast", least)
    nbt.setLong("UUIDMost", most)
  }
}

