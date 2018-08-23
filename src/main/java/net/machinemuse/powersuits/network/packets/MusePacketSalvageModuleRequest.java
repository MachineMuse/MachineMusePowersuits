package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import java.util.HashSet;
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
    public IMusePackager packager() {
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
            IPowerModule moduleType = ModuleManager.INSTANCE.getModule(moduleName);
            NonNullList<ItemStack> refund = ModuleManager.INSTANCE.getInstallCost(moduleName);
            if (ModuleManager.INSTANCE.itemHasModule(stack, moduleName)) {
                MuseNBTUtils.removeMuseValuesTag(stack);
                Set<Integer> slots = new HashSet<>();
                ModuleManager.INSTANCE.removeModule(stack, moduleName);
                for (ItemStack refundItem : refund) {
                    slots.addAll(MuseItemUtils.giveOrDropItemWithChance(refundItem.copy(), player, MPSConfig.INSTANCE.getSalvageChance()));
                }
                slots.add(itemSlot);

                for (Integer slotiter : slots) {
                    MusePacket reply = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter));
                    PacketSender.sendTo(reply, player);
                }
            }
        }
    }

    public static MusePacketSalvageModuleRequestPackager getPackagerInstance() {
        return  MusePacketSalvageModuleRequestPackager.INSTANCE;
    }

    public enum MusePacketSalvageModuleRequestPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            return new MusePacketSalvageModuleRequest(player, itemSlot, moduleName);
        }
    }
}