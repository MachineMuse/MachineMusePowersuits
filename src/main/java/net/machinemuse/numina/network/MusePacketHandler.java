package net.machinemuse.numina.network;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import net.machinemuse.numina.utils.MuseLogger;
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

import java.io.IOException;
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
    public static BiMap<Integer, IMusePackager> packagers;
    public static EnumMap<Side, FMLEmbeddedChannel> channels;
    private volatile static MusePacketHandler INSTANCE;

    public static MusePacketHandler getInstance() {
        if (INSTANCE == null) {
            synchronized (MusePacketHandler.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MusePacketHandler();
                }
            }
        }
        return INSTANCE;
    }

    private MusePacketHandler() {
        this.networkChannelName = "Numina";
        this.packagers = Maps.synchronizedBiMap(HashBiMap.create());
        this.channels = NetworkRegistry.INSTANCE.newChannel(this.networkChannelName, this);
    }

    public void addPackager(IMusePackager packagerIn) {
        // checks the map to see if the packager is already listed
        if (!packagers.inverse().containsKey(packagerIn)) {
            packagers.put(packagers.size(), packagerIn);
            }
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
        ByteBufInputStream data = new ByteBufInputStream(msg.payload());
        INetHandler handler = msg.handler();
        try {
            int packetType = data.readInt();
            if (handler instanceof NetHandlerPlayServer) {
                EntityPlayerMP player = ((NetHandlerPlayServer) handler).player;
                IMusePackager packagerServer = this.packagers.get(packetType);
                MusePacket packetServer = packagerServer.read(data, player);
                packetServer.handleServer(player);

            } else {
                if (!(handler instanceof NetHandlerPlayClient)) {
                    throw new IOException("Error with (INetHandler) handler. Should be instance of NetHandlerPlayClient.");
                }
                EntityPlayer player = this.getClientPlayer();
                IMusePackager packagerClient = this.packagers.get(packetType);
                MusePacket packetClient = packagerClient.read(data, player);
                packetClient.handleClient(player);
            }
        }catch (Exception exception) {
            MuseLogger.logException("PROBLEM READING PACKET IN DECODE STEP D:", exception);
        }
    }
}
