package net.machinemuse.powersuits.client.modelspec;

import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.geometry.EnumColour;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmor;
import net.machinemuse.powersuits.common.items.old.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.NBT_SPECLIST_TAG;
import static net.machinemuse.powersuits.common.MPSConstants.NBT_TEXTURESPEC_TAG;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:11 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 * rewritten to be custom model compatible by lehjr on 12/26/17
 *
 * Special note: tried forEach() with a filter, but speed was up to 8 times slower
 *
 */
@SideOnly(Side.CLIENT)
public class DefaultModelSpec {
    public static NBTTagCompound makeModelPrefs(ItemStack stack) {
        if (!stack.isEmpty()) {
            if (stack.getItem() instanceof ItemPowerArmor)
                return makeModelPrefs(stack, ((ItemPowerArmor) stack.getItem()).armorType);
            if (stack.getItem() instanceof ItemPowerFist)
                return makeModelPrefs(stack, EntityEquipmentSlot.MAINHAND);
        }
        return new NBTTagCompound();
    }

    public static NBTTagCompound makeModelPrefs(ItemStack stack, EntityEquipmentSlot slot) {
        List<NBTTagCompound> prefArray = new ArrayList<>();

        // ModelPartSpecs
        NBTTagList specList = new NBTTagList();

        // TextureSpec (only one texture visible at a time)
        NBTTagCompound texSpecTag = new NBTTagCompound();

        // List of EnumColour indexes
        byte[] colours = {(byte)EnumColour.WHITE.getIndex()};

        // temp data holder
        NBTTagCompound tempNBT;
        for (Spec spec : ModelRegistry.getInstance().getSpecs()) {
            if (spec.isDefault()) {
                if (stack.getItem() instanceof ItemPowerArmor) {
                    // if only default values are allowed then we only need to create a spec list
                    if (spec instanceof ModelSpec && MPSSettings.allowHighPollyArmorModels() &&
                            // NBTTagList for default settings
                            !MPSSettings.allowCustomHighPollyArmor() &&
                            ModuleManager.itemHasActiveModule(stack, MPSConstants.HIGH_POLY_ARMOR)) {
                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            byte colorIndex = partSpec.getEnumColourIndex();
                            partSpec.setDefaultColourArrayIndex((byte)ArrayUtils.indexOf(colours, colorIndex));
                        }


                        NBTTagCompound defaultModelSpec = new NBTTagCompound();
                        defaultModelSpec.setString("model", spec.getOwnName());
                        specList.appendTag(defaultModelSpec);
                    } else {
                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            if (partSpec.binding.getSlot() == slot) {
                                // if high poly armor model and is allowed
                                if (spec.getSpecType().equals(EnumSpecType.ARMOR_MODEL) &&
                                        ModuleManager.itemHasActiveModule(stack, MPSConstants.HIGH_POLY_ARMOR) &&
                                        MPSSettings.allowHighPollyArmorModels()) {
                                    if (partSpec.binding.getItemState().equals("all") ||
                                            (partSpec.binding.getItemState().equals("jetpack") &&
                                                    ModuleManager.itemHasModule(stack, MPSConstants.MODULE_JETPACK))) {

                                        byte colorIndex = partSpec.getEnumColourIndex();
                                        partSpec.setDefaultColourArrayIndex((byte)ArrayUtils.indexOf(colours, colorIndex));

                                        tempNBT = ((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                                (byte)partSpec.getDefaultColourArrayIndex(),
                                                ((ModelPartSpec) partSpec).getGlow());
                                        prefArray.add(tempNBT);
                                    }
                                } else if (spec.getSpecType().equals(EnumSpecType.ARMOR_SKIN)) {
                                    // set up color index settings
                                    byte colorIndex = spec.get(slot.getName()).getEnumColourIndex();
                                    colours = maybeAddColour(colours, colorIndex);
                                    spec.get(slot.getName()).setDefaultColourArrayIndex((byte)ArrayUtils.indexOf(colours, colorIndex));

                                    // only single texture can be used at a time
                                    texSpecTag = partSpec.multiSet(new NBTTagCompound(),
                                            (byte)partSpec.getDefaultColourArrayIndex());
                                }
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemPowerFist && spec.getSpecType().equals(EnumSpecType.POWER_FIST)) {
                    if (!MPSSettings.allowCustomPowerFistModels()) {
                        NBTTagCompound defaultModelSpec = new NBTTagCompound();
                        defaultModelSpec.setString("model", spec.getOwnName());
                        specList.appendTag(defaultModelSpec);

                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            byte colorIndex = partSpec.getEnumColourIndex();
                            partSpec.setDefaultColourArrayIndex((byte)ArrayUtils.indexOf(colours, colorIndex));
                        }
                    } else {
                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            if (partSpec instanceof ModelPartSpec) {
                                byte colorIndex = partSpec.getEnumColourIndex();
                                partSpec.setDefaultColourArrayIndex((byte)ArrayUtils.indexOf(colours, colorIndex));
                                prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                        (byte)partSpec.getDefaultColourArrayIndex(), ((ModelPartSpec) partSpec).getGlow()));

                            }
                        }
                    }
                }
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem : prefArray) {
            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
        }

        if (!specList.hasNoTags())
            nbt.setTag(NBT_SPECLIST_TAG, specList);

        if (!texSpecTag.hasNoTags())
            nbt.setTag(NBT_TEXTURESPEC_TAG, texSpecTag);

        nbt.setTag("colours", new NBTTagByteArray(colours));

        System.out.println("nbt: " + nbt.toString());



        return nbt;
    }

    static byte[] maybeAddColour(byte[] enumColourIndexes, byte indexToCheck) {
        if (!ArrayUtils.contains(enumColourIndexes, indexToCheck))
            enumColourIndexes = ArrayUtils.add(enumColourIndexes, indexToCheck);
        return enumColourIndexes;
    }
}