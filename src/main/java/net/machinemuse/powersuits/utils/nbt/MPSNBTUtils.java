package net.machinemuse.powersuits.utils.nbt;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.api.constants.MPSResourceConstants;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.TexturePartSpec;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;



public class MPSNBTUtils {
    @Nullable
    static NBTTagCompound getMuseCosmeticTag(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.TAG_COSMETIC, Constants.NBT.TAG_COMPOUND))
            return nbt.getCompoundTag(NuminaNBTConstants.TAG_COSMETIC);
        return null;
    }

    @Nullable
    static NBTTagCompound getRenderTag(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.TAG_RENDER, Constants.NBT.TAG_COMPOUND))
            return nbt.getCompoundTag(NuminaNBTConstants.TAG_RENDER);
        return null;
    }

    @Nullable
    static String getPresetName(NBTTagCompound nbt) {
        if (nbt.hasKey(NuminaNBTConstants.TAG_COSMETIC_PRESET, Constants.NBT.TAG_STRING))
            return nbt.getString(NuminaNBTConstants.TAG_COSMETIC_PRESET);
        return null;
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        NBTTagCompound renderTag;

        // this will be null on legacy rendering until user
        NBTTagCompound cosmeticTag = getMuseCosmeticTag(itemTag);

        // move render tag to under cosmetic tag, creating whatever tag structure needed
        if (cosmeticTag == null) {
            // if cosmetic presets are to be used, or powerfist customization is not allowed set to default preset
            if (!MPSConfig.INSTANCE.useLegacyCosmeticSystem() ||
                    (stack.getItem() instanceof ItemPowerFist && MPSConfig.INSTANCE.allowPowerFistCustomization())) {
                cosmeticTag = new NBTTagCompound();
                cosmeticTag.setString(NuminaNBTConstants.TAG_COSMETIC_PRESET, "Default");
                itemTag.removeTag(NuminaNBTConstants.TAG_RENDER);
                itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, cosmeticTag);
                return MPSConfig.INSTANCE.getPresetNBTFor(stack, "Default");
            } else {
                renderTag = getRenderTag(itemTag);
                if (renderTag != null) {
                    itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, renderTag);
                    itemTag.removeTag(NuminaNBTConstants.TAG_RENDER);
                    return renderTag;
                }
                renderTag = new NBTTagCompound();
                renderTag.setTag(NuminaNBTConstants.TAG_RENDER, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
                itemTag.setTag(NuminaNBTConstants.TAG_COSMETIC, renderTag);
                return renderTag;
            }
        }

        // cosmetic tag exists
        renderTag = getRenderTag(cosmeticTag);
        // allow customization so use render tag
        if (renderTag == null && !MPSConfig.INSTANCE.useLegacyCosmeticSystem() ||
                (stack.getItem() instanceof ItemPowerFist && MPSConfig.INSTANCE.allowPowerFistCustomization())) {
            if (renderTag != null)
                return renderTag;
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            cosmeticTag.removeTag(NuminaNBTConstants.TAG_RENDER);
            cosmeticTag.setTag(NuminaNBTConstants.TAG_RENDER, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
            return getRenderTag(cosmeticTag);
        }

        String presetName = getPresetName(cosmeticTag);
        if (presetName != null) {
            renderTag = MPSConfig.INSTANCE.getPresetNBTFor(stack, presetName);
            if (renderTag  == null) {
                MuseLogger.logDebug("Missing settings for preset: " + presetName);
                renderTag = MPSConfig.INSTANCE.getPresetNBTFor(stack, "Default");
            }
            return renderTag;
        }
        return MPSConfig.INSTANCE.getPresetNBTFor(stack, "Default");
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemPowerArmor)
                return getMuseRenderTag(stack, ((ItemPowerArmor) stack.getItem()).armorType);
            if (stack.getItem() instanceof ItemPowerFist)
                return getMuseRenderTag(stack, EntityEquipmentSlot.MAINHAND);
        }
        return new NBTTagCompound();
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