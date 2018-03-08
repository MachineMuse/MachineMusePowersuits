package net.machinemuse.numina.utils.nbt;

import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;

public class MPSRenderTag {
    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound tag = NuminaNBTUtils.getMuseItemTag(stack);
        if (!tag.hasKey(MPSNBTConstants.NBT_RENDER_TAG, Constants.NBT.TAG_COMPOUND)) {
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            tag.removeTag(MPSNBTConstants.NBT_RENDER_TAG);
            tag.setTag(MPSNBTConstants.NBT_RENDER_TAG, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
        }
        return tag.getCompoundTag(MPSNBTConstants.NBT_RENDER_TAG);
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor)
            return getMuseRenderTag(stack, ((ItemPowerArmor) stack.getItem()).armorType);

        NBTTagCompound tag = NuminaNBTUtils.getMuseItemTag(stack);
        if (!tag.hasKey(MPSNBTConstants.NBT_RENDER_TAG, Constants.NBT.TAG_COMPOUND)) {
            tag.removeTag(MPSNBTConstants.NBT_RENDER_TAG);
            tag.setTag(MPSNBTConstants.NBT_RENDER_TAG, new NBTTagCompound());
        }
        return tag.getCompoundTag(MPSNBTConstants.NBT_RENDER_TAG);
    }

}
