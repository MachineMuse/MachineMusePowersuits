package net.machinemuse.powersuits.item.armor;

import net.machinemuse.numina.basemod.MuseLogger;
import net.machinemuse.numina.client.render.modelspec.ModelRegistry;
import net.machinemuse.numina.client.render.modelspec.TexturePartSpec;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.string.MuseStringUtils;
import net.machinemuse.powersuits.client.model.helper.MPSModelHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 * <p>
 * Ported to Java by lehjr on 11/4/16.
 */
public interface IModularItemBase {
    default Colour getColorFromItemStack(ItemStack stack) {
        try {
            NBTTagCompound renderTag = MPSModelHelper.getMuseRenderTag(stack);
            if (renderTag.contains(ModelSpecTags.NBT_TEXTURESPEC_TAG)) {
                TexturePartSpec partSpec = (TexturePartSpec) ModelRegistry.getInstance().getPart(renderTag.getCompound(ModelSpecTags.NBT_TEXTURESPEC_TAG));
                NBTTagCompound specTag = renderTag.getCompound(ModelSpecTags.NBT_TEXTURESPEC_TAG);
                int index = partSpec.getColourIndex(specTag);
                int[] colours = renderTag.getIntArray(ModelSpecTags.TAG_COLOURS);
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

    default double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }
}