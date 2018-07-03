package net.machinemuse.numina.death;

import net.machinemuse.numina.common.Numina;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:31 PM, 10/15/13
 *
 * Ported to java by lehjr on 10/10/16.
 */
public class DeathEventHandler {
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent e) {
        EntityPlayer player = (EntityPlayer) e.getEntityLiving();
        e.setCanceled(true);
        player.openGui(Numina.getInstance(), 0, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
        MuseLogger.logDebug("Death");
//        player.setHealth(10f)
    }

    @SubscribeEvent
    public void onOpenGui(GuiOpenEvent e) {
        if (e.getGui() instanceof GuiGameOver) {
            e.setCanceled(true);
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            player.openGui(Numina.getInstance(), 0, player.world, (int)player.posX, (int)player.posY, (int)player.posZ);
        }
    }
}
