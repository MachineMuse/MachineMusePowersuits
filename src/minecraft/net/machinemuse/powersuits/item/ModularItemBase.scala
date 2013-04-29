package net.machinemuse.powersuits.item

import net.minecraft.item.{ItemStack, Item}
import cpw.mods.fml.relauncher.{SideOnly, Side}
import net.machinemuse.general.geometry.Colour
import net.machinemuse.utils.{MuseItemUtils, MuseStringUtils, MuseCommonStrings}
import scala.Predef.String
import net.minecraft.entity.player.EntityPlayer
import net.machinemuse.powersuits.powermodule.misc.{CosmeticGlowModule, TintModule}
import net.machinemuse.utils.MuseMathUtils._
import net.machinemuse.api.electricity.MuseElectricItem
import net.machinemuse.api.{ModuleManager, IModularItem}

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 */
trait ModularItemBase extends Item with IModularItem with MuseElectricItem {
  @SideOnly(Side.CLIENT) override def getColorFromItemStack(stack: ItemStack, par2: Int): Int = {
    val c: Colour = getColorFromItemStack(stack)
    return c.getInt
  }

  def getGlowFromItemStack(stack: ItemStack): Colour = {
    if (!MuseItemUtils.itemHasActiveModule(stack, CosmeticGlowModule.MODULE_GLOW)) {
      return Colour.LIGHTBLUE
    }
    val computedred: Double = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.RED_GLOW)
    val computedgreen: Double = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.GREEN_GLOW)
    val computedblue: Double = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.BLUE_GLOW)
    val colour: Colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8)
    return colour
  }

  def getColorFromItemStack(stack: ItemStack): Colour = {
    if (!MuseItemUtils.itemHasActiveModule(stack, TintModule.MODULE_TINT)) {
      return Colour.WHITE
    }
    val computedred: Double = ModuleManager.computeModularProperty(stack, TintModule.RED_TINT)
    val computedgreen: Double = ModuleManager.computeModularProperty(stack, TintModule.GREEN_TINT)
    val computedblue: Double = ModuleManager.computeModularProperty(stack, TintModule.BLUE_TINT)
    val colour: Colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F)
    return colour
  }

  @SideOnly(Side.CLIENT) override def requiresMultipleRenderPasses: Boolean = {
    return false
  }

  /**
   * Adds information to the item's tooltip when 'getting' it.
   *
   * @param stack            The itemstack to get the tooltip for
   * @param player           The player (client) viewing the tooltip
   * @param currentTipList   A list of strings containing the existing tooltip. When
   *                         passed, it will just contain the name of the item;
   *                         enchantments and lore are
   *                         appended afterwards.
   * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
   *                         their settings.
   */
  override def addInformation(stack: ItemStack, player: EntityPlayer, currentTipList: java.util.List[_], advancedToolTips: Boolean) {
    MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips)
  }

  def formatInfo(string: String, value: Double): String = {
    return string + '\t' + MuseStringUtils.formatNumberShort(value)
  }

  def getLongInfo(player: EntityPlayer, stack: ItemStack): java.util.List[String] = {
    val info = new java.util.ArrayList[String]
    info.add("Detailed Summary")
    info.add(formatInfo("Armor", getArmorDouble(player, stack)))
    info.add(formatInfo("Energy Storage", getCurrentEnergy(stack)) + 'J')
    info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + 'g')
    info
  }

  def getArmorDouble(player: EntityPlayer, stack: ItemStack): Double = 0
}
