package net.machinemuse.numina.network;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumMap;

/**
 * Ported to Java by lehjr on 10/22/16.
 */
public class PacketSender {
    private static EnumMap<Side, FMLEmbeddedChannel> channels = MusePacketHandler.channels;

    public static Packet getPacketFrom(MusePacket message) {
        return channels.get(Side.SERVER).generatePacketFrom(message);
    }

    public static void sendToAll(MusePacket message) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channels.get(Side.SERVER).writeOutbound(message);
    }

    public static void sendTo(MusePacket message, EntityPlayerMP player) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channels.get(Side.SERVER).writeOutbound(message);
    }

    public static void sendToAllAround(MusePacket message, NetworkRegistry.TargetPoint point) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channels.get(Side.SERVER).writeOutbound(message);
    }

    public static void sendToAllAround(MusePacket packet, TileEntity tileEntity, double d) {
        sendToAllAround(packet, new NetworkRegistry.TargetPoint(tileEntity.getWorld().provider.getDimension(), tileEntity.getPos().getX(), tileEntity.getPos().getY(), tileEntity.getPos().getZ(), d));
    }

    public static void sendToAllAround(MusePacket packet, Entity entity, double d) {
        sendToAllAround(packet, new NetworkRegistry.TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, d));
    }

    public static void sendToDimension(MusePacket message, int dimensionId) {
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channels.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channels.get(Side.SERVER).writeOutbound(message);
    }

    public static void sendToServer(MusePacket message) {
        channels.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channels.get(Side.CLIENT).writeOutbound(message);
    }
}