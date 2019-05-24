package net.machinemuse.powersuits.event;

import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.control.KeybindManager;
import net.machinemuse.powersuits.network.MPSPackets;
import net.machinemuse.powersuits.network.packets.MPSPacketConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 * <p>
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
            MPSPackets.sendTo(new MPSPacketConfig(), (EntityPlayerMP) player);
        } else {
            MPSSettings.loadCustomInstallCosts();
        }
        if (player.world.isRemote)
            KeybindManager.readInKeybinds();
    }
}