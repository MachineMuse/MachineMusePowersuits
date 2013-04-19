package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.*;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.EnergyMeter;
import net.machinemuse.general.gui.HeatMeter;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeHooks;
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
	private final static int SWAPTIME = 200;
	public static long lastSwapTime = 0;
	public static int lastSwapDirection = 0;
	protected static HeatMeter heat;
	protected static HeatMeter energy;
	private int lightningCounter = 0;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null && MuseItemUtils.modularItemsEquipped(player).size() > 0) {
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
			double currEnergy = ElectricItemUtils.getPlayerEnergy(player);
			double maxEnergy = ElectricItemUtils.getMaxEnergy(player);
			double currHeat = MuseHeatUtils.getPlayerHeat(player);
			double maxHeat = MuseHeatUtils.getMaxHeat(player);
			if (maxEnergy > 0 && BlockTinkerTable.energyIcon != null) {
				if (Config.useGraphicalMeters()) {
					if (energy == null) {
						energy = new EnergyMeter();
						heat = new HeatMeter();
					}
					energy.draw(screen.getScaledWidth() - 20, screen.getScaledHeight() / 2.0 - 16, currEnergy / maxEnergy);
					heat.draw(screen.getScaledWidth() - 12, screen.getScaledHeight() / 2.0 - 16, currHeat / maxHeat);
				} else {
					String currStr = MuseStringUtils.formatNumberShort(currEnergy);
					String maxStr = MuseStringUtils.formatNumberShort(maxEnergy);
					MuseRenderer.drawString(currStr + '/' + maxStr + " J", 1, 1);
					currStr = MuseStringUtils.formatNumberShort(currHeat);
					maxStr = MuseStringUtils.formatNumberShort(maxHeat);
					MuseRenderer.drawString(currStr + '/' + maxStr + " C", 1, 10);

				}
			}
			if (Minecraft.getMinecraft().currentScreen == null) {
				MuseRenderer.TEXTURE_MAP = MuseRenderer.ITEM_TEXTURE_QUILT;
				int i = player.inventory.currentItem;
				ItemStack stack = player.inventory.mainInventory[i];
				if (stack != null && stack.getItem() instanceof IModularItem) {
					MuseRenderer.blendingOn();
					NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
					int swapTime = (int) Math.min(System.currentTimeMillis() - lastSwapTime, SWAPTIME);
					Icon currentMode = null;
					Icon nextMode = null;
					Icon prevMode = null;
					List<String> modes = MuseItemUtils.getModes(stack, player);
					String mode = itemTag.getString("Mode");
					int modeIndex = modes.indexOf(mode);
					if (modeIndex > -1) {
						String prevModeName = modes.get((modeIndex + modes.size() - 1) % modes.size());
						String nextModeName = modes.get((modeIndex + 1) % modes.size());
						IPowerModule module = ModuleManager.getModule(mode);
						IPowerModule nextModule = ModuleManager.getModule(nextModeName);
						IPowerModule prevModule = ModuleManager.getModule(prevModeName);

						if (module != null) {
							currentMode = module.getIcon(stack);
							if (!nextModeName.equals(mode)) {
								nextMode = nextModule.getIcon(stack);
								prevMode = prevModule.getIcon(stack);
							}
						}
					}
					double prevX, prevY, currX, currY, nextX, nextY;
					double sw = screen.getScaledWidth_double();
					double baroffset = screen.getScaledHeight_double() - 40;
					if (!player.capabilities.isCreativeMode) {
						baroffset -= 16;
						if (ForgeHooks.getTotalArmorValue(player) > 0) {
							baroffset -= 8;
						}
					}
					// Root locations of the mode list
					prevX = sw / 2.0 - 105.0 + 20.0 * i;
					prevY = baroffset + 10;
					currX = sw / 2.0 - 89.0 + 20.0 * i;
					currY = baroffset;
					nextX = sw / 2.0 - 73.0 + 20.0 * i;
					nextY = baroffset + 10;
					if (swapTime == SWAPTIME || lastSwapDirection == 0) {
						drawIcon(prevX, prevY, prevMode, 0.4, 0, 0, 16, baroffset - prevY + 16);
						drawIcon(currX, currY, currentMode, 0.8, 0, 0, 16, baroffset - currY + 16);
						drawIcon(nextX, nextY, nextMode, 0.4, 0, 0, 16, baroffset - nextY + 16);
					} else {
						double r1 = 1 - swapTime / (double) SWAPTIME;
						double r2 = swapTime / (double) SWAPTIME;
						if (lastSwapDirection == -1) {
							nextX = (currX * r1 + nextX * r2);
							nextY = (currY * r1 + nextY * r2);
							currX = (prevX * r1 + currX * r2);
							currY = (prevY * r1 + currY * r2);
							drawIcon(currX, currY, currentMode, 0.8, 0, 0, 16, baroffset - currY + 16);
							drawIcon(nextX, nextY, nextMode, 0.8, 0, 0, 16, baroffset - nextY + 16);

						} else {
							prevX = (currX * r1 + prevX * r2);
							prevY = (currY * r1 + prevY * r2);
							currX = (nextX * r1 + currX * r2);
							currY = (nextY * r1 + currY * r2);
							// MuseRenderer
							drawIcon(prevX, prevY, prevMode, 0.8, 0, 0, 16, baroffset - prevY + 16);
							drawIcon(currX, currY, currentMode, 0.8, 0, 0, 16, baroffset - currY + 16);

						}
					}
					MuseRenderer.blendingOff();
					Colour.WHITE.doGL();
				}

			}
		}

	}

	private void drawIcon(double x, double y, Icon icon, double alpha, int u1, int v1, int u2, double v2) {
		MuseRenderer.drawIconPartial(x, y, icon, Colour.WHITE.withAlpha(alpha), u1, v1, u2, v2);

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
