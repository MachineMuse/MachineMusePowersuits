//package net.machinemuse.numina.recipe;
//
//import net.machinemuse.numina.utils.MuseLogger;
//import net.minecraft.inventory.InventoryCrafting;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.JsonToNBTFixed;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.oredict.OreDictionary;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 2:55 PM, 11/4/13
// */
//public class SimpleItemMaker implements IItemMaker {
//    public Integer meta;
//    public Integer quantity;
//    public String oredictName;
//    public String registryName;
//    public String itemStackName;
//    public String nbtString;
//
//    @Override
//    public ItemStack makeItem(InventoryCrafting i) {
//        return getRecipeOutput();
//    }
//
//    private int getOrElse(Integer input, int defaultval) {
//        if (input == null)
//            return defaultval;
//        else
//            return input;
//    }
//
//    @Override
//    public ItemStack getRecipeOutput() {
//        int newmeta = getOrElse(this.meta, 0);
//        int newquantity = getOrElse(this.quantity, 1);
//        NBTTagCompound nbt = null;
//
//        if(nbtString != null) {
//            try {
//                nbt = JsonToNBTFixed.getTagFromJson(nbtString);
//            } catch (Exception e) {
//                MuseLogger.logException("Bad NBT string in item! Attempting to create generic item instead.", e);
//            }
//        }
//
//        if (itemStackName != null) {
//            MuseLogger.logDebug("ItemStack name is: " + itemStackName);
//            try {
//                ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(itemStackName)), newquantity);
//                if(this.meta != null) stack.setItemDamage(meta);
//                if(nbt != null) stack.setTagCompound(nbt);
//                return stack;
//            } catch (Exception e) {
//                MuseLogger.logError("Unable to load " + itemStackName + " from Item Registry");
//                return null;
//            }
//        } else if (registryName != null) {
//            String[] names = registryName.split(":");
//            MuseLogger.logDebug("registry name is: " + registryName);
//            try {
//                ItemStack stack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(names[0], names[1])), newquantity, newmeta);
//                if(nbt != null) stack.setTagCompound(nbt);
//                return stack;
//            } catch (Exception e) {
//                MuseLogger.logError("Unable to load " + registryName + " from Item Registry");
//                return null;
//            }
//        } else if (oredictName != null) {
//            MuseLogger.logDebug("oredict name is: " + oredictName);
//            try {
//                ItemStack stack = OreDictionary.getOres(oredictName).get(0).copy();
//                stack.setCount(Math.min(newquantity, stack.getMaxStackSize()));
//                if(nbt != null) stack.setTagCompound(nbt);
//                return stack;
//            } catch (Exception e) {
//                MuseLogger.logError("Unable to load " + oredictName + " from oredict");
//                return null;
//            }
//        } else {
//            MuseLogger.logError("Could not find a valid item for recipe output so returning NULL \n this will invalidate the recipe");
//            return null;
//        }
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if(! (obj instanceof SimpleItemMaker) ) return false;
//        SimpleItemMaker other = (SimpleItemMaker)obj;
//        if(!compareInteger(meta, other.meta)) return false;
//        if(!compareInteger(quantity, other.quantity)) return false;
//        if(!compareString(oredictName, other.oredictName)) return false;
//        if(!compareString(registryName, other.registryName)) return false;
//        if(!compareString(itemStackName, other.itemStackName)) return false;
//        if(!compareString(nbtString, other.nbtString)) return false;
//        return true;
//    }
//
//    private boolean compareInteger(Integer a, Integer b) {
//        if(a == null && b == null) return true;
//        if(a != null && b != null) {
//            if(a.intValue() == b.intValue()) return true;
//        }
//        return false;
//    }
//
//    private boolean compareString(String a, String b) {
//        if(a == null && b == null) return true;
//        if(a != null && b != null) {
//            if(a.equals(b)) return true;
//        }
//        return false;
//    }
//}