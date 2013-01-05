package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.gui.GuiTinkerTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.AchievementList;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

/**
 * Gui handler for this mod. Mainly just takes an ID according to what was
 * passed to player.OpenGUI, and opens the corresponding GUI.
 * 
 * @author MachineMuse
 * 
 */
public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		switch (ID) {
		case 0:
			Minecraft.getMinecraft().thePlayer.addStat(
					AchievementList.openInventory, 1);
			return new GuiTinkerTable((EntityClientPlayerMP) player);
		default:
			return null;
		}
	}
}
