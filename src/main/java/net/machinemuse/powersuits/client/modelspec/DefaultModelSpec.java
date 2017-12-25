package net.machinemuse.powersuits.client.modelspec;

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
 */

// TODO: Eliminate this !!!!! No, really, this should be handled by the XML reader
@SideOnly(Side.CLIENT)
public class DefaultModelSpec {
    public static Colour normalcolour = Colour.WHITE;
    public static Colour glowcolour = new Colour(17.0 / 255, 78.0 / 255, 1, 1);


    public static NBTTagCompound makeModelPrefs(ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor)
            return makeModelPrefs(stack, ((ItemPowerArmor) stack.getItem()).armorType);
        return makeModelPrefs(stack, EntityEquipmentSlot.MAINHAND);
    }

    public static NBTTagCompound makeModelPrefs(ItemStack stack, EntityEquipmentSlot slot) {
        List<NBTTagCompound> list = new ArrayList<>();

        List<NBTTagCompound> prefArray = new ArrayList<>();


        String modelName;

        System.out.println("Making prefs for " + stack.getDisplayName());

        for (Spec spec: ModelRegistry.getInstance().getSpecs()) {
            if (spec.isDefault()) {
                if (stack.getItem() instanceof ItemPowerArmor) {
                    // high poly armor handled in the ModelSpecXML Reader so no need to use config options here
                    if (spec.getSpecType().equals(EnumSpecType.ARMOR_MODEL) || spec.getSpecType().equals(EnumSpecType.ARMOR_SKIN)) {
                        // TODO: get parts
                        modelName = spec.getName();
                        System.out.println("ModelName: " + modelName);

                        NBTTagCompound tempNBT;
                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            if (partSpec.binding.getSlot() == slot) {
                                if (partSpec instanceof ModelPartSpec) {
                                    System.out.println("partSpec: " + partSpec.displayName);
                                    tempNBT = ((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(), partSpec.getDefaultColourIndex(), ((ModelPartSpec) partSpec).getGlow());

                                    System.out.println("tempNBT: " + tempNBT.toString());

                                    prefArray.add(tempNBT);
                                } else if (partSpec instanceof TexturePartSpec) {
                                    System.out.println("partSpec: " + partSpec.displayName);

                                    tempNBT =((TexturePartSpec) partSpec).multiSet(new NBTTagCompound(), ((TexturePartSpec) partSpec).textureLocation, partSpec.getDefaultColourIndex());

                                    System.out.println("tempNBT: " + tempNBT.toString());

                                    prefArray.add(tempNBT);
                                }
                            }
                        }
                    }
                } else if (stack.getItem() instanceof ItemPowerFist) {
                    if (spec.getSpecType().equals(EnumSpecType.POWER_FIST)) {
                        // TODO: get parts
                        modelName = spec.getName();
                        System.out.println("ModelName: " + modelName);

                        for (PartSpec partSpec : spec.getPartSpecs()) {
                            if (partSpec instanceof ModelPartSpec)
                                prefArray.add(((ModelPartSpec) partSpec).multiSet(new NBTTagCompound(), partSpec.getDefaultColourIndex(), ((ModelPartSpec) partSpec).getGlow()));
                        }
                    }
                }
            }
        }

        System.out.println("preffArray: " + prefArray.toString());

        NBTTagCompound nbt = new NBTTagCompound();
        for (NBTTagCompound elem: prefArray) {
            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
        }

        System.out.println("nbt: " + nbt.toString());


        return nbt;







//        ItemPowerArmor item = (ItemPowerArmor) stack.getItem();
//        Colour normalcolour = item.getColorFromItemStack(stack);
//        Colour glowcolour = item.getGlowFromItemStack(stack);
//
//
//        switch (slot) {
//            case HEAD:
//                list.addAll(makePrefs("mps_helm", "helm_main;helm_tube_entry1;helm_tubes;helm_tube_entry2".split(";"), 0, false));
//                list.addAll(makePrefs("mps_helm", "visor".split(";"), 1, true));
//                break;
//
//            case CHEST:
//                list.addAll(makePrefs("mps_arms", "arms2;arms3".split(";"), 0, false));
//                list.addAll(makePrefs("mps_arms", "crystal_shoulder_2;crystal_shoulder_1".split(";"), 1, true));
//                list.addAll(makePrefs("mps_chest", "belt;chest_main;polySurface36;backpack;chest_padding".split(";"), 0, false));
//                list.addAll(makePrefs("mps_chest", "crystal_belt".split(";"), 1, true));
//                break;
//
//            case LEGS:
//                list.addAll(makePrefs("mps_pantaloons", "leg1;leg2".split(";"), 0, false));
//                break;
//
//            case FEET:
//                list.addAll(makePrefs("mps_boots", "boots1;boots2".split(";"), 0, false));
//                break;
//        }
//        NBTTagCompound nbt = new NBTTagCompound();
//        for (NBTTagCompound elem: list) {
//            nbt.setTag(elem.getString("model") + "." + elem.getString("part"), elem);
//        }
//        return nbt;
    }

//    public static List<NBTTagCompound> makePrefs(String modelname, String[] partnames, int colour, boolean glow) {
//        List<NBTTagCompound> prefArray = new ArrayList<>();
////        ModelSpec model = ModelRegistry.getInstance().get(modelname);
////        for (String name: partnames) {
////            prefArray.add(makePref(model.get(name), colour, glow));
////        }
//        return prefArray;
//    }
//
//
//    public static NBTTagCompound makePref(ModelPartSpec modelPartSpec, Integer colourindex, Boolean glow) {
//        return modelPartSpec.multiSet(new NBTTagCompound(), colourindex, glow);
//    }
//
//
//
//    public static NBTTagCompound makePref(PartSpec partSpec, Integer colourindex, Boolean glow) {
//
//
//
//        return partSpec.multiSet(new NBTTagCompound(), colourindex, glow);
//    }
}