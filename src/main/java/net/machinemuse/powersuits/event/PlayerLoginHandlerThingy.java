package net.machinemuse.powersuits.event;

import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.client.render.model.ModelHelper;
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 *
 * Ported to Java by lehjr on 10/24/16.
 */
public class PlayerLoginHandlerThingy {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayer player = e.player;
        PacketSender.sendTo(new MusePacketPropertyModifierConfig(player, null), (EntityPlayerMP)player);
    }
}