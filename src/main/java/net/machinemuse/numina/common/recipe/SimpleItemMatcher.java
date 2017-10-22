package net.machinemuse.numina.common.recipe;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:48 PM, 11/4/13
 */
public class SimpleItemMatcher implements IItemMatcher {
    public Integer meta;
    public String oredictName;
    public String registryName;
    public String itemStackName;
    public String nbtString;

    public SimpleItemMatcher() {
    }

    @Override
    public boolean matchesItem(ItemStack stack) {
        if (stack == null) {
            return false;
        }
        if (stack.getItem() == null) {
            return false;
        }
        if (meta != null && meta != stack.getItemDamage()) {
            return false;
        }

        if (oredictName != null) {
            boolean found = false;
            for (ItemStack ore : OreDictionary.getOres(oredictName)) {
                if (ore.getItem() == stack.getItem() && ore.getItemDamage() == stack.getItemDamage()) {
                    found = true;
                }
            }
            if (!found) return false;
        }
        if (registryName != null) {
            String[] names = registryName.split(":");
            Item item = Item.REGISTRY.getObject(new ResourceLocation(names[0], names[1]));
            if (item == null) {
                MuseLogger.logError("Item " + registryName + " not found in registry for recipe.");
                return false;
            }
            if (item != stack.getItem()) return false;
        }
        if (itemStackName != null) {
//            String[] names = itemStackName.split(":");
            ItemStack compareStack = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(itemStackName)), 1);
            if (compareStack == null) {
                MuseLogger.logError("ItemStack " + itemStackName + " not found in registry for recipe.");
                return false;
            }
            if (stack.getItemDamage() != compareStack.getItemDamage()) return false;
            if (stack.getItem() != compareStack.getItem()) return false;
            if (!stack.getTagCompound().equals(compareStack.getTagCompound())) return false;
        }
        if (nbtString != null) {
            try {
                NBTTagCompound nbt = (NBTTagCompound) JsonToNBT.getTagFromJson(nbtString);
                if (!nbt.equals(stack.getTagCompound())) {
                    return false;
                }
            } catch (Exception e) {
                MuseLogger.logException("Bad NBT string in item!", e);
                return false;
            }
        }
        return true;
    }

    public SimpleItemMatcher copy() {
        SimpleItemMatcher copy = new SimpleItemMatcher();
        copy.meta = this.meta;
        copy.oredictName = this.oredictName;
        copy.nbtString = this.nbtString;
        return copy;
    }

    @Override
    public boolean equals(Object obj) {
        if(! (obj instanceof SimpleItemMatcher) ) return false;
        SimpleItemMatcher other = (SimpleItemMatcher)obj;
        if(!compareInteger(meta, other.meta)) return false;
        if(!compareString(oredictName, other.oredictName)) return false;
        if(!compareString(registryName, other.registryName)) return false;
        if(!compareString(itemStackName, other.itemStackName)) return false;
        return compareString(nbtString, other.nbtString);
    }

    private boolean compareInteger(Integer a, Integer b) {
        if(a == null && b == null) return true;
        if(a != null && b != null) {
            if(a.intValue() == b.intValue()) return true;
        }
        return false;
    }

    private boolean compareString(String a, String b) {
        if(a == null && b == null) return true;
        if(a != null && b != null) {
            if(a.equals(b)) return true;
        }
        return false;
    }
}