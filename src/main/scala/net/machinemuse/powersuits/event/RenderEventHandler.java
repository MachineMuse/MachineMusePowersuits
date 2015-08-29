package net.machinemuse.powersuits.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.machinemuse.powersuits.powermodule.movement.GliderModule;
import net.machinemuse.powersuits.powermodule.movement.JetBootsModule;
import net.machinemuse.powersuits.powermodule.movement.JetPackModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;

public class RenderEventHandler {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderLast(RenderWorldLastEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution screen = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);

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

}
