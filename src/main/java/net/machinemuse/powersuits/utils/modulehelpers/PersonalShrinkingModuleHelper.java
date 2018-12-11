package net.machinemuse.powersuits.utils.modulehelpers;

import net.machinemuse.numina.item.IModularItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Helper methods for the Personal Shrinking Device module
 */
public class PersonalShrinkingModuleHelper {
    public static String TAG_COMPACT_MACHINES = "CompactMachines";
    public static String TAG_CAN_SHRINK = "canShrink";

    public static boolean getCanShrink(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = stack.getTagCompound();
            NBTTagCompound cmTag = ((itemTag.hasKey(TAG_COMPACT_MACHINES)) ? itemTag.getCompoundTag(TAG_COMPACT_MACHINES) : null);
            if (cmTag != null && cmTag.hasKey(TAG_CAN_SHRINK)) {
                return cmTag.getBoolean(TAG_CAN_SHRINK);
            }
        }
        return false;
    }

    public static void setCanShrink(@Nonnull ItemStack stack, boolean b) {
        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = stack.getTagCompound();
            NBTTagCompound cmTag = ((itemTag.hasKey(TAG_COMPACT_MACHINES)) ? itemTag.getCompoundTag(TAG_COMPACT_MACHINES) : (new NBTTagCompound()));
            cmTag.setBoolean(TAG_CAN_SHRINK, b);
            itemTag.setTag(TAG_COMPACT_MACHINES, cmTag);
        }
    }
}
