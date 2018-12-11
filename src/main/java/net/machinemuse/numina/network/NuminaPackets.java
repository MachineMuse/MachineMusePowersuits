package net.machinemuse.numina.network;

import net.machinemuse.numina.api.constants.NuminaConstants;
import net.machinemuse.numina.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.packets.NuminaPacketConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NuminaPackets {
    private static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(NuminaConstants.MODID);

    public static void registerPackets() {
        INSTANCE.registerMessage(NuminaPacketConfig.Handler.class, NuminaPacketConfig.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MusePacketModeChangeRequest.Handler.class, MusePacketModeChangeRequest.class, 1, Side.SERVER);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAll(IMessage message) {
        INSTANCE.sendToAll(message);
    }

    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint targetPoint) {
        INSTANCE.sendToAllAround(message, targetPoint);
    }

    public static void sendToDimension(IMessage message, int dim) {
        INSTANCE.sendToDimension(message, dim);
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}
