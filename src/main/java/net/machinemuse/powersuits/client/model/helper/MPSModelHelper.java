package net.machinemuse.powersuits.client.model.helper;

import net.machinemuse.numina.client.render.modelspec.ModelRegistry;
import net.machinemuse.numina.client.render.modelspec.TexturePartSpec;
import net.machinemuse.numina.constants.ModelSpecTags;
import net.machinemuse.numina.nbt.MuseNBTUtils;
import net.machinemuse.powersuits.basemod.MPSConfig;
import net.machinemuse.powersuits.constants.MPSResourceConstants;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MPSModelHelper {
    // One pass just to register the textures called from texture stitch event
    // another to register the models called from model bake event (second run)
    public static void loadArmorModels(@Nullable TextureStitchEvent event) {
        ArrayList<String> resourceList = new ArrayList<String>() {{
            add("/assets/powersuits/modelspec/armor2.xml");
            add("/assets/powersuits/modelspec/default_armor.xml");
            add("/assets/powersuits/modelspec/default_armorskin.xml");
            add("/assets/powersuits/modelspec/armor_skin2.xml");
            add("/assets/powersuits/modelspec/default_powerfist.xml");
        }};

        for (String resourceString : resourceList) {
            parseSpecFile(resourceString, event);
        }
//
//        URL resource = MPSModelHelper.class.getResource("/assets/powersuits/models/item/armor/modelspec.xml");
//        ModelSpecXMLReader.INSTANCE.parseFile(resource, event);
//        URL otherResource = MPSModelHelper.class.getResource("/assets/powersuits/models/item/armor/armor2.xml");
//        ModelSpecXMLReader.INSTANCE.parseFile(otherResource, event);

//        ModelPowerFistHelper.INSTANCE.loadPowerFistModels(event);
    }

    public static void parseSpecFile(String resourceString, @Nullable TextureStitchEvent event) {
        URL resource = MPSModelHelper.class.getResource(resourceString);
//        ModelSpecXMLReader.INSTANCE.parseFile(resource, event);
    }

    public static boolean hasHighPolyModel(ItemStack stack, EntityEquipmentSlot slot) {
        NBTTagCompound renderTag = getMuseRenderTag(stack, slot);

        // any tag other than the colours or texSpec tag is a ModelPartSpec tag
        for (String tagName : renderTag.keySet()) {
            if (Objects.equals(tagName, ModelSpecTags.NBT_TEXTURESPEC_TAG) || Objects.equals(tagName, ModelSpecTags.TAG_COLOURS))
                continue;
            else
                return true;
        }
        return false;
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound itemTag = MuseNBTUtils.getMuseItemTag(stack);
        NBTTagCompound renderTag = null;
        if (itemTag.contains(ModelSpecTags.TAG_RENDER, Constants.NBT.TAG_COMPOUND))
            renderTag = itemTag.getCompound(ModelSpecTags.TAG_RENDER);
        else if (itemTag.contains(ModelSpecTags.TAG_COSMETIC_PRESET, Constants.NBT.TAG_STRING))
            renderTag = MPSConfig.INSTANCE.getPresetNBTFor(stack, itemTag.getString(ModelSpecTags.TAG_COSMETIC_PRESET));

        if (renderTag != null)
            return renderTag;

        // if cosmetic presets are to be used, or powerfist customization is not allowed set to default preset
        if (!MPSConfig.INSTANCE.COSMETIC_USE_LEGACY_COSMETIC_SYSTEM.get() ||
                (stack.getItem() instanceof ItemPowerFist && MPSConfig.INSTANCE.COSMETIC_ALLOW_POWER_FIST_CUSTOMIZATOIN.get())) {
            itemTag.putString(ModelSpecTags.TAG_COSMETIC_PRESET, "Default");
            return MPSConfig.INSTANCE.getPresetNBTFor(stack, "Default");
        }

        renderTag = DefaultModelSpec.makeModelPrefs(stack, armorSlot);
        itemTag.put(ModelSpecTags.TAG_RENDER, renderTag);
        return renderTag;
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        EntityEquipmentSlot slot = stack.getEquipmentSlot();

        if (slot != null)
            return getMuseRenderTag(stack, slot);

        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemPowerArmor)
                return getMuseRenderTag(stack, ((ItemPowerArmor) stack.getItem()).getEquipmentSlot());
            if (stack.getItem() instanceof ItemPowerFist)
                return getMuseRenderTag(stack, EntityEquipmentSlot.MAINHAND);
        }
        return new NBTTagCompound();
    }

    public static String getArmorTexture(ItemStack stack, EntityEquipmentSlot slot) {
        NBTTagCompound renderTag = getMuseRenderTag(stack, slot);
        try {
            TexturePartSpec partSpec = (TexturePartSpec) ModelRegistry.getInstance().getPart(renderTag.getCompound(ModelSpecTags.NBT_TEXTURESPEC_TAG));
            return partSpec.getTextureLocation();
        } catch (Exception ignored) {
            return MPSResourceConstants.BLANK_ARMOR_MODEL_PATH;
        }
    }
}