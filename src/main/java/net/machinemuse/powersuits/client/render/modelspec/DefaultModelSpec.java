package net.machinemuse.powersuits.client.render.modelspec;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.utils.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.powersuits.powermodule.movement.JetPackModule.MODULE_JETPACK;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 9:11 AM, 29/04/13
 *
 * Ported to Java by lehjr on 11/8/16.
 * rewritten to be custom model compatible by lehjr on 12/26/17
 *
 * Special note: tried forEach() with a filter, but speed was up to 8 times slower
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

        List<NBTTagCompound> prefArray = new ArrayList<>();

        // ModelPartSpecs
        NBTTagList specList = new NBTTagList();

        // TextureSpecBase (only one texture visible at a time)
        NBTTagCompound texSpecTag = new NBTTagCompound();

        // List of EnumColour indexes
        List<Integer> colours = new ArrayList<>();

        // temp data holder
        NBTTagCompound tempNBT;

        for (SpecBase spec : ModelRegistry.getInstance().getSpecs()) {
            // Only generate NBT data from Specs marked as "default"
            if (spec.isDefault()) {

                /** Power Fist -------------------------------------------------------------------- */
                if (stack.getItem() instanceof ItemPowerFist && spec.getSpecType().equals(EnumSpecType.POWER_FIST)) {
                    colours = addNewColourstoList(colours, spec.getColours()); // merge new color int arrays in

                    for (PartSpecBase partSpec : spec.getPartSpecs()) {
                        if (partSpec instanceof ModelPartSpec) {
                            prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                    getNewColourIndex(colours, spec.getColours(), partSpec.defaultcolourindex),
                                    ((ModelPartSpec) partSpec).getGlow()));
                        }
                    }

                    /** Power Armor ------------------------------------------------------------------- */
                } else if (stack.getItem() instanceof ItemPowerArmor) {
                    colours = addNewColourstoList(colours, spec.getColours()); // merge new color int arrays in

                    // Armor Skin
                    if (spec.getSpecType().equals(EnumSpecType.ARMOR_SKIN) && spec.get(slot.getName()) != null) {
                        // only a single texture per equipment slot can be used at a time
                        texSpecTag = spec.get(slot.getName()).multiSet(new NBTTagCompound(),
                                getNewColourIndex(colours, spec.getColours(), spec.get(slot.getName()).defaultcolourindex));
                    }

                    // Armor models
                    else if (spec.getSpecType().equals(EnumSpecType.ARMOR_MODEL) && MPSConfig.INSTANCE.allowHighPollyArmorModels() ) {

                        for (PartSpecBase partSpec : spec.getPartSpecs()) {

                            if (partSpec.partName == "visor")
                                System.out.println("helm original index");


                            if (partSpec.binding.getSlot() == slot) {

                                // jet pack model not displayed by default
                                if (partSpec.binding.getItemState().equals("all") ||
                                        (partSpec.binding.getItemState().equals("jetpack") &&
                                                ModuleManager.INSTANCE.itemHasModule(stack, MODULE_JETPACK))) {
                                    prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(),
                                            getNewColourIndex(colours, spec.getColours(), partSpec.defaultcolourindex),
                                            ((ModelPartSpec) partSpec).getGlow()));
                                }
                            }
                        }
                    }
                }
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem : prefArray) {
            nbt.setTag(elem.getString(NuminaNBTConstants.TAG_MODEL) + "." + elem.getString(NuminaNBTConstants.TAG_PART), elem);
        }

        if (!specList.hasNoTags())
            nbt.setTag(MPSNBTConstants.NBT_SPECLIST_TAG, specList);

        if (!texSpecTag.hasNoTags())
            nbt.setTag(MPSNBTConstants.NBT_TEXTURESPEC_TAG, texSpecTag);

        nbt.setTag(NuminaNBTConstants.TAG_COLOURS, new NBTTagIntArray(colours));

        return nbt;
    }

    /**
     * When dealing with possibly multiple specs and color lists, new list needs to be created, since there is only one list per item.
     *
     */
    static List<Integer> addNewColourstoList(List<Integer> colours, List<Integer> coloursToAdd) {
        for (Integer i : coloursToAdd) {
            if (!colours.contains(i))
                colours.add(i);
        }
        return colours;
    }

    /**
     * new array means setting a new array index for the same value
     */
    public static int getNewColourIndex(List<Integer> colours, List<Integer> oldColours, Integer index) {
        return colours.indexOf(oldColours.get(index != null ? index : 0));
    }
}