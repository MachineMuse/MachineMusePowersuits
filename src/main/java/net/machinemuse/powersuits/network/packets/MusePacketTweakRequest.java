package net.machinemuse.powersuits.network.packets;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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
    final EntityPlayer player;
    final int itemSlot;
    final String moduleName;
    final String tweakName;
    double tweakValue;

    public MusePacketTweakRequest(EntityPlayer player, int itemSlot, String moduleName, String tweakName, double tweakValue) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
        this.tweakName = tweakName;
        this.tweakValue = tweakValue;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(this.itemSlot);
        writeString(this.moduleName);
        writeString(this.tweakName);
        writeDouble(this.tweakValue);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        if (moduleName != null && tweakName != null) {
            ItemStack stack = player.inventory.getStackInSlot(itemSlot);
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            if (itemTag != null && ModuleManager.tagHasModule(itemTag, moduleName)) {
                NBTTagCompound moduleTag = itemTag.getCompoundTag(moduleName);
                moduleTag.setDouble(tweakName, MuseMathUtils.clampDouble(tweakValue, 0, 1));
            }
        }
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
