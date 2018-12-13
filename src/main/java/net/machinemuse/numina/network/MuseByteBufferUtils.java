package net.machinemuse.numina.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nonnull;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class MuseByteBufferUtils extends ByteBufUtils {
    public static void writePlayerInputMap(ByteBuf buf, PlayerInputMap inputMap) {
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        DataOutputStream dos = new DataOutputStream(bos);
        inputMap.writeToStream(dos);
        try {
            dos.flush();
            dos.close();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

//    public static PlayerInputMap readPlayerInputMap(ByteBuf buf) {
//        ByteBufInputStream bis = new ByteBufInputStream(buf);
//        DataInputStream dis = new DataInputStream(bis);
//
//
//
//
//    }

    public static void writeCompressedNBT(ByteBuf buf, NBTTagCompound nbt) {
        ByteBufOutputStream bos = new ByteBufOutputStream(buf);
        DataOutputStream dos = new DataOutputStream(bos);
        try {
            CompressedStreamTools.writeCompressed(nbt, dos);
            dos.flush();
            dos.close();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    public static NBTTagCompound readCompressedNBT(ByteBuf buf ) {
        ByteBufInputStream bis = new ByteBufInputStream(buf);
        DataInputStream dis = new DataInputStream(bis);
        NBTTagCompound nbt;
        try {
            nbt = CompressedStreamTools.readCompressed(dis);
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET:", exception);
            nbt = new NBTTagCompound();
        }
        return nbt;
    }

    public static void writeIntArray(ByteBuf buf, int[] intArray) {
        buf.writeInt(intArray.length);
        for (int i = 0; i < intArray.length; i++) {
            buf.writeInt(intArray[i]);
        }
    }

    public static int[] readIntArray(ByteBuf buf) {
        int arraySize = buf.readInt();
        int[] intArray = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            intArray[i] = buf.readInt();
        }
        return intArray;
    }

    /**
     * write compressed or uncompressed map to the ByteBuf
     */
    public static void writeMap(ByteBuf buf, @Nonnull final Map map, boolean compressOrNot) {
        byte[] bytes = writeMapToBytes(map);
        try {
            if (compressOrNot) {
                bytes = compressBytesGZip(bytes);
            }
            buf.writeBoolean(compressOrNot);
            buf.writeInt(bytes.length); // FIXME: is this needed?
            buf.writeBytes(bytes);
        } catch (Exception exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
    }

    /**
     * Note, the only reason to write the map to ByteArrayOutputStream is for getting the data size to write out.
     *
     * @param map
     * @return
     */
    public static byte[] writeMapToBytes(@Nonnull final Map map) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(map.size());
            for (final Object key : map.keySet()) {
                writeObjectToStream(dos, key);
                writeObjectToStream(dos, (map.get(key)));
            }
            // bytearrayoutputstream only updates if dataoutputstream closes
            dos.flush();
            dos.close();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
        return baos.toByteArray();
    }

    /**
     * DecompiledPixelLauncher (package com.google.research.reflection.common)
     */
    public static HashMap readMap(final ByteBuf dataIn, final Class keyClass, final Class valueClass) {
        boolean compressOrNot;
        int dataLength;
        ByteArrayInputStream bais;
        DataInputStream dataInputStream;
        HashMap<Object, Object> hashMap = new HashMap<>();

        try {
            compressOrNot = dataIn.readBoolean();
            dataLength = dataIn.readInt();

            byte[] bytes = new byte[dataLength];
            dataIn.readBytes(bytes);//, dataLength - remaining, remaining);

            if (compressOrNot) {
                bytes = decompressGZipBytes(bytes);
            }
//
            bais = new ByteArrayInputStream(bytes);
            dataInputStream = new DataInputStream(bais);
            hashMap = readMap(dataInputStream, keyClass, valueClass);
            dataInputStream.close();
            bais.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    private static HashMap readMap(final DataInputStream dataIn, final Class keyClass, final Class valueClass) {
        final HashMap<Object, Object> hashMap = new HashMap<>();
        int mapLength;

        try {
            mapLength = dataIn.readInt();
            for (int i = 0; i < mapLength; i++) {
                hashMap.put(readObject(dataIn, keyClass), readObject(dataIn, valueClass));
            }
            return hashMap;
        } catch (Exception exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
        return hashMap;
    }

    /** Read/Write Objects ------------------------------------------------------------------------ */
    // Used for writing map to packets
    private static void writeObjectToStream(DataOutputStream dataOut, @Nonnull final Object o) throws IOException {
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
        } else if (o instanceof NBTTagCompound) {
            CompressedStreamTools.write((NBTTagCompound)o, dataOut);
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
            throw new TypeNotPresentException(o.getClass().getName(), new Throwable("map key or getValue type handler not found!!"));
    }

    private static Object readObject(final DataInputStream dataIn, final Class clazz) throws IOException {
        int i = 0;
        if (clazz == Boolean.class) {
            return dataIn.readBoolean();
        }
        if (clazz == Integer.class) {
            return dataIn.readInt();
        }
        if (clazz == Long.class) {
            return dataIn.readLong();
        }
        if (clazz == Double.class) {
            return dataIn.readDouble();
        }
        if (clazz == Float.class) {
            return dataIn.readFloat();
        }
        if (clazz == String.class) {
            return dataIn.readUTF();
        }
        if (clazz == NBTTagCompound.class) {
            return CompressedStreamTools.read(dataIn);
        }
        if (clazz == int[].class) {
            final int int1 = dataIn.readInt();
            final int[] array = new int[int1];
            while (i < int1) {
                array[i] = dataIn.readInt();
                ++i;
            }
            return array;
        }
        if (clazz != float[].class) {
            return null;
        }
        final int int2 = dataIn.readInt();
        final float[] array2 = new float[int2];
        while (i < int2) {
            array2[i] = dataIn.readFloat();
            ++i;
        }
        return array2;
    }

    /** Compression/Decompression ----------------------------------------------------------------- */
    // https://stackoverflow.com/questions/37204975/decompressing-byte-using-lz4
    public static byte[] compressBytesGZip(final byte[] data) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[0];
        try {
            GZIPOutputStream compressedStream = new GZIPOutputStream(outStream);
            compressedStream.write(data);
            compressedStream.flush();
            compressedStream.close();
            return outStream.toByteArray();
        } catch (Exception e) {

        }
        return new byte[0];
    }

    public static byte[] decompressGZipBytes(final byte[] compressed) {
        byte[] decomp = new byte[compressed.length * 4];//you might need to allocate more

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            GZIPInputStream zippy = new GZIPInputStream(new ByteArrayInputStream(compressed));
            int buffRead;
            // once reading is finished, -1 is returned
            while ((buffRead = zippy.read(decomp)) != -1) {
                bos.write(decomp, 0, buffRead);
            }
            zippy.close();
            return bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}