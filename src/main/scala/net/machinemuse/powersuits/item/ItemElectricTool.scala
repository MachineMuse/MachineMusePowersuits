package net.machinemuse.powersuits.item

import com.google.common.collect.Sets
import net.machinemuse.api.electricity._
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.{ItemStack, Item, ItemTool}

object ItemElectricTool {
  val blocksEffectiveOn = Sets.newHashSet(Array[Block](Blocks.COBBLESTONE,
    Blocks.DOUBLE_STONE_SLAB, Blocks.STONE_SLAB, Blocks.STONE,
    Blocks.SANDSTONE, Blocks.MOSSY_COBBLESTONE, Blocks.IRON_ORE, Blocks.IRON_BLOCK,
    Blocks.COAL_ORE, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.DIAMOND_ORE,
    Blocks.DIAMOND_BLOCK, Blocks.ICE, Blocks.NETHERRACK, Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK,
    Blocks.REDSTONE_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.GOLDEN_RAIL, Blocks.ACTIVATOR_RAIL))
}
class ItemElectricTool(damageBonus : Float, material : Item.ToolMaterial)
  extends ItemTool(damageBonus, material, ItemElectricTool.blocksEffectiveOn)
  with ModularItemBase
  with MuseElectricItem {
  override def getToolTip(stack: ItemStack): String = null
}