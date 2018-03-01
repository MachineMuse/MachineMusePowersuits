package net.machinemuse.numina.utils.module.helpers;

import net.machinemuse.numina.api.item.IMuseItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class PersonalShrinkingModuleHelper {
    public static boolean getCanShrink(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = stack.getTagCompound();
            NBTTagCompound cmTag = ((itemTag.hasKey("CompactMachines")) ? itemTag.getCompoundTag("CompactMachines") : null);
            if (cmTag != null && cmTag.hasKey("canShrink")) {
                return cmTag.getBoolean("canShrink");
            }
        }
        return false;
    }

    public static void setCanShrink(ItemStack stack, boolean b) {
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            NBTTagCompound itemTag = stack.getTagCompound();
            NBTTagCompound cmTag = ((itemTag.hasKey("CompactMachines")) ? itemTag.getCompoundTag("CompactMachines") : (new NBTTagCompound()));
            cmTag.setBoolean("canShrink", b);
            itemTag.setTag("CompactMachines", cmTag);
        }
    }
}
