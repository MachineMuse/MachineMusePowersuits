package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.powersuits.api.constants.MPSModuleConstants.MODULE_HIGH_POLY_ARMOR;
import static net.machinemuse.powersuits.powermodule.movement.JetPackModule.MODULE_JETPACK;

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
        if (stack != null) {
            if (stack.getItem() instanceof ItemPowerArmor)
                return makeModelPrefs(stack, ((ItemPowerArmor) stack.getItem()).armorType);
            if (stack.getItem() instanceof ItemPowerFist)
                return makeModelPrefs(stack, EntityEquipmentSlot.MAINHAND);
        }
        return new NBTTagCompound();
    }

    public static NBTTagCompound makeModelPrefs(ItemStack stack, EntityEquipmentSlot slot) {
        if (stack.isEmpty())
            return new NBTTagCompound();

        System.out.println("slot: " + slot.getName());
        System.out.println("stack: " + stack.getUnlocalizedName());

        System.out.println("modelk reg size: " + ModelRegistry.getInstance().getSpecs().toString());

        List<NBTTagCompound> prefArray = new ArrayList<>();

        // ModelPartSpecs
        NBTTagList specList = new NBTTagList();

        // TextureSpecBase (only one texture visible at a time)
        NBTTagCompound texSpecTag = new NBTTagCompound();

        // List of EnumColour indexes
        int[] colours = {Colour.WHITE.getInt()};

        // temp data holder
        NBTTagCompound tempNBT;






        for (SpecBase spec : ModelRegistry.getInstance().getSpecs()) {
            System.out.println("SpecStuff =============================================");
            System.out.println(spec.getDisaplayName());
            System.out.println(spec.getName());
            System.out.println(spec.getOwnName());
            System.out.println(spec.getSpecType().name);

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println("SpecStuff =============================================");

            if (spec.isDefault()) {
                if (stack.getItem() instanceof ItemPowerArmor) {
                    // if only default values are allowed then we only need to create a spec list
//                    if (spec instanceof ModelSpec && MPSConfig.INSTANCE.allowHighPollyArmorModels() &&
//                            // NBTTagList for default settings
//                            !MPSConfig.INSTANCE.allowCustomHighPollyArmor() &&
//                            ModuleManager.INSTANCE.itemHasActiveModule(stack, MPSModuleConstants.MODULE_HIGH_POLY_ARMOR)) {
//                        NBTTagCompound defaultModelSpec = new NBTTagCompound();
//
//
//                        for (PartSpecBase partSpec : spec.getPartSpecs()) {
//                            int colorIndex = partSpec.getColourIndex(defaultModelSpec);
//
//
//                            getEnumColourIndex();
//                            partSpec.setModel();
//
//                            setDefaultColourArrayIndex((byte) ArrayUtils.indexOf(colours, colorIndex));
//                        }
//
//
//                        defaultModelSpec.setString("model", spec.getOwnName());
//                        specList.appendTag(defaultModelSpec);
//                    } else {
                    for (PartSpecBase partSpec : spec.getPartSpecs()) {
                        if (partSpec.binding.getSlot() == slot) {
                            // if high poly armor model and is allowed
                            if (spec.getSpecType().equals(EnumSpecType.ARMOR_MODEL) &&
                                    // FIXME: cosmetic modules to be phased out.
//                                    ModuleManager.INSTANCE.itemHasActiveModule(stack, MODULE_HIGH_POLY_ARMOR)
                                    MPSConfig.INSTANCE.allowHighPollyArmorModels() ) {



                                if (partSpec.binding.getItemState().equals("all") ||
                                        (partSpec.binding.getItemState().equals("jetpack") &&
                                                ModuleManager.INSTANCE.itemHasModule(stack, MODULE_JETPACK))) {

                                    int colorIndex = partSpec.defaultcolourindex !=null ? partSpec.defaultcolourindex : 0;
//                                        partSpec.setDefaultColourArrayIndex((byte) ArrayUtils.indexOf(colours, colorIndex));

                                    tempNBT = ((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                            colorIndex,
                                            ((ModelPartSpec) partSpec).getGlow());
                                    prefArray.add(tempNBT);
                                }
                            } else if (spec.getSpecType().equals(EnumSpecType.ARMOR_SKIN)) {
                                // set up color index settings
                                int colorIndex = spec.get(slot.getName()).defaultcolourindex;
                                colours = maybeAddColour(colours, colorIndex);
//                                    spec.get(slot.getName()).setDefaultColourArrayIndex((int) ArrayUtils.indexOf(colours, colorIndex));

                                // only single texture can be used at a time
                                texSpecTag = partSpec.multiSet(new NBTTagCompound(),
                                        colorIndex);
                            }
                        }
                    }
                }
            } else if (stack.getItem() instanceof ItemPowerFist && spec.getSpecType().equals(EnumSpecType.POWER_FIST)) {
//                    if (!MPSConfig.INSTANCE.allowCustomPowerFistModels()) {
//                        NBTTagCompound defaultModelSpec = new NBTTagCompound();
//                        defaultModelSpec.setString("model", spec.getOwnName());
//                        specList.appendTag(defaultModelSpec);
//
//                        for (PartSpecBase partSpec : spec.getPartSpecs()) {
//                            byte colorIndex = partSpec.getEnumColourIndex();
//                            partSpec.setDefaultColourArrayIndex((byte) ArrayUtils.indexOf(colours, colorIndex));
//                        }
//                    } else {
                for (PartSpecBase partSpec : spec.getPartSpecs()) {
                    if (partSpec instanceof ModelPartSpec) {
                        int colorIndex = partSpec.defaultcolourindex !=null ? partSpec.defaultcolourindex : 0;
//                                partSpec.setDefaultColourArrayIndex((byte) ArrayUtils.indexOf(colours, colorIndex));
                        prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                colorIndex, ((ModelPartSpec) partSpec).getGlow()));

                    }
                }
//                    }
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem : prefArray) {
            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
        }

        if (!specList.hasNoTags())
            nbt.setTag(MPSNBTConstants.NBT_SPECLIST_TAG, specList);

        if (!texSpecTag.hasNoTags())
            nbt.setTag(MPSNBTConstants.NBT_TEXTURESPEC_TAG, texSpecTag);

        nbt.setTag("colours", new NBTTagIntArray(colours));

        System.out.println("nbt: " + nbt.toString());



        return nbt;
    }

    static int[] maybeAddColour(int[] colourIndexes, int indexToCheck) {
        if (!ArrayUtils.contains(colourIndexes, indexToCheck))
            colourIndexes = ArrayUtils.add(colourIndexes, indexToCheck);
        return colourIndexes;
    }
}