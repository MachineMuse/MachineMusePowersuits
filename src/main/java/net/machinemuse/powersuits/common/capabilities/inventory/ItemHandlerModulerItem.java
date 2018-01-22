package net.machinemuse.powersuits.common.capabilities.inventory;


import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public final class ItemHandlerModulerItem extends ItemStackHandler {
    private static final String TAG_ITEMS = "items";

    private final ItemStack container;

    public ItemHandlerModulerItem(final ItemStack container) {
        // Starting size for all the Armor, power fist and w/e else
        // size can be adjusted later on by adding upgrade modules
        super(4);
        this.container = container;
    }


    public void updateFromNBT() {
        final NBTTagCompound nbt = container.getTagCompound();
        if (nbt != null && nbt.hasKey(TAG_ITEMS, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) nbt.getTag(TAG_ITEMS));
//            if (stacks.size() != li.cil.scannable.common.config.Constants.SCANNER_TOTAL_MODULE_COUNT) {
//                final List<ItemStack> oldStacks = new ArrayList<>(stacks);
//                setSize(li.cil.scannable.common.config.Constants.SCANNER_TOTAL_MODULE_COUNT);
//                final int count = Math.min(li.cil.scannable.common.config.Constants.SCANNER_TOTAL_MODULE_COUNT, oldStacks.size());
//                for (int slot = 0; slot < count; slot++) {
//                    stacks.set(slot, oldStacks.get(slot));
//                }
//            }
        }
    }

    /** these 2 methods look like a way of returning a slice of the total inventory, almost like a partition I guess.
     * This way a range of slots can be designated as a special purpose.

     */


    public IItemHandler getActiveModules() {
        return null;
//        return new RangedWrapper(this, 0, li.cil.scannable.common.config.Constants.SCANNER_ACTIVE_MODULE_COUNT);
    }

    public IItemHandler getInactiveModules() {
        return null;
//        return new RangedWrapper(this, li.cil.scannable.common.config.Constants.SCANNER_ACTIVE_MODULE_COUNT, li.cil.scannable.common.config.Constants.SCANNER_TOTAL_MODULE_COUNT);
    }




    /*** IItemHandler --------------------------------------------------------- */
    @Override
    protected int getStackLimit(final int slot, @Nonnull final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        return 1;
    }

    /*** ItemStackHandler ----------------------------------------------------- */
    @Override
    protected void onContentsChanged(final int slot) {
        super.onContentsChanged(slot);
        container.setTagInfo(TAG_ITEMS, serializeNBT());
    }
}
