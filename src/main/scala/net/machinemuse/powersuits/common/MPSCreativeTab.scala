package net.machinemuse.powersuits.common

import net.minecraft.creativetab.CreativeTabs

object MPSCreativeTab extends CreativeTabs(CreativeTabs.getNextID, "powersuits") {
  def getTabIconItem = MPSItems.powerArmorHead
}