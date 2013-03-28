package net.machinemuse.powersuits.tick;

import java.util.EnumSet;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.general.MuseRenderer;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.RadialIndicator;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeHooks;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Called before and after the 3D world is rendered (tickEnd is called BEFORE the 2D gui is drawn... I think?).
 * 
 * @param float tickData[0] the amount of time (0.0f-1.0f) since the last tick.
 * 
 * @author MachineMuse
 */
public class RenderTickHandler implements ITickHandler {
	private final static int SWAPTIME = 200;
	public static long lastSwapTime = 0;
	public static int lastSwapDirection = 0;
	protected static RadialIndicator heat;
	protected static RadialIndicator energy;
	private int lightningCounter = 0;

	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {

	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
		if (player != null) {
			double currEnergy = ElectricItemUtils.getPlayerEnergy(player);
			double maxEnergy = ElectricItemUtils.getMaxEnergy(player);
			if (maxEnergy > 0) {
				String currStr = MuseStringUtils.formatNumberShort(currEnergy);
				String maxStr = MuseStringUtils.formatNumberShort(maxEnergy);
				MuseRenderer.drawString(currStr + '/' + maxStr + " J", 1, 1);
			}
			if (BlockTinkerTable.energyIcon != null) {
				if (energy == null) {
					energy = new RadialIndicator(8, 16, 15 * Math.PI / 8, 9 * Math.PI / 8,
							Colour.BLACK, new Colour(0.2, 0.8, 1.0, 1.0), BlockTinkerTable.energyIcon, MuseRenderer.BLOCK_TEXTURE_QUILT);
					heat = new RadialIndicator(8, 16, 1 * Math.PI / 8, 7 * Math.PI / 8,
							Colour.BLACK, Colour.WHITE, Block.lavaMoving.getBlockTextureFromSide(1), MuseRenderer.BLOCK_TEXTURE_QUILT);
				}
				heat.draw(50, 50, 0.6);
				energy.draw(50, 50, 0.6);
				MuseRenderer.drawLightningBetweenPoints(50.0, 50.0, 1.0, 50.0, 100.0, 1.0, lightningCounter);
				lightningCounter = (lightningCounter + 1) % 50;

			}
		}
		if (Minecraft.getMinecraft().currentScreen == null) {
			MuseRenderer.TEXTURE_MAP = MuseRenderer.ITEM_TEXTURE_QUILT;
			Minecraft mc = Minecraft.getMinecraft();
			ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
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
				// MuseRenderer.blendingOff();
				GL11.glDisable(GL11.GL_LIGHTING);
				Colour.WHITE.doGL();
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
