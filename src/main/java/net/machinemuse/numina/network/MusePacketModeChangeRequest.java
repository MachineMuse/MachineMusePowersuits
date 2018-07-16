package net.machinemuse.numina.network;

import io.netty.buffer.ByteBufInputStream;
import net.machinemuse.numina.api.item.IModeChangingItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
    public IMusePackager packager() {
        return MusePacketModeChangeRequestPackager.INSTANCE;
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
            if (!stack.isEmpty()) {
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

    public static MusePacketModeChangeRequestPackager getPackagerInstance() {
        return MusePacketModeChangeRequestPackager.INSTANCE;
    }

    public enum MusePacketModeChangeRequestPackager implements IMusePackager {
        INSTANCE;

        @Override
        public MusePacket read(ByteBufInputStream datain, EntityPlayer player) {
            int slot = readInt(datain);
            String mode = readString(datain);
            return new MusePacketModeChangeRequest(player, mode, slot);
        }
    }
}