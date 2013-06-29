package net.machinemuse.powersuits.event;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.movement.FlightControlModule;
import net.machinemuse.powersuits.powermodule.movement.GliderModule;
import net.machinemuse.powersuits.powermodule.movement.JetBootsModule;
import net.machinemuse.powersuits.powermodule.movement.JetPackModule;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.render.GlowBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class RenderEventHandler {
    @SideOnly(Side.CLIENT)
    @ForgeSubscribe
    public void renderLast(RenderWorldLastEvent event) {

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution screen = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        if (Config.useShaders() && Config.canUseShaders && Minecraft.isFancyGraphicsEnabled()) {
            GlowBuffer.drawFullScreen(screen);
        }
    }

    @ForgeSubscribe
    public void onTextureStitch(TextureStitchEvent.Post event) {
        // MuseIcon.registerAllIcons(event.map);
    }

    static boolean ownFly = false;

    @ForgeSubscribe
    public void onPreRenderPlayer(RenderPlayerEvent.Pre event) {
        if (!event.entityPlayer.capabilities.isFlying && !event.entityPlayer.onGround && playerHasFlightOn(event.entityPlayer)) {
            event.entityPlayer.capabilities.isFlying = true;
            ownFly = true;
        }
    }

    private boolean playerHasFlightOn(EntityPlayer player) {
        return MuseItemUtils.itemHasActiveModule(player.getCurrentArmor(2), JetPackModule.MODULE_JETPACK)
                || MuseItemUtils.itemHasActiveModule(player.getCurrentArmor(2), GliderModule.MODULE_GLIDER)
                || MuseItemUtils.itemHasActiveModule(player.getCurrentArmor(0), JetBootsModule.MODULE_JETBOOTS)
                || MuseItemUtils.itemHasActiveModule(player.getCurrentArmor(3), FlightControlModule.MODULE_FLIGHT_CONTROL);
    }

    @ForgeSubscribe
    public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
        if (ownFly) {
            ownFly = false;
            event.entityPlayer.capabilities.isFlying = false;
        }
    }
}
