package net.machinemuse.numina.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import net.jpountz.lz4.LZ4BlockOutputStream;
import net.jpountz.lz4.LZ4Compressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4SafeDecompressor;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:58 AM, 09/05/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
public abstract class MusePacket {
    private PacketBuffer bytes;
    private ByteBufOutputStream bytesOut;
//    private final DataOutputStream dataout;

    public MusePacket() {
        this.bytes = new PacketBuffer(Unpooled.buffer());
        this.bytesOut = new ByteBufOutputStream(this.bytes);
//        this.dataout = new DataOutputStream(bytesOut);
    }

    public abstract IMusePackager packager();

    public abstract void write();

    public ByteBufOutputStream bytesOut() {
        return bytesOut;
    }


    public ByteBuf bytes() {
        return this.bytes;
    }

//    public DataOutputStream dataout() {
//        return this.dataout;
//    }

    /**
     * Gets the MC packet associated with this MusePacket
     *
     * @return Packet250CustomPayload
     */
    public FMLProxyPacket getFMLProxyPacket() throws IOException {
//        this.dataout.writeInt(MusePacketHandler.getInstance().packagers.inverse().get(this.packager()));
        this.bytesOut.writeInt(MusePacketHandler.getInstance().packagers.inverse().get(this.packager()));

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
            this.bytesOut.writeInt(i);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeByteArray(byte[] data) {
        try {
            this.bytesOut.writeInt(data.length);
            for (int k : data)
                bytesOut.writeByte(k);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeIntArray(int[] data) {
        try {
            this.bytesOut.writeInt(data.length);
            for (int k : data)
                bytesOut.writeInt(k);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeBoolean(boolean b) {
        try {
            this.bytesOut.writeBoolean(b);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public void writeDouble(double i) {
        try {
            this.bytesOut.writeDouble(i);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Writes a String to the DataOutputStream
     */
    public void writeString(String string) {
        try {
            this.bytesOut.writeUTF(string);
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
                this.bytesOut.writeShort(-1);
            }
            else {
                byte[] compressednbt = compressLZ4(nbt); // Int needed in "for loop" for LZ4 decompressor
                this.bytesOut.writeInt((short)compressednbt.length);
                this.bytesOut.write(compressednbt);
            }
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Writes the IC2ItemTest's ID (short), then size (byte), then damage. (short)
     */
    public void writeItemStack(@Nonnull ItemStack stack) {
        try {
            if (stack == null) {
                this.bytesOut.writeShort(-1);
            } else {
                NBTTagCompound nbt = new NBTTagCompound();
                stack.writeToNBT(nbt);
                this.writeNBTTagCompound(nbt);
            }
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Adapted from 1.7.10
     */
    public byte[] compressLZ4(NBTTagCompound nbt) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            // LZ4 adaptation
            DataOutputStream dataoutputstream = new DataOutputStream(new LZ4BlockOutputStream(bytearrayoutputstream));
            CompressedStreamTools.write(nbt, dataoutputstream);
            // bytearrayoutputstream only updates if dataoutputstream closes
            dataoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytearrayoutputstream.toByteArray();
    }

    /**
     * "adapted" from 1.7.10
     */
    public byte[] compressGZip(NBTTagCompound nbt) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
            CompressedStreamTools.write(nbt, dataoutputstream);

            // bytearrayoutputstream only updates if dataoutputstream closes
            dataoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return bytearrayoutputstream.toByteArray();
    }

    /**
     *
     */
    public void writeMap(@Nonnull final Map map, boolean compressOrNot) {
        System.out.println("Map size: " + map.size());

        byte[] bytes = writeMapToBytes(map);
        System.out.println("Map size in uncompressed bytes: " + bytes.length);

        try {
            if (compressOrNot) {
                bytes = compress(bytes);
                System.out.println("Map size compressed: " + bytes.length);
            }
            bytesOut.writeBoolean(compressOrNot);
            bytesOut.writeInt(bytes.length); // FIXME: is this needed?
            bytesOut.write(bytes);

        } catch (Exception exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     *
     * @param map
     * @return
     */
    public byte[] writeMapToBytes(@Nonnull final Map map) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(map.size());
            for (final Object key : map.keySet()) {
                writeObjectToStream(dos, key);
                writeObjectToStream(dos, (map.get(key)));
            }
            // bytearrayoutputstream only updates if dataoutputstream closes
            dos.close();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
        return baos.toByteArray();
    }

    // Used for writing map to packets
    private void writeObjectToStream(DataOutputStream dataOut, @Nonnull final Object o) throws IOException {
        final Class objectType = o.getClass();
        int i = 0;
        if (objectType == Boolean.class)
            dataOut.writeBoolean((Boolean) o);
        else if (objectType == Byte.class)
            dataOut.writeByte((Byte) o);
        else if (objectType == Integer.class)
            dataOut.writeInt((Integer) o);
        else if (objectType == Long.class)
            dataOut.writeLong((Long) o);
        else if (objectType == Double.class)
            dataOut.writeDouble((Double) o);
        else if (objectType == Float.class)
            dataOut.writeFloat((Float) o);
        else if (objectType == String.class)
            dataOut.writeUTF((String) o);
        else if (o instanceof float[]) {
            final float[] array = (float[]) o;
            dataOut.writeInt(array.length);
            while (i < array.length) {
                dataOut.writeFloat(array[i]);
                ++i;
            }
        } else if (o instanceof int[]) {
            final int[] array2 = (int[]) o;
            dataOut.writeInt(array2.length);
            while (i < array2.length) {
                dataOut.writeInt(array2[i]);
                ++i;
            }

            // this probably won't work and should be tested. Should not even be needed.
//        } else if (objectType == HashMap.class) {
//            writeMapToStream((Map) o);
        } else
            throw new TypeNotPresentException(o.getClass().getName(), new Throwable("map key or value type handler not found!!"));
    }


    // https://stackoverflow.com/questions/37204975/decompressing-byte-using-lz4
    byte[] compress(final byte[] data) {
//        LZ4Factory lz4Factory = LZ4Factory.safeInstance();
        LZ4Factory lz4Factory = LZ4Factory.fastestInstance();
        LZ4Compressor fastCompressor = lz4Factory.fastCompressor();
        int maxCompressedLength = fastCompressor.maxCompressedLength(data.length);
        byte[] comp = new byte[maxCompressedLength];
        int compressedLength = fastCompressor.compress(data, 0, data.length, comp, 0, maxCompressedLength);
        return Arrays.copyOf(comp, compressedLength);
    }
}