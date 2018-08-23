//package net.machinemuse.powersuits.network.packets;
//
//import net.machinemuse.numina.network.MusePackager;
//import net.machinemuse.numina.network.MusePacket;
//import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.entity.EntityPlayerSP;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//
//import java.io.DataInputStream;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 12:28 PM, 5/6/13
// *
// * Ported to Java by lehjr on 11/14/16.
// */
//public class MusePacketPlasmaBolt extends MusePacket {
//    EntityPlayer player;
//    int entityID;
//    double size;
//
//
//    public MusePacketPlasmaBolt(EntityPlayer player, int entityID, double size) {
//        this.player = player;
//        this.entityID = entityID;
//        this.size = size;
//    }
//
//    @Override
//    public MusePackager packager() {
//        return getPackagerInstance();
//    }
//
//    @Override
//    public void write() {
//        writeInt(entityID);
//        writeDouble(size);
//        System.out.println("entity ID here is: " + entityID);
//        System.out.println("size here is: " + size);
//
//    }
//
//    @SideOnly(Side.CLIENT)
//    @Override
//    public void handleClient(EntityPlayerSP player) {
//        try {
//            EntityPlasmaBolt entity = (EntityPlasmaBolt) Minecraft.getMinecraft().world.getEntityByID(entityID);
//            entity.size = this.size;
//        }
//        catch (Exception e){
//
//            return;
//        }
//    }
//
//    private static MusePacketPlasmaBoltPackager PACKAGERINSTANCE;
//    public static MusePacketPlasmaBoltPackager getPackagerInstance() {
//        if (PACKAGERINSTANCE == null)
//            PACKAGERINSTANCE = new MusePacketPlasmaBoltPackager();
//        return PACKAGERINSTANCE;
//    }
//
//    public static class MusePacketPlasmaBoltPackager extends MusePackager {
//        @Override
//        public MusePacket read(DataInputStream datain, EntityPlayer player) {
//            int entityID = readInt(datain);
//            double size = readDouble(datain);
//
//            System.out.println("entity ID here is: " + entityID);
//            System.out.println("size here is: " + size);
//
//
//            return  new MusePacketPlasmaBolt(player, entityID, size);
//        }
//    }
//}