package net.machinemuse.powersuits.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 *
 * Ported to Java by lehjr on 10/24/16.
 */
public class PlayerLoginHandlerThingy {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayer player = e.player;
        PacketSender.sendTo(new MusePacketPropertyModifierConfig(player, null), (EntityPlayerMP)player);
    }
}
