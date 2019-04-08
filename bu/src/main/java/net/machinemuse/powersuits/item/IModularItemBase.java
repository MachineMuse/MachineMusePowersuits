package net.machinemuse.powersuits.item;

import net.machinemuse.numina.capabilities.energy.adapter.IMuseElectricItem;
import net.machinemuse.numina.common.constants.NuminaNBTConstants;
import net.machinemuse.numina.item.IModularItem;
import net.machinemuse.numina.module.IModuleManager;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.client.render.modelspec.ModelRegistry;
import net.machinemuse.powersuits.client.render.modelspec.TexturePartSpec;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.utils.MuseStringUtils;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IModularItemBase extends IModularItem, IMuseElectricItem {
    @Override
    default double getMaxBaseHeat(@Nonnull ItemStack itemStack) {
        return MPSConfig.INSTANCE.getBaseMaxHeat(itemStack);
    }

    default Colour getColorFromItemStack(ItemStack stack) {
        try {
            NBTTagCompound renderTag = MPSNBTUtils.getMuseRenderTag(stack);
            if (renderTag.hasKey(MPSNBTConstants.NBT_TEXTURESPEC_TAG)) {
                TexturePartSpec partSpec = (TexturePartSpec) ModelRegistry.getInstance().getPart(renderTag.getCompoundTag(MPSNBTConstants.NBT_TEXTURESPEC_TAG));
                NBTTagCompound specTag = renderTag.getCompoundTag(MPSNBTConstants.NBT_TEXTURESPEC_TAG);
                int index = partSpec.getColourIndex(specTag);
                int[] colours = renderTag.getIntArray(NuminaNBTConstants.TAG_COLOURS);
                if (colours.length > index)
                    return new Colour(colours[index]);
            }
        } catch (Exception e) {
            MuseLogger.logException("something failed here: ", e);
        }
        return Colour.WHITE;
    }

    default String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

    @Override
    default IModuleManager getModuleManager() {
        return ModuleManager.INSTANCE;
    }

    default double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }
}