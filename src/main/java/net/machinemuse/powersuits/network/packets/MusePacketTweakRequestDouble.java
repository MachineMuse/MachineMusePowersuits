package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
            IItemHandler modularItemCap = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
            if (modularItemCap instanceof IModularItemCapability) {
                if (((IModularItemCapability) modularItemCap).isModuleInstalled(moduleName)) {
                    ((IModularItemCapability) modularItemCap).setModuleTweakDouble(moduleName, tweakName, tweakValue);
                }
            }
        }
    }

    private static MusePacketTweakRequestPackagerDouble PACKAGERINSTANCE;
    public static MusePacketTweakRequestPackagerDouble getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketTweakRequestPackagerDouble();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketTweakRequestPackagerDouble extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            String tweakName = readString(datain);
            double tweakValue = readDouble(datain);
            return new MusePacketTweakRequestDouble(player, itemSlot, moduleName, tweakName, tweakValue);
        }
    }
}