package net.machinemuse_old.powersuits.network.packets;

import net.machinemuse_old.api.IPowerModule;
import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.numina.network.MusePackager;
import net.machinemuse_old.numina.network.MusePacket;
import net.machinemuse_old.numina.network.PacketSender;
import net.machinemuse_old.powersuits.common.Config;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;

import java.io.DataInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server
 * decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketSalvageModuleRequest extends MusePacket {
    EntityPlayer player;
    int itemSlot;
    String moduleName;

    public MusePacketSalvageModuleRequest(EntityPlayer player, int itemSlot, String moduleName) {
        this.player = player;
        this.itemSlot = itemSlot;
        this.moduleName = moduleName;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(itemSlot);
        writeString(moduleName);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        if (moduleName != null) {
            InventoryPlayer inventory = player.inventory;
            ItemStack stack = player.inventory.getStackInSlot(itemSlot);
            IPowerModule moduleType = ModuleManager.getModule(moduleName);
            List<ItemStack> refund = moduleType.getInstallCost();
            if (ModuleManager.itemHasModule(stack, moduleName)) {
                Set<Integer> slots = new HashSet<>();
                ModuleManager.removeModule(stack, moduleName);
                for (ItemStack refundItem : refund) {
                    slots.addAll(MuseItemUtils.giveOrDropItemWithChance(refundItem.copy(), player, Config.getSalvageChance()));
                }
                slots.add(itemSlot);

                for (Integer slotiter : slots) {
                    MusePacket reply = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter));
                    PacketSender.sendTo(reply, player);
                }
            }
        }
    }

    private static MusePacketSalvageModuleRequestPackager PACKAGERINSTANCE;
    public static MusePacketSalvageModuleRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketSalvageModuleRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketSalvageModuleRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            return new MusePacketSalvageModuleRequest(player, itemSlot, moduleName);
        }
    }
}