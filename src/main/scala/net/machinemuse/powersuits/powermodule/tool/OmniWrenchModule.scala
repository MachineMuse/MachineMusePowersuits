package net.machinemuse.powersuits.powermodule.tool

import java.util.List

import net.machinemuse.api.IModularItem
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.machinemuse.utils.{MuseCommonStrings, MuseItemUtils}
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

/**
 * Created by User: Andrew2448
 * 4:39 PM 4/21/13
 * updated by MachineMuse, adapted from OpenComputers srench
 */
object OmniWrenchModule {
  val MODULE_OMNI_WRENCH: String = "Prototype OmniWrench"
}

class OmniWrenchModule(validItems: List[IModularItem]) extends PowerModuleBase(validItems) with IRightClickModule {
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1))
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2))

  def getTextureFile: String = "omniwrench"

  def getCategory: String = MuseCommonStrings.CATEGORY_TOOL


  def getDataName: String = OmniWrenchModule.MODULE_OMNI_WRENCH

  def getLocalizedName: String = StatCollector.translateToLocal("module.omniwrench.name")


  def getDescription: String = "A wrench which can interact with almost every mod."

  def onRightClick(playerClicking: EntityPlayer, world: World, item: ItemStack) {}

  def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float) {}

  def onItemUseFirst(stack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    world.blockExists(x, y, z) && world.canMineBlock(player, x, y, z) && (world.getBlock(x, y, z) match {
      case block: Block if block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side)) =>
        block.onNeighborBlockChange(world, x, y, z, Blocks.air)
        player.swingItem()
        !world.isRemote
      case _ =>
        false
    })
  }

  def onPlayerStoppedUsing(itemStack: ItemStack, world: World, player: EntityPlayer, par4: Int) {}
}