package net.machinemuse.powersuits.tick;

import java.lang.reflect.Method;
import java.util.EnumSet;

import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Called before and after the 3D world is rendered (tickEnd is called BEFORE
 * the 2D gui is drawn... I think?).
 * 
 * @param float tickData[0] the amount of time (0.0f-1.0f) since the last tick.
 * 
 * @author MachineMuse
 */
public class RenderTickHandler implements ITickHandler {
	private Method thaumGuiRenderer;
	private Object thaumcraftGui;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			double currEnergy = ItemUtils.getPlayerEnergy(player);
			double maxEnergy = ItemUtils.getMaxEnergy(player);
			if (maxEnergy > 0) {
				String currStr = MuseStringUtils.formatNumberShort(currEnergy);
				String maxStr = MuseStringUtils.formatNumberShort(maxEnergy);
				MuseRenderer.drawString(currStr + "/" + maxStr + " J", 1, 1);
			}
		}

		//renderThaumGui(0, player, 0);
	}

	public void renderThaumGui(float par1, EntityPlayer player, long par2) {
		try {
			if (thaumGuiRenderer == null || thaumcraftGui == null && ModCompatability.isThaumCraftLoaded()) {
				Class guiticker = Class.forName("thaumcraft.client.GUITicker");
				thaumcraftGui = guiticker.getConstructor().newInstance();
				thaumGuiRenderer = guiticker.getMethod("renderGogglesHUD", float.class, EntityPlayer.class, long.class);
			}
			thaumGuiRenderer.invoke(thaumcraftGui, par1, player, par2);
		} catch (Exception e) {
			MuseLogger.logError("Problem loading Thaumcraft classes to display HUD");
			e.printStackTrace();
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.RENDER);
	}

	@Override
	public String getLabel() {
		return "MMMPS: Render Tick";
	}

}
