package net.machinemuse.numina.network;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:58 AM, 09/05/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IMusePackager {
    default MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
        return null;
    }

    default byte readByte(ByteBufInputStream dataIn) {
        try {
            return dataIn.readByte();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Byte.parseByte(null);
        }
    }

    default short readShort(ByteBufInputStream dataIn) {
        try {
            return dataIn.readShort();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Short.parseShort(null);
        }
    }

    default int readInt(ByteBufInputStream dataIn) {
        try {
            return dataIn.readInt();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Integer.parseInt(null);
        }
    }

    default long readLong(ByteBufInputStream dataIn) {
        try {
            return dataIn.readLong();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Long.parseLong(null);
        }
    }

    default boolean readBoolean(ByteBufInputStream dataIn) {
        try {
            return dataIn.readBoolean();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Boolean.parseBoolean(null);
        }
    }

    default float readFloat(ByteBufInputStream dataIn) {
        try {
            return dataIn.readFloat();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Float.parseFloat(null);
        }
    }

    default double readDouble(ByteBufInputStream dataIn) {
        try {
            return dataIn.readDouble();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Double.parseDouble(null);
        }
    }

    /**
     * Reads a series Int's from the InputStream and returns an Array of them
     */
    default int[] readIntArray(ByteBufInputStream dataIn) {
        // TODO: Simplify in 1.10.2 (Java 8)
        List<Integer> integerArrayList = new ArrayList<>();
        try {
            int arraySize = dataIn.readInt();
            for (int k = 0; k < arraySize; k++)
                integerArrayList.add(dataIn.readInt());

            int[] intArray = new int[integerArrayList.size()];
            for (int i = 0; i < integerArrayList.size(); i++) {
                intArray[i] = integerArrayList.get(i);
            }
            return intArray;

        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return new int[0];
        }
    }

    /**
     * Reads a string from a packet
     */
    default String readString(ByteBufInputStream dataIn) {
        try {
            return dataIn.readUTF();
        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return null;
        }
    }

    /**
     * Reads a compressed NBTTagCompound from the InputStream
     */
    default NBTTagCompound readNBTTagCompound(ByteBufInputStream dataIn) {
        short length;
        try {
            length = dataIn.readShort();
            return (length != -1) ? CompressedStreamTools.readCompressed(dataIn) : new NBTTagCompound();
        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return new NBTTagCompound();
        }
    }
    /**
     * Just because read map fails for this.
     */
    default Map<String, NBTTagCompound> readNBTMap(ByteBufInputStream dataIn) {
        Map<String, NBTTagCompound> mapOut = new HashMap<>();

        try {
            int mapLength = dataIn.readInt();
            System.out.println("map length: " + mapLength);

            for (int i = 0; i < mapLength; i++) {
                String s = readString(dataIn);
                NBTTagCompound nbt = readNBTTagCompound(dataIn);

                System.out.println("string: " + s);
                System.out.println("nbt: " + nbt);
                mapOut.put(s, nbt);

//                mapOut.put(readString(dataIn), readNBTTagCompound(dataIn));
            }
            return mapOut;
        } catch (Exception exception) {
            MuseLogger.logException("PROBLEM WRITING DATA TO PACKET:", exception);
        }
        return mapOut;
    }

    default byte[] decompressGZipBytes(final byte[] compressed) {
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

    /**
     * DecompiledPixelLauncher (package com.google.research.reflection.common)
     */
    default HashMap readMap(final ByteBufInputStream dataIn, final Class keyClass, final Class valueClass) {
        boolean compressOrNot;
        int dataLength;
        ByteArrayInputStream bais;
        DataInputStream dataInputStream;
        HashMap<Object, Object> hashMap = new HashMap<>();

        try {
            compressOrNot = dataIn.readBoolean();
            dataLength = dataIn.readInt();

            byte[] bytes = new byte[dataLength];
            int remaining = dataLength;
            while (remaining > 0) {
                int read = dataIn.read(bytes, dataLength - remaining, remaining);
                if (read > 0) {
                    remaining -= read;
                } else {
                    break;
                }
            }

            if (compressOrNot) {
                bytes = decompressGZipBytes(bytes);
            }

            bais = new ByteArrayInputStream(bytes);
            dataInputStream = new DataInputStream(bais);
            hashMap = readMap(dataInputStream, keyClass, valueClass);
            dataInputStream.close();
            bais.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    default HashMap readMap(final DataInputStream dataIn, final Class keyClass, final Class valueClass) {
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

    default Object readObject(final DataInputStream dataIn, final Class clazz) throws IOException {
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
}