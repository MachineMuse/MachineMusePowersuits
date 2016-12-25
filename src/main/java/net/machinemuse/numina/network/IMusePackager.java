//package net.machinemuse.numina.network;
//
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//
//import java.io.DataInputStream;
//
///**
// * Ported to Java by lehjr on 11/4/16.
// */
//public interface IMusePackager {
//    short READ_ERROR = -150;
//
//    MusePacket read(DataInputStream datain, EntityPlayer player);
//
//    byte readByte(DataInputStream datain);
//
//    short readShort(DataInputStream datain);
//
//    int readInt(DataInputStream datain);
//
//    long readLong(DataInputStream datain);
//
//    boolean readBoolean(DataInputStream datain);
//
//    float readFloat(DataInputStream datain);
//
//    double readDouble(DataInputStream datain);
//
//    int[] readIntArray(DataInputStream datain);
//
//    String readString(DataInputStream datain);
//
//    ItemStack readItemStack(DataInputStream datain);
//
//    NBTTagCompound readNBTTagCompound(DataInputStream datain);
//}
