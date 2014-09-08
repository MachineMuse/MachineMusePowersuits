package net.machinemuse.powersuits.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.machinemuse.general.gui.EnergyMeter;
import net.machinemuse.general.gui.HeatMeter;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.Config;
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

import java.util.EnumSet;

/**
 * Called before and after the 3D world is rendered (tickEnd is called BEFORE
 * the 2D gui is drawn... I think?).
 *
 * @author MachineMuse
 */
public class RenderTickHandler implements ITickHandler {
    protected static HeatMeter heat;
    protected static HeatMeter energy;
    private int lightningCounter = 0;

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if (Config.canUseShaders) {
            GlowBuffer.clear();
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;
        if (player != null && MuseItemUtils.modularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
            Minecraft mc = Minecraft.getMinecraft();
            ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            drawMeters(player, screen);
//            drawGogglesHUD(player, mc, ((Float) tickData[0]));

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

//    private void drawGogglesHUD(EntityPlayer player, Minecraft mc, float partialTickTime) {
//
//        if (ModCompatability.isThaumCraftLoaded() && ModCompatability.enableThaumGogglesModule() && player.getCurrentArmor(3) != null && player.getCurrentArmor(3).getItem() instanceof IModularItem && ModuleManager.itemHasActiveModule(player.getCurrentArmor(3), "Aurameter")) {
//            ThaumRenderEventHandler.renderGogglesHUD(partialTickTime, player, mc.theWorld.getWorldTime());
//        }
//    }


    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER);
    }

    @Override
    public String getLabel() {
        return "MMMPS: Render Tick";
    }

}
