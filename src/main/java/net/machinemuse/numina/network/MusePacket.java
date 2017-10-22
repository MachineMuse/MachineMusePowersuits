package net.machinemuse.numina.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:58 AM, 09/05/13
 *
 * Ported to Java by lehjr on 10/25/16.
 */
public abstract class MusePacket
{
    private PacketBuffer bytes;
    private final DataOutputStream dataout;

    public MusePacket() {
        this.bytes = new PacketBuffer(Unpooled.buffer());
        this.dataout = new DataOutputStream(new ByteBufOutputStream(this.bytes));
    }

    public abstract MusePackager packager();

    public abstract void write();

    public ByteBuf bytes() {
        return this.bytes;
    }

    public DataOutputStream dataout() {
        return this.dataout;
    }

    /**
     * Gets the MC packet associated with this MusePacket
     *
     * @return Packet250CustomPayload
     */
    public FMLProxyPacket getFMLProxyPacket() throws IOException {
        this.dataout.writeInt((Integer) MusePacketHandler.packagers.inverse().get(this.packager()));
        this.write();
        return new FMLProxyPacket(this.bytes, MusePacketHandler.networkChannelName);
    }

    public MusePacket getPacket131() {
        return this;
    }

    /**
     * Called by the network manager since it does all the packet mapping
     *
     * @param player
     */
    @SideOnly(Side.CLIENT)
    public void handleClient(EntityPlayer player) {
    }

    public void handleServer(EntityPlayerMP player) {
    }

    public void writeInt(int i) {
        try {
            this.dataout.writeInt(i);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeIntArray(int[] data) {
        try {
            this.dataout.writeInt(data.length);
            for (int k :  data)
                dataout.writeInt(k);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeBoolean(boolean b) {
        try {
            this.dataout.writeBoolean(b);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeDouble(double i) {
        try {
            this.dataout.writeDouble(i);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Writes the IC2ItemTest's ID (short), then size (byte), then damage. (short)
     */
    public void writeItemStack(ItemStack stack) {
        try {
            if (stack == null) {
                this.dataout.writeShort(-1);
            }
            else {
                NBTTagCompound nbt = new NBTTagCompound();
                stack.writeToNBT(nbt);
                this.writeNBTTagCompound(nbt);
            }
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Writes a compressed NBTTagCompound to the OutputStream
     */
    public void writeNBTTagCompound(NBTTagCompound nbt) {
        try {
            if (nbt == null) {
                this.dataout.writeShort(-1);
            }
            else {
                byte[] compressednbt = compress(nbt);
                this.dataout.writeShort((short)compressednbt.length);
                this.dataout.write(compressednbt);
            }
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /*
     * "Borrowed" from 1.7.10
     */
    public byte[] compress(NBTTagCompound nbt) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
            CompressedStreamTools.write(nbt, dataoutputstream);
            dataoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytearrayoutputstream.toByteArray();
    }

    /**
     * Writes a String to the DataOutputStream
     */
    public void writeString(String string) {
        try {
            this.dataout.writeUTF(string);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }
}
