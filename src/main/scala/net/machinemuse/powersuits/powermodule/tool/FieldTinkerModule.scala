package net.machinemuse.powersuits.powermodule.tool

import net.machinemuse.api.IModularItem
import net.machinemuse.api.moduletrigger.IRightClickModule
import net.machinemuse.powersuits.common.ModularPowersuits
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.PowerModuleBase
import net.machinemuse.utils.{MuseCommonStrings, MuseItemUtils}
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.world.World

/**
 * Created with IntelliJ IDEA.
 * User: Claire2
 * Date: 4/30/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
class FieldTinkerModule(list: java.util.List[IModularItem]) extends PowerModuleBase(list) with IRightClickModule {
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1))
  addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2))
  def getCategory: String = MuseCommonStrings.CATEGORY_SPECIAL

  def getDataName: String = "Field Tinker Module"

  override def getUnlocalizedName = "fieldTinkerer"

  def getTextureFile: String = "transparentarmor"

  def onItemUse(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float) {}

  def onItemUseFirst(itemStack: ItemStack, player: EntityPlayer, world: World, x: Int, y: Int, z: Int, side: Int, hitX: Float, hitY: Float, hitZ: Float): Boolean =  {false}

  def onPlayerStoppedUsing(itemStack: ItemStack, world: World, player: EntityPlayer, par4: Int) {}

  def onRightClick(player: EntityPlayer, world: World, item: ItemStack) {
    player.openGui(ModularPowersuits, 2, world, player.posX.toInt, player.posY.toInt, player.posZ.toInt)
  }
}
