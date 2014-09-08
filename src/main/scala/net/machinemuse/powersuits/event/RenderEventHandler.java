package net.machinemuse.powersuits.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.general.gui.EnergyMeter;
import net.machinemuse.general.gui.HeatMeter;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.machinemuse.powersuits.powermodule.movement.GliderModule;
import net.machinemuse.powersuits.powermodule.movement.JetBootsModule;
import net.machinemuse.powersuits.powermodule.movement.JetPackModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.machinemuse.utils.render.GlowBuffer;
import net.machinemuse.utils.render.MuseRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class RenderEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderLast(RenderWorldLastEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution screen = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        if (Config.useShaders() && Config.canUseShaders && Minecraft.isFancyGraphicsEnabled()) {
            GlowBuffer.drawFullScreen(screen);
            GlowBuffer.clear();
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Post event) {
        // MuseIcon.registerAllIcons(event.map);
    }

    static boolean ownFly = false;

    @SubscribeEvent
    public void onPreRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!event.entityPlayer.capabilities.isFlying && !event.entityPlayer.onGround && playerHasFlightOn(event.entityPlayer)) {
            event.entityPlayer.capabilities.isFlying = true;
            ownFly = true;
        }
    }

    private boolean playerHasFlightOn(EntityPlayer player) {
        return ModuleManager.itemHasActiveModule(player.getCurrentArmor(2), JetPackModule.MODULE_JETPACK)
                || ModuleManager.itemHasActiveModule(player.getCurrentArmor(2), GliderModule.MODULE_GLIDER)
                || ModuleManager.itemHasActiveModule(player.getCurrentArmor(0), JetBootsModule.MODULE_JETBOOTS)
                || ModuleManager.itemHasActiveModule(player.getCurrentArmor(3), FlightControlModule.MODULE_FLIGHT_CONTROL);
    }

    @SubscribeEvent
    public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
        if (ownFly) {
            ownFly = false;
            event.entityPlayer.capabilities.isFlying = false;
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        switch(event.type) {
            case EXPERIENCE:
                EntityPlayer player = Minecraft.getMinecraft().thePlayer;
                if (player != null && MuseItemUtils.modularItemsEquipped(player).size() > 0 && Minecraft.getMinecraft().currentScreen == null) {
                    Minecraft mc = Minecraft.getMinecraft();
                    ScaledResolution screen = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                    drawMeters(player, screen);
                }
                break;
            default:
        }

    }


    protected static HeatMeter heat;
    protected static HeatMeter energy;
    private int lightningCounter = 0;
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
}
