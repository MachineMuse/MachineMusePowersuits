package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.common.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and <strike>replies with an associated
 * inventoryrefresh packet</strike>.
 * <p>
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 * <p>
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketTweakRequestDouble extends MusePacket {
    final EntityPlayer player;
    final int itemSlot;
    final String moduleName;
    final String tweakName;
    double tweakValue;

    public MusePacketTweakRequestDouble(EntityPlayer player, int itemSlot, String moduleName, String tweakName, double tweakValue) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
        this.tweakName = tweakName;
        this.tweakValue = tweakValue;
    }

    public static MusePacketTweakRequestPackager getPackagerInstance() {
        return MusePacketTweakRequestPackager.INSTANCE;
    }

    @Override
    public IMusePackager packager() {
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
            NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);

            if (itemTag != null && ModuleManager.INSTANCE.tagHasModule(itemTag, moduleName)) {
                MuseNBTUtils.removeMuseValuesTag(stack);
                NBTTagCompound moduleTag = itemTag.getCompoundTag(moduleName);
                moduleTag.setDouble(tweakName, tweakValue);
            }
        }
    }

    public enum MusePacketTweakRequestPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            String tweakName = readString(datain);
            double tweakValue = readDouble(datain);
            return new MusePacketTweakRequestDouble(player, itemSlot, moduleName, tweakName, tweakValue);
        }
    }
}