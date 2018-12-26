package net.machinemuse.numina.event;

import net.machinemuse.numina.network.NuminaPackets;
import net.machinemuse.numina.network.packets.NuminaPacketConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 * <p>
 * Ported to Java by lehjr on 10/26/16.
 */
public final class NuminaPlayerTracker {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        EntityPlayer player = event.player;
        boolean isUsingBuiltInServer = FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer();

        // dedidated server or multiplayer game
        if (!isUsingBuiltInServer || (isUsingBuiltInServer && FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount() > 1)) {

            // sync config settings between client and server
            NuminaPackets.sendTo(new NuminaPacketConfig(), (EntityPlayerMP) player);
        }
    }
}