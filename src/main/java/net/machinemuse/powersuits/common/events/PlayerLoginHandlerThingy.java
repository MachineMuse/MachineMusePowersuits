package net.machinemuse.powersuits.common.events;

import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.config.ServerSettings;
import net.machinemuse.powersuits.network.MusePacketConfig;
import net.machinemuse.powersuits.network.MusePacketPropertyModifierConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 8:01 PM, 12/05/13
 *
 * Ported to Java by lehjr on 10/24/16.
 */
@Mod.EventBusSubscriber(modid = MODID)
public class PlayerLoginHandlerThingy {
    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent e) {
        EntityPlayer player = e.player;
        PacketSender.sendTo(new MusePacketPropertyModifierConfig(player, null), (EntityPlayerMP) player);

        // dedicated server
        if (!FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer())
            PacketSender.sendTo(new MusePacketConfig(player), (EntityPlayerMP) player);
        else {
            // actual single player game
            if (FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount() == 1)
                MPSSettings.setServerSettings(new ServerSettings());
            else
                // multi player game running from builtin server
                PacketSender.sendTo(new MusePacketConfig(player), (EntityPlayerMP) player);
        }
    }
}