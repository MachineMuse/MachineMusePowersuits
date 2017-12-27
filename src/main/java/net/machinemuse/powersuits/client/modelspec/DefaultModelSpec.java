package net.machinemuse.powersuits.client.modelspec;

import com.google.common.collect.Iterables;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.common.items.old.ItemPowerArmor;
import net.machinemuse.powersuits.common.items.old.ItemPowerFist;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

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
// TODO: support for specs with custom models disabled. (ie: using spec tags for whole spec rather than using part tags)
// TODO: support custom default colors
@SideOnly(Side.CLIENT)
public class DefaultModelSpec {
    public static NBTTagCompound makeModelPrefs(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor)
            return makeModelPrefs(stack, ((ItemPowerArmor) stack.getItem()).armorType);
        return makeModelPrefs(stack, EntityEquipmentSlot.MAINHAND);
    }

    public static NBTTagCompound makeModelPrefs(ItemStack stack, EntityEquipmentSlot slot) {
        List<NBTTagCompound> prefArray = new ArrayList<>();
        for (Spec spec : ModelRegistry.getInstance().getSpecs()) {
            if (spec.isDefault()) {
                if (stack.getItem() instanceof ItemPowerArmor) {
                    NBTTagCompound tempNBT;
                    for (PartSpec partSpec : spec.getPartSpecs()) {
                        if (partSpec.binding.getSlot() == slot && partSpec.binding.getItemState().equals("all")) {
                            // high poly armor handled in the ModelSpecXML Reader so no need to use config options here
                            if (spec.getSpecType().equals(EnumSpecType.ARMOR_MODEL)) {
                                tempNBT = ((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(), partSpec.getDefaultColourIndex(), ((ModelPartSpec) partSpec).getGlow());
                                prefArray.add(tempNBT);
                            } else if (spec.getSpecType().equals(EnumSpecType.ARMOR_SKIN)) {
                                tempNBT = ((TexturePartSpec) partSpec).multiSet(new NBTTagCompound(), ((TexturePartSpec) partSpec).textureLocation, partSpec.getDefaultColourIndex());
                                prefArray.add(tempNBT);
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemPowerFist && spec.getSpecType().equals(EnumSpecType.POWER_FIST)) {
                    for (PartSpec partSpec : spec.getPartSpecs()) {
                        if (partSpec instanceof ModelPartSpec)
                            prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(), partSpec.getDefaultColourIndex(), ((ModelPartSpec) partSpec).getGlow()));
                    }
                }
            }
        }

        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem : prefArray) {
            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
        }
        return nbt;
    }
}