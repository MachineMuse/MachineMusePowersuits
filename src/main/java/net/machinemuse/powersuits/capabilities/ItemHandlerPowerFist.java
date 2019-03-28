package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import java.util.ArrayList;
import java.util.List;

public class ItemHandlerPowerFist extends ItemStackHandler {
    // Scannable
    public static final int SCANNER_ACTIVE_MODULE_COUNT = 3;
    public static final int SCANNER_TOTAL_MODULE_COUNT = 9;
    public static final int TOTAL_SIZE = SCANNER_TOTAL_MODULE_COUNT + 10;
    private static final String TAG_ITEMS = "items";
    private final ItemStack container;


    public ItemHandlerPowerFist(final ItemStack container) {
        super(TOTAL_SIZE);
        this.container = container;

    }

    public IItemHandler getActiveModules() {
        return new RangedWrapper(this, 0, SCANNER_ACTIVE_MODULE_COUNT);
    }

    public IItemHandler getInactiveModules() {
        return new RangedWrapper(this, SCANNER_ACTIVE_MODULE_COUNT, SCANNER_TOTAL_MODULE_COUNT);
    }

    // Store the emulated tools in an inventory. This won't be accessable by the player.
    public IItemHandler getEmulatedTools() {
        return new RangedWrapper(this, SCANNER_TOTAL_MODULE_COUNT, TOTAL_SIZE);
    }

    public void updateFromNBT() {
        final NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(container);

        // TODO: edit to hold other things like emulated tools
        if (itemTag != null && itemTag.hasKey(TAG_ITEMS, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) itemTag.getTag(TAG_ITEMS));
            if (stacks.size() != TOTAL_SIZE) {
                final List<ItemStack> oldStacks = new ArrayList<>(stacks);
                setSize(TOTAL_SIZE);
                final int count = Math.min(TOTAL_SIZE, oldStacks.size());
                for (int slot = 0; slot < count; slot++) {
                    stacks.set(slot, oldStacks.get(slot));
                }
            }
        }
    }

    @Override
    protected void onContentsChanged(final int slot) {
        super.onContentsChanged(slot);
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(container);
        itemTag.setTag(TAG_ITEMS, serializeNBT());
    }
}