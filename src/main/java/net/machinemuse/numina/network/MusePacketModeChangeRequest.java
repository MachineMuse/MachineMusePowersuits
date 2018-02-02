package net.machinemuse.numina.network;

import net.machinemuse.numina.api.item.IModeChangingItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
            if (stack != ItemStack.EMPTY) {
                Item item = stack.getItem();
                if (item instanceof IModeChangingItem) {
                    List<String> modes = ((IModeChangingItem) item).getValidModes(stack);
                    if (modes.contains(mode)) {
                        ((IModeChangingItem) item).setActiveMode(stack, mode);
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
