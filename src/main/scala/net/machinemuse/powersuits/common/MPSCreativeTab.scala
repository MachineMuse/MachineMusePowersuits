package net.machinemuse.powersuits.common

import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import cpw.mods.fml.common.registry.LanguageRegistry

object MPSCreativeTab extends CreativeTabs(CreativeTabs.getNextID, "powersuits") {
  def getTabIconItem = MPSItems.powerArmorHead
}