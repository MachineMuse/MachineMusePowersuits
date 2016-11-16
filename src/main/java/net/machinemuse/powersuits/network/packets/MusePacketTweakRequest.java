package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.minecraft.entity.player.EntityPlayer;

import java.io.DataInputStream;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and <strike>replies with an associated
 * inventoryrefresh packet</strike>.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketTweakRequest extends MusePacket {
    EntityPlayer player;
    int itemSlot;
    String moduleName;
    String tweakName;
    double tweakValue;

    public  MusePacketTweakRequest(EntityPlayer player, int itemSlot, String moduleName, String tweakName, double tweakValue) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
        this.tweakValue = tweakValue;
    }

    @Override
    public IMusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(itemSlot);
        writeString(moduleName);
        writeString(tweakName);
        writeDouble(tweakValue);
    }

    private static MusePacketTweakRequestPackager PACKAGERINSTANCE;
    public static MusePacketTweakRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketTweakRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketTweakRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            String tweakName = readString(datain);
            double tweakValue = readDouble(datain);
            return new MusePacketTweakRequest(player, itemSlot, moduleName, tweakName, tweakValue);
        }
    }
}