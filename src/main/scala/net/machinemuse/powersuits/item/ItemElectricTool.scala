package net.machinemuse.powersuits.item

import com.google.common.collect.Sets
import net.machinemuse.api.electricity._
import net.minecraft.block.Block
import net.minecraft.init.Blocks
import net.minecraft.item.{ItemStack, Item, ItemTool}

object ItemElectricTool {
  val blocksEffectiveOn = Sets.newHashSet(Array[Block](Blocks.cobblestone, Blocks.double_stone_slab, Blocks.stone_slab, Blocks.stone, Blocks.sandstone, Blocks.mossy_cobblestone, Blocks.iron_ore, Blocks.iron_block, Blocks.coal_ore, Blocks.gold_block, Blocks.gold_ore, Blocks.diamond_ore, Blocks.diamond_block, Blocks.ice, Blocks.netherrack, Blocks.lapis_ore, Blocks.lapis_block, Blocks.redstone_ore, Blocks.lit_redstone_ore, Blocks.rail, Blocks.detector_rail, Blocks.golden_rail, Blocks.activator_rail))
}
class ItemElectricTool(damageBonus : Float, material : Item.ToolMaterial)
  extends ItemTool(damageBonus, material, ItemElectricTool.blocksEffectiveOn)
  with ModularItemBase
  with MuseElectricItem {
  override def getToolTip(stack: ItemStack): String = null
}