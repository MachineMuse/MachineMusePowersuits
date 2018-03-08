package net.machinemuse.powersuits.utils.module.helpers;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class WirelessTerminalHelper {


    public static NBTTagCompound getFluidTermTag(ItemStack stack) {
        NBTTagCompound ret = null;
        if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
            ret = NuminaNBTUtils.getMuseItemTag(stack).getCompoundTag("AppEng EC Wireless Fluid Terminal");
        }
        return ret;
    }

    public static void setFluidTermTag(ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound t = NuminaNBTUtils.getMuseItemTag(stack);
        t.setTag("AppEng EC Wireless Fluid Terminal", tag);
        stack.getTagCompound().setTag(NuminaNBTConstants.NBTPREFIX, t);
    }
}
