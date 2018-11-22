package net.machinemuse.powersuits.utils.nbt;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.TexturePartSpec;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.Objects;

import static net.machinemuse.numina.api.constants.NuminaNBTConstants.TAG_RENDER;


public class MPSNBTUtils {

    // TODO: cosmetic tag

    /**
        MuseCosmeticTag: new parent tag for cosmetic stuff

        MuseRenderTag: child tag under cosmetic tag ... designates legacy style rendering

        MuseCosmeticPreset: child tag under cosmetic tag ... value is a key for lookup in the config system

        MuseLegacyRenderTag: legacy rendering that has not yet been converted to new system.








     */

//    public static NBTTagCompound getMuseCosmeticTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
//        NBTTagCompound tag = MuseNBTUtils.getMuseItemTag(stack);
//
//
//
//    }











    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound tag = MuseNBTUtils.getMuseItemTag(stack);
        if (!tag.hasKey(TAG_RENDER) || !(tag.getTag(TAG_RENDER) instanceof NBTTagCompound)) {
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            tag.removeTag(TAG_RENDER);
            tag.setTag(TAG_RENDER, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
        }
        return tag.getCompoundTag(TAG_RENDER);
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        if (!itemTag.hasKey(TAG_RENDER) || !(itemTag.getTag(TAG_RENDER) instanceof NBTTagCompound)) {
            itemTag.removeTag(TAG_RENDER);
            itemTag.setTag(TAG_RENDER, DefaultModelSpec.makeModelPrefs(stack));
        }
        return itemTag.getCompoundTag(TAG_RENDER);
    }

    public static String getArmorTexture(ItemStack stack, EntityEquipmentSlot slot) {
        NBTTagCompound renderTag = getMuseRenderTag(stack, slot);
        try {
            TexturePartSpec partSpec = (TexturePartSpec) ModelRegistry.getInstance().getPart(renderTag.getCompoundTag(MPSNBTConstants.NBT_TEXTURESPEC_TAG));
            return partSpec.getTextureLocation();
        } catch (Exception ignored) {
            return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;
        }
    }

    public static boolean hasHighPolyModel(ItemStack stack, EntityEquipmentSlot slot) {
        NBTTagCompound renderTag = getMuseRenderTag(stack, slot);
        // any tag other than the colours or texSpec tag is a ModelPartSpec tag
        for (String tagName : renderTag.getKeySet()) {
            if (Objects.equals(tagName, MPSNBTConstants.NBT_TEXTURESPEC_TAG) || Objects.equals(tagName, NuminaNBTConstants.TAG_COLOURS))
                continue;
            else
                return true;
        }
        return false;
    }
}