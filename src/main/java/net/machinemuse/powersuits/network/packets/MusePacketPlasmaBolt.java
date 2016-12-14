package net.machinemuse.powersuits.network.packets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketPlasmaBolt extends MusePacket {
    EntityPlayer player;
    int entityID;
    double size;


    public MusePacketPlasmaBolt(EntityPlayer player, int entityID, double size) {
        this.player = player;
        this.entityID = entityID;
        this.size = size;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(entityID);
        writeDouble(size);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(EntityPlayer player) {
        try {
            EntityPlasmaBolt entity = (EntityPlasmaBolt) Minecraft.getMinecraft().theWorld.getEntityByID(entityID);
            entity.size = this.size;
        }
        catch (Exception ignored){
        }
    }

    private static MusePacketPlasmaBoltPackager PACKAGERINSTANCE;
    public static MusePacketPlasmaBoltPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketPlasmaBoltPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketPlasmaBoltPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int entityID = readInt(datain);
            double size = readDouble(datain);
            return  new MusePacketPlasmaBolt(player, entityID, size);
        }
    }
}