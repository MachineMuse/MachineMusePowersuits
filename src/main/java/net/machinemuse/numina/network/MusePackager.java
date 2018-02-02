package net.machinemuse.numina.network;

import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:58 AM, 09/05/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public class MusePackager {
    private static MusePackager INSTANCE;
    public short READ_ERROR = -150;

    public static MusePackager getInstance() {
        if (INSTANCE == null)
            INSTANCE = new MusePackager();
        return INSTANCE;
    }

    public MusePacket read(DataInputStream datain, EntityPlayer player) {
        return null;
    }

    public byte readByte(DataInputStream datain) {
        try {
            return datain.readByte();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Byte.parseByte(null);
        }
    }

    public short readShort(DataInputStream datain) {
        try {
            return datain.readShort();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Short.parseShort(null);
        }
    }

    public int readInt(DataInputStream datain) {
        try {
            return datain.readInt();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Integer.parseInt(null);
        }
    }

    public long readLong(DataInputStream datain) {
        try {
            return datain.readLong();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Long.parseLong(null);
        }
    }

    public boolean readBoolean(DataInputStream datain) {
        try {
            return datain.readBoolean();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Boolean.parseBoolean(null);
        }
    }

    public float readFloat(DataInputStream datain) {
        try {
            return datain.readFloat();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Float.parseFloat(null);
        }
    }

    public double readDouble(DataInputStream datain) {
        try {
            return datain.readDouble();
        } catch (IOException exception) {
            MuseLogger.logException("PROBLEM READING DATA FROM PACKET D:", exception);
            return Double.parseDouble(null);
        }
    }

    /* RichInputStream ---------------------------------------------------------------------------- */
    public int[] readIntArray(DataInputStream datain) {
        return RichInputStream.toRichStream(datain).readIntArray();
    }

    public byte[] readByteArray(DataInputStream datain) {
        return RichInputStream.toRichStream(datain).readByteArray();
    }

    public String readString(DataInputStream datain) {
        return RichInputStream.toRichStream(datain).readString();
    }

    public ItemStack readItemStack(DataInputStream datain) {
        return RichInputStream.toRichStream(datain).readItemStack();
    }

    public NBTTagCompound readNBTTagCompound(DataInputStream datain) {
        return RichInputStream.toRichStream(datain).readNBTTagCompound();
    }
}