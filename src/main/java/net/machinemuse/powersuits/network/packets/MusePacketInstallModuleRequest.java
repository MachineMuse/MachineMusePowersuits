package net.machinemuse.powersuits.network.packets;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.api.module.IPowerModule;
import net.machinemuse.numina.network.IMusePackager;
import net.machinemuse.numina.network.MusePacket;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentString;

/**
 * Packet for requesting to purchase an upgrade. Player-to-server. Server decides whether it is a valid upgrade or not and replies with an associated
 * inventoryrefresh packet.
 * <p>
 * Author: MachineMuse (Claire Semple)
 * Created: 10:16 AM, 01/05/13
 * <p>
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

    public static MusePacketInstallModuleRequestPackager getPackagerInstance() {
        return MusePacketInstallModuleRequestPackager.INSTANCE;
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
        ItemStack stack = player.inventory.getStackInSlot(itemSlot);
        if (moduleName != null) {
            InventoryPlayer inventory = player.inventory;
            IPowerModule moduleType = ModuleManager.INSTANCE.getModule(moduleName);
            if (moduleType == null || !moduleType.isAllowed()) {
                player.sendMessage(new TextComponentString("Server has disallowed this module. Sorry!"));
                return;
            }
            NonNullList<ItemStack> cost = ModuleManager.INSTANCE.getInstallCost(moduleName);
            if ((!ModuleManager.INSTANCE.itemHasModule(stack, moduleName) && MuseItemUtils.hasInInventory(cost, player.inventory)) || player.capabilities.isCreativeMode) {
                MuseNBTUtils.removeMuseValuesTag(stack);
                ModuleManager.INSTANCE.itemAddModule(stack, moduleType);
                for (ItemStack stackInCost : cost) {
                    ElectricItemUtils.givePlayerEnergy(player, MPSConfig.INSTANCE.rfValueOfComponent(stackInCost));
                }

                if (!player.capabilities.isCreativeMode) {
                    MuseItemUtils.deleteFromInventory(cost, inventory);
                }

                // use builtin handler
                player.inventoryContainer.detectAndSendChanges();
            }
        }
    }

    public enum MusePacketInstallModuleRequestPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int itemSlot = readInt(datain);
            String moduleName = readString(datain);
            return new MusePacketInstallModuleRequest(player, itemSlot, moduleName);
        }
    }
}