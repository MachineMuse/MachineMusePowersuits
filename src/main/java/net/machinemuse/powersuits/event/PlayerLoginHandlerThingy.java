package net.machinemuse.powersuits.event;

import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.network.packets.MPSPacketConfig;
import net.machinemuse.powersuits.network.packets.MusePacketPropertyModifierConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 *
 * Ported to Java by lehjr on 10/24/16.
 */
public final class PlayerLoginHandlerThingy {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        boolean isUsingBuiltInServer = FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer();

        // dedidated server or multiplayer game
        if (!isUsingBuiltInServer || (isUsingBuiltInServer && FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount() > 1)) {
            // sync config settings between client and server
            PacketSender.sendTo(new MPSPacketConfig(player), (EntityPlayerMP) player);
        }

        // TODO: with the new config system, this could become obsolete
        PacketSender.sendTo(new MusePacketPropertyModifierConfig(player, null), (EntityPlayerMP)player);
    }
}