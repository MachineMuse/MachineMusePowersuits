package net.machinemuse.powersuits.common

import cpw.mods.fml.common.network.IGuiHandler
import net.machinemuse.general.gui.CosmeticGui
import net.machinemuse.general.gui.GuiFieldTinker
import net.machinemuse.general.gui.GuiTinkerTable
import net.machinemuse.general.gui.KeyConfigGui
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.stats.AchievementList
import net.minecraft.world.World

/**
 * Gui handler for this mod. Mainly just takes an ID according to what was
 * passed to player.OpenGUI, and opens the corresponding GUI.
 *
 * @author MachineMuse
 */
class GuiHandler extends IGuiHandler {
  def getServerGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    ID match {
      case _ => null
    }
  }

  def getClientGuiElement(ID: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): AnyRef = {
    Minecraft.getMinecraft.thePlayer.addStat(AchievementList.openInventory, 1)
    ID match {
      case 0 => new GuiTinkerTable(player)
      case 1 => new KeyConfigGui(player)
      case 2 => new GuiFieldTinker(player)
      case 3 => new CosmeticGui(player)
      case _ => null
    }
  }
}