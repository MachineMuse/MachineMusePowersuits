package net.machinemuse.powersuits.powermodule.tool

import net.machinemuse.utils.{MuseCommonStrings, MuseItemUtils}
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.minecraft.item.ItemStack
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.machinemuse.powersuits.common.ModularPowersuits
import net.minecraftforge.common.ForgeDirection
import net.machinemuse.api.IModularItem
import net.machinemuse.api.moduletrigger.IRightClickModule

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:27 PM, 5/8/13
 */
class RedstoneLaser(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IRightClickModule {
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1))
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2))

  def getCategory: String = MuseCommonStrings.CATEGORY_SPECIAL

  def getName: String = "Redstone Laser"

  def getDescription: String = "No need for a lever if you're just giving it a pulse."

  def getTextureFile: String = "laser"

  def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float) {


  }

  def onItemUseFirst(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, sideIndex: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean = {
    val side: ForgeDirection = ForgeDirection.getOrientation(sideIndex)
    val xo = x + side.offsetX
    val yo = y + side.offsetY
    val zo = z + side.offsetZ
    val redlaserid = 0
    world.getBlockId(xo, yo, zo) match {
      case 0 => world.setBlock(xo, yo, zo, redlaserid)
      case `redlaserid` => world.setBlockMetadataWithNotify(xo,yo,zo,redlaserid,world.getBlockMetadata(xo,yo,zo)+1)
      case _ =>
    }
    true
  }

  def onPlayerStoppedUsing(itemStack: ItemStack, world: World, player: EntityPlayer, par4: Int) {}

  def onRightClick(player: EntityPlayer, world: World, item: ItemStack) {
    player.openGui(ModularPowersuits.instance, 2, world, player.posX.toInt, player.posY.toInt, player.posZ.toInt)
  }

}
