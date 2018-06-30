package net.machinemuse.numina.network;

import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.scala.MuseNumericRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.List;

/**
 * @author MachineMuse
 *
 * Ported to Java by lehjr on 10/23/16.
 */
@ChannelHandler.Sharable
public final class MusePacketHandler extends MessageToMessageCodec<FMLProxyPacket, MusePacket> {
    public static String networkChannelName;
    public static MuseNumericRegistry<MusePackager> packagers;
    public static EnumMap<Side, FMLEmbeddedChannel> channels;

    private MusePacketHandler() {
        this.networkChannelName = "Numina";
        this.packagers = new MuseNumericRegistry<>();
        this.channels = NetworkRegistry.INSTANCE.newChannel(this.networkChannelName, this);
    }

    static {
        new MusePacketHandler();
    }

    public void encode(ChannelHandlerContext ctx, MusePacket msg, List<Object> out) {
        try {
            out.add(msg.getFMLProxyPacket());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    public void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) {
        DataInputStream data = new DataInputStream((InputStream)new ByteBufInputStream(msg.payload()));
        int packetType;

        INetHandler handler = msg.handler();
        try {
            if (handler instanceof NetHandlerPlayServer) {
                EntityPlayerMP player = ((NetHandlerPlayServer) handler).player;
                packetType = data.readInt();
                MusePackager packagerServer = this.packagers.get(packetType);
                MusePacket packetServer = packagerServer.read(data, player);
                packetServer.handleServer(player);

            } else {
                if (!(handler instanceof NetHandlerPlayClient)) {
                    throw new IOException("Error with (INetHandler) handler. Should be instance of NetHandlerPlayClient.");
                }
                EntityPlayer player = this.getClientPlayer();
                packetType = data.readInt();
                MusePackager packagerClient = this.packagers.get(packetType);
                MusePacket packetClient = packagerClient.read(data, player);
                packetClient.handleClient(player);
            }
        }catch (Exception exception) {
            MuseLogger.logException("PROBLEM READING PACKET IN DECODE STEP D:", exception);
        }
    }
}
