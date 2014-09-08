package net.machinemuse.powersuits.item

import net.machinemuse.api.electricity._
import net.minecraft.block.Block
import net.minecraft.item.EnumToolMaterial
import net.minecraft.item.ItemTool

class ItemElectricTool(par1: Int, par2: Int, par3EnumToolMaterial: EnumToolMaterial, par4ArrayOfBlock: Array[Block])
  extends ItemTool(par1, par2, par3EnumToolMaterial, par4ArrayOfBlock)
  with ModularItemBase
  with MuseElectricItem {

}