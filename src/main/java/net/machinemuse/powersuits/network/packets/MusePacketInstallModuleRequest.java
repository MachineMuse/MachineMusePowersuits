package net.machinemuse.powersuits.network.packets;

import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.network.MusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.network.PacketSender;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;

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
            ItemStack moduleType = ModuleManager.getInstance().getModule(moduleName);
            if (moduleType.isEmpty() || !((IModule)moduleType.getItem()).isAllowed()) {
                player.sendMessage(new TextComponentString("Server has disallowed this module. Sorry!"));
                return;
            }
            List<ItemStack> cost = ((IModule)moduleType.getItem()).getInstallCost();
            if ((!ModuleManager.getInstance().itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, player.inventory)) || player.capabilities.isCreativeMode) {
                ModuleManager.getInstance().itemAddModule(stack, moduleType);
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
                    PacketSender.sendTo(reply, player);
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