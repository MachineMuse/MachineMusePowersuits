package net.machinemuse.numina.api.capability_ports.itemwrapper;

import net.machinemuse.numina.api.capability_ports.inventory.IModularItemCapability;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ModularItemWrapper extends ItemStackHandler implements IModularItemCapability {
    protected ItemStack container;
    private static final String TAG_MODULES = "modules";
    int slotCount;

    public ModularItemWrapper(@Nonnull ItemStack container, int slotCount, NBTTagCompound nbt) {
        super(slotCount);
        this.container = container;
        this.slotCount = slotCount;
        if (nbt != null)
            deserializeNBT(nbt);
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        System.out.println("trying to insert item");
/*
 itemTag: {"Maximum Heat":900000,"Current Heat":0,item.powersuits.module.debugModule:{Active:1b},render:{}}

 */

        if (stack.isEmpty())
            System.out.println("stack is empty!!!");
        else
            System.out.println("stack is NOT empty!!!");

        System.out.println("serialize: " + serializeNBT());

        return super.insertItem(slot, stack, simulate);
    }

    public void updateFromNBT() {
        final NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);
        if (nbt != null && nbt.hasKey(TAG_MODULES, Constants.NBT.TAG_COMPOUND)) {
            deserializeNBT((NBTTagCompound) nbt.getTag(TAG_MODULES));

            if (stacks.size() != slotCount) {
                final List<ItemStack> oldStacks = new ArrayList<>(stacks);
                setSize(slotCount); // FIXME : use config reference?
                final int count = Math.min(slotCount, oldStacks.size());
                for (int slot = 0; slot < count; slot++) {
                    stacks.set(slot, oldStacks.get(slot));
                }
            }
        }
    }




    // --------------------------------------------------------------------- //
    // IItemHandler

    @Override
    protected int getStackLimit(final int slot, @Nonnull final ItemStack stack) {
        if (stack.isEmpty()) {
            return 0;
        }
        return 1;
    }

    // --------------------------------------------------------------------- //
    // ItemStackHandler

    @Override
    protected void onContentsChanged(final int slot) {
        super.onContentsChanged(slot);
        NBTTagCompound nbt = MuseItemUtils.getMuseItemTag(container);

        System.out.println("nbt: " + nbt) ;
        nbt.setTag(TAG_MODULES, serializeNBT());
    }
}
