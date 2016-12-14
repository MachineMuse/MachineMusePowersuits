package net.machinemuse.powersuits.network.packets;

import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import java.io.DataInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 *
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketInstallModuleRequest extends MusePacket {
    EntityPlayer player;
    int itemSlot;
    String moduleName;

    public MusePacketInstallModuleRequest(EntityPlayer player, int itemSlot, String moduleName) {
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
        ItemStack stack = player.inventory.getStackInSlot(itemSlot);
        if (moduleName != null) {
            InventoryPlayer inventory = player.inventory;
            IPowerModule moduleType = ModuleManager.getModule(moduleName);
            if (moduleType == null || !moduleType.isAllowed()) {
                player.addChatComponentMessage(new ChatComponentText("Server has disallowed this module. Sorry!"));
                return;
            }
            List<ItemStack> cost = moduleType.getInstallCost();
            if ((!ModuleManager.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, player.inventory)) || player.capabilities.isCreativeMode) {
                ModuleManager.itemAddModule(stack, moduleType);
                for (ItemStack stackInCost : cost) {
                    ElectricItemUtils.givePlayerEnergy(player, ElectricItemUtils.jouleValueOfComponent(stackInCost));
                }
                List<Integer> slotsToUpdate = new ArrayList<>();
                if (!player.capabilities.isCreativeMode) {
                    slotsToUpdate = MuseItemUtils.deleteFromInventory(cost, inventory);
                }
                slotsToUpdate.add(itemSlot);
                for (Integer slotiter : slotsToUpdate) {
                    MusePacket reply = new MusePacketInventoryRefresh(player, slotiter, inventory.getStackInSlot(slotiter));
                    PacketSender.sendTo(reply, (EntityPlayerMP) player);
                }
            }
        }
    }

    private static MusePacketInstallModuleRequestPackager PACKAGERINSTANCE;
    public static MusePacketInstallModuleRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null)
            PACKAGERINSTANCE = new MusePacketInstallModuleRequestPackager();
        return PACKAGERINSTANCE;
    }

    public static class MusePacketInstallModuleRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            return new MusePacketInstallModuleRequest(player, itemSlot, moduleName);
        }
    }
}