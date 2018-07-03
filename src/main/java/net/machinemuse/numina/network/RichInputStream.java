package net.machinemuse.numina.network;

import net.jpountz.lz4.LZ4BlockInputStream;
import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 1:15 AM, 09/05/13
 *
 * Ported to Java by lehjr on 11/5/16.
 */
public class RichInputStream {
    public static DataInputStream in;

    private RichInputStream(DataInputStream in) {
        this.in = in;
    }

    public static RichInputStream toRichStream(DataInputStream in) {
        return new RichInputStream(in);
    }

    /**
     * Reads a series Int's from the InputStream and returns an Array of them
     */
    public int[] readIntArray() {
        // TODO: Simplify in 1.10.2 (Java 8)
        List<Integer> integerArrayList = new ArrayList<>();
        try {
            int arraySize = in.readInt();
            for (int k = 0; k < arraySize; k++)
                integerArrayList.add(in.readInt());

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
     * Reads a series Bytes from the InputStream and returns an Array of them
     */
    public byte[] readByteArray() {
        // TODO: Simplify in 1.10.2 (Java 8)
        List<Byte> byteArrayList = new ArrayList<>();
        try {
            int arraySize = in.readInt();
            for (byte k = 0; k < arraySize; k++)
                byteArrayList.add(in.readByte());

            byte[] byteArray = new byte[byteArrayList.size()];
            for (int i = 0; i < byteArrayList.size(); i++) {
                byteArray[i] = byteArrayList.get(i);
            }
            return byteArray;

        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return new byte[0];
        }
    }

    /**
     * Reads an ItemStack from the InputStream
     */
    public ItemStack readItemStack() {
        NBTTagCompound tag = readNBTTagCompound();
        return (tag == null) ? null : new ItemStack(tag);
    }

    /**
     * Load the compressed compound from the inputstream.
     */
    public static NBTTagCompound readCompressed(InputStream is) throws IOException {
//        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
        // LZ4 adaptation of vanilla method
        DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new LZ4BlockInputStream(is)));
        NBTTagCompound nbttagcompound;

        try {
            nbttagcompound = CompressedStreamTools.read(datainputstream, NBTSizeTracker.INFINITE);
        } finally {
            datainputstream.close();
        }
        return nbttagcompound;
    }

    /**
     * Reads a compressed NBTTagCompound from the InputStream
     */
    public NBTTagCompound readNBTTagCompound() {
        short length;
        try {
            length = in.readShort();
            return (length != -1) ? readCompressed(in) : null;
        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return null;
        }
    }

    /**
     * Reads a string from a packet
     */
    public String readString() {
        try {
            return in.readUTF();
        } catch (IOException e) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", e);
            return null;
        }
    }

    /**
     * DecompiledPixelLauncher (package com.google.research.reflection.common)
     */
    public static HashMap getMapFromStream(final DataInputStream datain, final Class keyClass, final Class valueClass) {
        try {
            final HashMap<Object, Object> hashMap = new HashMap<>();
            for (int int1 = datain.readInt(), i = 0; i < int1; ++i) {
                hashMap.put(readObject(datain, keyClass), readObject(datain, valueClass));
            }
            return hashMap;
        } catch (Exception e) {

        }
        return null;
    }

    private static Object readObject(final DataInputStream dataInputStream, final Class clazz) throws IOException {
        int i = 0;
        if (clazz == Integer.class) {
            return dataInputStream.readInt();
        }
        if (clazz == Long.class) {
            return dataInputStream.readLong();
        }
        if (clazz == Float.class) {
            return dataInputStream.readFloat();
        }
        if (clazz == String.class) {
            return dataInputStream.readUTF();
        }
        if (clazz == int[].class) {
            final int int1 = dataInputStream.readInt();
            final int[] array = new int[int1];
            while (i < int1) {
                array[i] = dataInputStream.readInt();
                ++i;
            }
            return array;
        }
        if (clazz != float[].class) {
            return null;
        }
        final int int2 = dataInputStream.readInt();
        final float[] array2 = new float[int2];
        while (i < int2) {
            array2[i] = dataInputStream.readFloat();
            ++i;
        }
        return array2;
    }
}
