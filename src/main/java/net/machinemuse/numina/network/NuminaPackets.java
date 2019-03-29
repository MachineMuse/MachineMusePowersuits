package net.machinemuse.numina.network;

import net.machinemuse.numina.constants.NuminaConstants;
import net.machinemuse.numina.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.numina.network.packets.NuminaPacketConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NuminaPackets {
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(NuminaConstants.MODID);

    public static void registerNuminaPackets() {
        INSTANCE.registerMessage(NuminaPacketConfig.Handler.class, NuminaPacketConfig.class, 0, Side.CLIENT);
        INSTANCE.registerMessage(MusePacketModeChangeRequest.Handler.class, MusePacketModeChangeRequest.class, 1, Side.SERVER);
    }

    public static void sendTo(IMessage message, EntityPlayerMP player) {
        INSTANCE.sendTo(message, player);
    }

    public static void sendToAll(IMessage message) {
        INSTANCE.sendToAll(message);
    }

    public static void sendToAllAround(IMessage message, Entity entity, double d) {
        INSTANCE.sendToAllAround(message, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, d));
    }

    public static void sendToDimension(IMessage message, int dim) {
        INSTANCE.sendToDimension(message, dim);
    }

    @SideOnly(Side.CLIENT)
    public static void sendToServer(IMessage message) {
        INSTANCE.sendToServer(message);
    }
}
