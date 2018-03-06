package net.machinemuse.numina.network;

import net.machinemuse.numina.api.capability_ports.inventory.IModeChangingItemCapability;
import net.machinemuse.numina.item.IModeChangingItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.io.DataInputStream;
import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:28 PM, 5/6/13
 *
 * Ported to Java by lehjr on 11/14/16.
 */
public class MusePacketModeChangeRequest extends MusePacket {
    String mode;
    int slot;

    public MusePacketModeChangeRequest(EntityPlayer player, String mode, int slot) {
        this.slot = slot;
        this.mode = mode;
    }

    @Override
    public MusePackager packager() {
        return getPackagerInstance();
    }

    @Override
    public void write() {
        writeInt(slot);
        writeString(mode);
    }

    @Override
    public void handleServer(EntityPlayerMP player) {
        if (slot > -1 && slot < 9) {
            ItemStack stack = player.inventory.mainInventory.get(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IModeChangingItem) {
                IItemHandler modeChangingCapability = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (modeChangingCapability != null && modeChangingCapability instanceof IModeChangingItemCapability) {
                    if (((IModeChangingItemCapability) modeChangingCapability).isValidMode(mode)) {
                        ((IModeChangingItemCapability) modeChangingCapability).setActiveMode(mode);
                    }
                }
            }
        }
    }

    private static MusePacketModeChangeRequestPackager PACKAGERINSTANCE;
    public static MusePacketModeChangeRequestPackager getPackagerInstance() {
        if (PACKAGERINSTANCE == null) {
            synchronized (MusePacketModeChangeRequestPackager.class) {
                if (PACKAGERINSTANCE == null) {
                    PACKAGERINSTANCE = new MusePacketModeChangeRequestPackager();
                }
            }
        }
        return PACKAGERINSTANCE;
    }

    public static class MusePacketModeChangeRequestPackager extends MusePackager {
        @Override
        public MusePacket read(DataInputStream datain, EntityPlayer player) {
            int slot = readInt(datain);
            String mode = readString(datain);
            return new MusePacketModeChangeRequest(player, mode, slot);
        }
    }
}
