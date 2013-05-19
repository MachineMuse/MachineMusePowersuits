package net.machinemuse.powersuits.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.EnergyMeter;
import net.machinemuse.general.gui.HeatMeter;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.event.ThaumRenderEventHandler;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.machinemuse.utils.render.GlowBuffer;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Icon;
import net.minecraftforge.common.ForgeHooks;

import java.util.EnumSet;
import java.util.List;

/**
 * Called before and after the 3D world is rendered (tickEnd is called BEFORE
 * the 2D gui is drawn... I think?).
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
        GlowBuffer.clear();
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null && MuseItemUtils.modularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            drawMeters(player, screen);
            drawGogglesHUD(player, mc, ((Float) tickData[0]));
            drawActiveMode(player, screen);

        }
    }

    private void drawMeters(EntityPlayer player, ScaledResolution screen) {

        double currEnergy = ElectricItemUtils.getPlayerEnergy(player);
        double maxEnergy = ElectricItemUtils.getMaxEnergy(player);
        double currHeat = MuseHeatUtils.getPlayerHeat(player);
        double maxHeat = MuseHeatUtils.getMaxHeat(player);
        if (maxEnergy > 0 && BlockTinkerTable.energyIcon != null) {
            String currStr = MuseStringUtils.formatNumberShort(currEnergy);
            String maxStr = MuseStringUtils.formatNumberShort(maxEnergy);
            String currHeatStr = MuseStringUtils.formatNumberShort(currHeat);
            String maxHeatStr = MuseStringUtils.formatNumberShort(maxHeat);
            if (Config.useGraphicalMeters()) {
                if (energy == null) {
                    energy = new EnergyMeter();
                    heat = new HeatMeter();
                }
                double left = screen.getScaledWidth() - 20;
                double top = screen.getScaledHeight() / 2.0 - 16;
                energy.draw(left, top, currEnergy / maxEnergy);
                heat.draw(left + 8, top, currHeat / maxHeat);
                MuseRenderer.drawRightAlignedString(currStr, left - 2, top + 10);
                MuseRenderer.drawRightAlignedString(currHeatStr, left - 2, top + 20);
            } else {
                MuseRenderer.drawString(currStr + '/' + maxStr + " \u1D60", 1, 1);
                MuseRenderer.drawString(currHeatStr + '/' + maxHeatStr + " C", 1, 10);

            }
        }
    }

    private void drawActiveMode(EntityPlayer player, ScaledResolution screen) {

        int i = player.inventory.currentItem;
        ItemStack stack = player.inventory.mainInventory[i];
        if (stack != null && stack.getItem() instanceof IModularItem) {
            MuseRenderer.pushTexture(MuseRenderer.ITEM_TEXTURE_QUILT);
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
            int sw = screen.getScaledWidth();
            int sh = screen.getScaledHeight();
            int baroffset = 22;
            if (!player.capabilities.isCreativeMode) {
                baroffset += 16;
                if (ForgeHooks.getTotalArmorValue(player) > 0) {
                    baroffset += 8;
                }
            }
            MuseRenderer.scissorsOn(0, 0, sw, sh - baroffset);

            baroffset = screen.getScaledHeight() - baroffset;
            // Root locations of the mode list
            prevX = sw / 2.0 - 105.0 + 20.0 * i;
            prevY = baroffset - 8;
            currX = sw / 2.0 - 89.0 + 20.0 * i;
            currY = baroffset - 18;
            nextX = sw / 2.0 - 73.0 + 20.0 * i;
            nextY = baroffset - 8;
            if (swapTime == SWAPTIME || lastSwapDirection == 0) {
                drawIcon(prevX, prevY, prevMode, 0.4);
                drawIcon(currX, currY, currentMode, 0.8);
                drawIcon(nextX, nextY, nextMode, 0.4);
            } else {
                double r1 = 1 - swapTime / (double) SWAPTIME;
                double r2 = swapTime / (double) SWAPTIME;
                if (lastSwapDirection == -1) {
                    nextX = (currX * r1 + nextX * r2);
                    nextY = (currY * r1 + nextY * r2);
                    currX = (prevX * r1 + currX * r2);
                    currY = (prevY * r1 + currY * r2);
                    drawIcon(currX, currY, currentMode, 0.8);
                    drawIcon(nextX, nextY, nextMode, 0.8);

                } else {
                    prevX = (currX * r1 + prevX * r2);
                    prevY = (currY * r1 + prevY * r2);
                    currX = (nextX * r1 + currX * r2);
                    currY = (nextY * r1 + currY * r2);
                    // MuseRenderer
                    drawIcon(prevX, prevY, prevMode, 0.8);
                    drawIcon(currX, currY, currentMode, 0.8);

                }
            }
            MuseRenderer.scissorsOff();
            MuseRenderer.blendingOff();
            Colour.WHITE.doGL();
            MuseRenderer.popTexture();
        }
    }

    private void drawGogglesHUD(EntityPlayer player, Minecraft mc, float partialTickTime) {

        if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule() && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() instanceof IModularItem && MuseItemUtils.itemHasActiveModule(player.getCurrentArmor(3), "Aurameter")) {
            ThaumRenderEventHandler.renderGogglesHUD(partialTickTime, player, mc.theWorld.getWorldTime());
        }
    }

    private void drawIcon(double x, double y, Icon icon, double alpha) {
        MuseRenderer.drawIconAt(x, y, icon, Colour.WHITE.withAlpha(alpha));
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
