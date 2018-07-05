package net.machinemuse.utils;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemTag;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.*;

public class MuseItemUtils {
    public static final String TAG_ONLINE = "Active";
    public static final String TAG_RENDER = "render";

    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is null,
     *         returns null.
     */
    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
        return MuseItemTag.getMuseItemTag(stack);
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey(TAG_RENDER) || !(tag.getTag(TAG_RENDER) instanceof NBTTagCompound)) {
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            tag.removeTag(TAG_RENDER);
            tag.setTag(TAG_RENDER, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
        }
        return tag.getCompoundTag(TAG_RENDER);
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey(TAG_RENDER) || !(tag.getTag(TAG_RENDER) instanceof NBTTagCompound)) {
            tag.removeTag(TAG_RENDER);
            tag.setTag(TAG_RENDER, new NBTTagCompound());
        }
        return tag.getCompoundTag(TAG_RENDER);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of ItemStacks in the inventory which implement
     *         IModularItem
     */
    public static NonNullList<ItemStack> getModularItemsInInventory(IInventory inv) {
        NonNullList<ItemStack> stacks = NonNullList.create();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public static NonNullList<ItemStack> getModularItemsInInventory(EntityPlayer player) {
        return getModularItemsInInventory(player.inventory);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of inventory slots containing an IModularItem
     */
    public static List<Integer> getModularItemSlotsInInventory(IInventory inv) {
        ArrayList<Integer> slots = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                slots.add(i);
            }
        }
        return slots;
    }

    /**
     * Attempts to cast an item to IModularItem, returns null if fails
     */
    public static IModularItem getAsModular(Item item) {
        if (item instanceof IModularItem) {
            return (IModularItem) item;
        } else {
            return null;
        }
    }

    /**
     * Checks if the player has a copy of all of the items in
     * workingUpgradeCost.
     *
     * @param workingUpgradeCost
     * @param inventory
     * @return
     */
    public static boolean hasInInventory(List<ItemStack> workingUpgradeCost, InventoryPlayer inventory) {
        for (ItemStack stackInCost : workingUpgradeCost) {
            int found = 0;
            for (int i = 0; i < inventory.getSizeInventory(); i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    found += stackInInventory.getCount();
                }
            }
            if (found < stackInCost.getCount()) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> deleteFromInventory(NonNullList<ItemStack> cost, InventoryPlayer inventory) {
        List<Integer> slots = new LinkedList<>();
        for (ItemStack stackInCost : cost) {
            int remaining = stackInCost.getCount();
            for (int i = 0; i < inventory.getSizeInventory() && remaining > 0; i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    int numToTake = Math.min(stackInInventory.getCount(), remaining);
                    stackInInventory.setCount(stackInInventory.getCount() - numToTake);
                    remaining -= numToTake;
                    if (stackInInventory.getCount() == 0) {
                        inventory.setInventorySlotContents(i, ItemStack.EMPTY);
                    }
                    slots.add(i);
                }
            }
        }
        return slots;
    }

    public static List<Integer> findInInventoryForCost(List<ItemStack> workingUpgradeCost, InventoryPlayer inventory) {
        List<Integer> slots = new LinkedList<>();
        for (ItemStack stackInCost : workingUpgradeCost) {
            int found = 0;
            for (int i = 0; i < inventory.getSizeInventory() && found < stackInCost.getCount(); i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    found += stackInInventory.getCount();
                    slots.add(i);
                }
            }
        }
        return slots;
    }

    /**
     * Checks the given NBTTag and returns the value if it exists, otherwise 0.
     */
    public static double getDoubleOrZero(NBTTagCompound itemProperties, String string) {
        double value = 0;
        if (itemProperties != null) {
            if (itemProperties.hasKey(string)) {
                value = itemProperties.getDouble(string);
            }
        }
        return value;
    }

    /**
     * Bouncer for succinctness. Checks the item's modular properties and
     * returns the value if it exists, otherwise 0.
     */
    public static double getDoubleOrZero(ItemStack stack, String string) {
        return getDoubleOrZero(getMuseItemTag(stack), string);
    }

    /**
     * Sets the value of the given nbt tag, or removes it if the value would be
     * zero.
     */
    public static void setDoubleOrRemove(NBTTagCompound itemProperties, String string, double value) {
        if (itemProperties != null) {
            if (value == 0) {
                itemProperties.removeTag(string);
            } else {
                itemProperties.setDouble(string, value);
            }
        }
    }

    /**
     * Sets the given itemstack's modular property, or removes it if the value
     * would be zero.
     */
    public static void setDoubleOrRemove(@Nonnull ItemStack stack, String string, double value) {
        setDoubleOrRemove(getMuseItemTag(stack), string, value);
    }

    public static String getStringOrNull(@Nonnull NBTTagCompound itemProperties, String key) {
        String value = null;
        if (itemProperties != null) {
            if (itemProperties.hasKey(key)) {
                value = itemProperties.getString(key);
            }
        }
        return value;
    }

    public static String getStringOrNull(@Nonnull ItemStack stack, String key) {
        return getStringOrNull(getMuseItemTag(stack), key);
    }

    public static void setStringOrNull(NBTTagCompound itemProperties, String key, String value) {
        if (itemProperties != null) {
            if (value.isEmpty()) {
                itemProperties.removeTag(key);
            } else {
                itemProperties.setString(key, value);
            }
        }
    }

    public static void setStringOrNull(@Nonnull ItemStack stack, String key, String value) {
        setStringOrNull(getMuseItemTag(stack), key, value);
    }

    public static List<ItemStack> modularItemsEquipped(EntityPlayer player) {
        List<ItemStack> modulars = new ArrayList(5);
        ItemStack[] equipped = itemsEquipped(player);
        for (ItemStack stack : equipped) {
            if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
                modulars.add(stack);
            }
        }
        return modulars;
    }

    public static ItemStack[] itemsEquipped(EntityPlayer player) {
        return new ItemStack[]{
                player.getItemStackFromSlot(EntityEquipmentSlot.HEAD),
                player.getItemStackFromSlot(EntityEquipmentSlot.CHEST),
                player.getItemStackFromSlot(EntityEquipmentSlot.LEGS),
                player.getItemStackFromSlot(EntityEquipmentSlot.FEET),
                player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)};
//                player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND) // todo ?
    }

    public static boolean canStackTogether(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        if (!isSameItem(stack1, stack2)) {
            return false;
        } else if (!stack1.isStackable()) {
            return false;
        } else
            return stack1.getCount() < stack1.getMaxStackSize();
    }

    public static boolean isSameItem(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        if (stack1.isEmpty() || stack2.isEmpty()) {
            return false;
        } else if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else
            return !((!stack1.isItemStackDamageable())
                    && (stack1.getItemDamage() != stack2.getItemDamage()));
    }

    public static void transferStackWithChance(@Nonnull ItemStack itemsToGive, @Nonnull ItemStack destinationStack, double chanceOfSuccess) {
        if (isSameItem(itemsToGive, ItemComponent.lvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }
        if (isSameItem(itemsToGive, ItemComponent.mvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }
        if (isSameItem(itemsToGive, ItemComponent.hvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }
        if (isSameItem(itemsToGive, ItemComponent.evcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }

        int maxSize = destinationStack.getMaxStackSize();
        while (itemsToGive.getCount() > 0 && destinationStack.getCount()< maxSize) {
            itemsToGive.setCount(itemsToGive.getCount() - 1);
            if (MuseMathUtils.nextDouble() < chanceOfSuccess) {
                destinationStack.setCount(destinationStack.getCount() +1);
            }
        }
    }

    public static Set<Integer> giveOrDropItems(@Nonnull ItemStack itemsToGive, EntityPlayer player) {
        return giveOrDropItemWithChance(itemsToGive, player, 1.0);
    }

    public static Set<Integer> giveOrDropItemWithChance(@Nonnull ItemStack itemsToGive, EntityPlayer player, double chanceOfSuccess) {
        Set<Integer> slots = new HashSet<>();

        // First try to add the items to existing stacks
        for (int i = 0; i < player.inventory.getSizeInventory() && itemsToGive.getCount() > 0; i++) {
            ItemStack currentStack = player.inventory.getStackInSlot(i);
            if (canStackTogether(currentStack, itemsToGive)) {
                slots.add(i);
                transferStackWithChance(itemsToGive, currentStack, chanceOfSuccess);
            }
        }
        // Then try to add the items to empty slots
        for (int i = 0; i < player.inventory.getSizeInventory() && itemsToGive.getCount() > 0; i++) {
            if (player.inventory.getStackInSlot(i) == null) {
                ItemStack destination = new ItemStack(itemsToGive.getItem(), 0, itemsToGive.getItemDamage());
                transferStackWithChance(itemsToGive, destination, chanceOfSuccess);
                if (destination.getCount() > 0) {
                    player.inventory.setInventorySlotContents(i, destination);
                    slots.add(i);
                }
            }
        }
        // Finally spawn the items in the world.
        if (itemsToGive.getCount()> 0) {
            for (int i = 0; i < itemsToGive.getCount(); i++) {
                if (MuseMathUtils.nextDouble() < chanceOfSuccess) {
                    ItemStack copyStack = itemsToGive.copy();
                    copyStack.setCount(1);
                    player.dropItem(copyStack, false);
                }
            }
        }

        return slots;
    }
    public static double getPlayerWeight(EntityPlayer player) {
        double weight = 0;
        for (ItemStack stack : modularItemsEquipped(player)) {
            weight += ModuleManager.computeModularProperty(stack, MuseCommonStrings.WEIGHT);
        }
        return weight;
    }

    public static List<IPowerModule> getPlayerInstalledModules(EntityPlayer player) {
        List<IPowerModule> installedModules = new ArrayList();
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            for (IPowerModule module : ModuleManager.getValidModulesForItem(stack)) {
                if (ModuleManager.tagHasModule(itemTag, module.getDataName())) {
                    installedModules.add(module);
                }
            }
        }
        return installedModules;
    }

    public static void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval) {
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            ModuleManager.toggleModule(itemTag, name, toggleval);
        }
    }

    /**
     * Helper function for making recipes. Returns a copy of the itemstack with
     * the specified stacksize.
     *
     * @param stack  Itemstack to copy
     * @param number New Stacksize
     * @return A new itemstack with the specified properties
     */
    public static ItemStack copyAndResize(@Nonnull ItemStack stack, int number) {
        ItemStack copy = stack.copy();
        copy.setCount(number);
        return copy;
    }

    /**
     * Checks if an ItemStack can fit in a player's inventory
     *
     * @param player
     * @param itemstack
     * @return
     */
    public static boolean canItemFitInInventory(EntityPlayer player, @Nonnull ItemStack itemstack) {
        for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
            if (player.inventory.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        if (!itemstack.isItemDamaged()) {
            if (itemstack.getMaxStackSize() == 1) {
                return false;
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invstack = player.inventory.getStackInSlot(i);
                if (invstack != null && invstack.getItem() == itemstack.getItem() && invstack.isStackable() && invstack.getCount() < invstack.getMaxStackSize() && invstack.getCount() < player.inventory.getInventoryStackLimit() && (!invstack.getHasSubtypes() || invstack.getItemDamage() == itemstack.getItemDamage())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }


//    public static NBTTagCompound getFluidTermTag(@Nonnull ItemStack stack) {
//        NBTTagCompound ret = null;
//        if (!stack.isEmpty() && stack.getItem() instanceof IModularItem) {
//            ret = MuseItemUtils.getMuseItemTag(stack).getCompoundTag("AppEng EC Wireless Fluid Terminal");
//        }
//        return ret;
//    }
//
//    public static void setFluidTermTag(@Nonnull ItemStack stack, NBTTagCompound tag) {
//        NBTTagCompound t = MuseItemUtils.getMuseItemTag(stack);
//        t.setTag("AppEng EC Wireless Fluid Terminal", tag);
//        stack.getTagCompound().setTag(MuseItemTag.NBTPREFIX, t);
//    }



    public static NBTTagCompound getNBTTag(@Nonnull ItemStack itemStack) {
        NBTTagCompound tag = itemStack.getTagCompound();

        if (tag == null) {
            tag = new NBTTagCompound();
            itemStack.setTagCompound(tag);
        }

        return tag;
    }








    public static boolean isClientWorld(World world) {
        return world.isRemote;
    }

    public static boolean isServerWorld(World world) {
        return !world.isRemote;
    }

    public static boolean isClientSide() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
    }

    public static boolean isServerSide() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER;
    }
}
