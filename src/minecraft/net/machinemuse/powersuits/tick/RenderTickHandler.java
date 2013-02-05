package net.machinemuse.powersuits.tick;

import java.util.EnumSet;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.item.IModularItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			double currEnergy = MuseItemUtils.getPlayerEnergy(player);
			double maxEnergy = MuseItemUtils.getMaxEnergy(player);
			if (maxEnergy > 0) {
				String currStr = MuseStringUtils.formatNumberShort(currEnergy);
				String maxStr = MuseStringUtils.formatNumberShort(maxEnergy);
				MuseRenderer.drawString(currStr + "/" + maxStr + " J", 1, 1);
			}
		}
		if (Minecraft.getMinecraft().currentScreen == null) {
			for (int i = 0; i < 9; i++) {
				Minecraft mc = Minecraft.getMinecraft();
				ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
				ItemStack stack = player.inventory.mainInventory[i];
				if (stack != null && stack.getItem() instanceof IModularItem) {
					MuseRenderer.blendingOn();
					NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
					if (itemTag.hasKey("Mode")) {
						String mode = itemTag.getString("Mode");
						IPowerModule module = ModuleManager.getModule(mode);
						MuseIcon currentMode = MuseIcon.WEAPON_ELECTRIC;
						MuseIcon nextMode = MuseIcon.WEAPON_FIRE;
						MuseIcon prevMode = MuseIcon.WEAPON_GRAVITY;

						MuseRenderer.drawIconPartial(screen.getScaledWidth_double() / 2.0 - 105.0 + 20.0 * i, screen.getScaledHeight_double() - 30,
								prevMode, Colour.WHITE.withAlpha(0.4), 0, 0, 16, 8);
						MuseRenderer.drawIconAt(screen.getScaledWidth_double() / 2.0 - 89.0 + 20.0 * i, screen.getScaledHeight_double() - 40,
								currentMode, Colour.WHITE.withAlpha(0.7));
						MuseRenderer.drawIconPartial(screen.getScaledWidth_double() / 2.0 - 73.0 + 20.0 * i, screen.getScaledHeight_double() - 30,
								nextMode, Colour.WHITE.withAlpha(0.4), 0, 0, 16, 8);
						MuseRenderer.blendingOff();
					}
				}
			}
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
