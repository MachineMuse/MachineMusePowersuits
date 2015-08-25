package net.machinemuse.api

import java.util.List
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 *
 * @author MachineMuse
 */
trait IModularItem extends net.minecraft.item.Item {
  /**
   * Gets the item's extended summary for displaying in the gui.
   *
   * @param stack
   * @return
   */
  def getLongInfo(player: EntityPlayer, stack: ItemStack): List[String]

  /**
   * Returns the amount of energy contained in the player's inventory.
   *
   * @param player
   * @return
   */
  def getPlayerEnergy(player: EntityPlayer): Double

  /**
   * Drains the amount of energy from the player's inventory.
   *
   * @param player
   * @return
   */
  def drainPlayerEnergy(player: EntityPlayer, drainAmount: Double)

  /**
   * Adds the amount of energy to the player's inventory.
   *
   * @param player
   * @return
   */
  def givePlayerEnergy(player: EntityPlayer, joulesToGive: Double)

}