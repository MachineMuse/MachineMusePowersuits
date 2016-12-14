package net.machinemuse.utils;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseItemTag;
import net.machinemuse.numina.general.MuseLogger;
import net.machinemuse.numina.general.MuseMathUtils;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.*;

public class MuseItemUtils {
    public static final String ONLINE = "Active";

    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is null,
     *         returns null.
     */
    public static NBTTagCompound getMuseItemTag(ItemStack stack) {
        return MuseItemTag.getMuseItemTag(stack);
    }

    public static NBTTagCompound getMuseRenderTag(ItemStack stack, int armorSlot) {
        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey("render") || !(tag.getTag("render") instanceof NBTTagCompound)) {
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            tag.removeTag("render");
            tag.setTag("render", DefaultModelSpec.makeModelPrefs(stack, armorSlot));
        }
        return tag.getCompoundTag("render");
    }

    public static NBTTagCompound getMuseRenderTag(ItemStack stack) {
        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey("render") || !(tag.getTag("render") instanceof NBTTagCompound)) {
            tag.removeTag("render");
            tag.setTag("render", new NBTTagCompound());
        }
        return tag.getCompoundTag("render");
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of ItemStacks in the inventory which implement
     *         IModularItem
     */
    public static List<ItemStack> getModularItemsInInventory(IInventory inv) {
        ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof IModularItem) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public static List<ItemStack> getModularItemsInInventory(EntityPlayer player) {
        return getModularItemsInInventory(player.inventory);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of inventory slots containing an IModularItem
     */
    public static List<Integer> getModularItemSlotsInInventory(IInventory inv) {
        ArrayList<Integer> slots = new ArrayList<Integer>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack != null && stack.getItem() instanceof IModularItem) {
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
                    found += stackInInventory.stackSize;
                }
            }
            if (found < stackInCost.stackSize) {
                return false;
            }
        }
        return true;
    }

    public static List<Integer> deleteFromInventory(List<ItemStack> cost, InventoryPlayer inventory) {
        List<Integer> slots = new LinkedList<Integer>();
        for (ItemStack stackInCost : cost) {
            int remaining = stackInCost.stackSize;
            for (int i = 0; i < inventory.getSizeInventory() && remaining > 0; i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    int numToTake = Math.min(stackInInventory.stackSize, remaining);
                    stackInInventory.stackSize -= numToTake;
                    remaining -= numToTake;
                    if (stackInInventory.stackSize == 0) {
                        inventory.setInventorySlotContents(i, null);
                    }
                    slots.add(i);
                }
            }
        }
        return slots;
    }

    public static List<Integer> findInInventoryForCost(List<ItemStack> workingUpgradeCost, InventoryPlayer inventory) {
        List<Integer> slots = new LinkedList<Integer>();
        for (ItemStack stackInCost : workingUpgradeCost) {
            int found = 0;
            for (int i = 0; i < inventory.getSizeInventory() && found < stackInCost.stackSize; i++) {
                ItemStack stackInInventory = inventory.getStackInSlot(i);
                if (isSameItem(stackInInventory, stackInCost)) {
                    found += stackInInventory.stackSize;
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
    public static void setDoubleOrRemove(ItemStack stack, String string, double value) {
        setDoubleOrRemove(getMuseItemTag(stack), string, value);
    }

    public static String getStringOrNull(NBTTagCompound itemProperties, String key) {
        String value = null;
        if (itemProperties != null) {
            if (itemProperties.hasKey(key)) {
                value = itemProperties.getString(key);
            }
        }
        return value;
    }

    public static String getStringOrNull(ItemStack stack, String key) {
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

    public static void setStringOrNull(ItemStack stack, String key, String value) {
        setStringOrNull(getMuseItemTag(stack), key, value);
    }

    public static List<ItemStack> modularItemsEquipped(EntityPlayer player) {
        List<ItemStack> modulars = new ArrayList(5);
        ItemStack[] equipped = itemsEquipped(player);
        for (ItemStack stack : equipped) {
            if (stack != null && stack.getItem() instanceof IModularItem) {
                modulars.add(stack);
            }
        }
        return modulars;
    }

    public static ItemStack[] itemsEquipped(EntityPlayer player) {
        return new ItemStack[]{
                player.inventory.armorInventory[0],
                player.inventory.armorInventory[1],
                player.inventory.armorInventory[2],
                player.inventory.armorInventory[3],
                player.inventory.getCurrentItem()};
    }

    public static boolean canStackTogether(ItemStack stack1, ItemStack stack2) {
        if (!isSameItem(stack1, stack2)) {
            return false;
        } else if (!stack1.isStackable()) {
            return false;
        } else
            return stack1.stackSize < stack1.getMaxStackSize();
    }

    public static boolean isSameItem(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return false;
        } else if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else
            return !((!stack1.isItemStackDamageable())
                    && (stack1.getItemDamage() != stack2.getItemDamage()));
    }

    public static void transferStackWithChance(ItemStack itemsToGive, ItemStack destinationStack, double chanceOfSuccess) {
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.lvcapacitor)) {
            itemsToGive.stackSize = 0;
            return;
        }
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.mvcapacitor)) {
            itemsToGive.stackSize = 0;
            return;
        }
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.hvcapacitor)) {
            itemsToGive.stackSize = 0;
            return;
        }

        int maxSize = destinationStack.getMaxStackSize();
        while (itemsToGive.stackSize > 0 && destinationStack.stackSize < maxSize) {
            itemsToGive.stackSize -= 1;
            if (MuseMathUtils.nextDouble() < chanceOfSuccess) {
                destinationStack.stackSize += 1;
            }
        }
    }

    public static Set<Integer> giveOrDropItems(ItemStack itemsToGive, EntityPlayer player) {
        return giveOrDropItemWithChance(itemsToGive, player, 1.0);
    }

    public static Set<Integer> giveOrDropItemWithChance(ItemStack itemsToGive, EntityPlayer player, double chanceOfSuccess) {
        Set<Integer> slots = new HashSet<Integer>();

        // First try to add the items to existing stacks
        for (int i = 0; i < player.inventory.getSizeInventory() && itemsToGive.stackSize > 0; i++) {
            ItemStack currentStack = player.inventory.getStackInSlot(i);
            if (canStackTogether(currentStack, itemsToGive)) {
                slots.add(i);
                transferStackWithChance(itemsToGive, currentStack, chanceOfSuccess);
            }
        }
        // Then try to add the items to empty slots
        for (int i = 0; i < player.inventory.getSizeInventory() && itemsToGive.stackSize > 0; i++) {
            if (player.inventory.getStackInSlot(i) == null) {
                ItemStack destination = new ItemStack(itemsToGive.getItem(), 0, itemsToGive.getItemDamage());
                transferStackWithChance(itemsToGive, destination, chanceOfSuccess);
                if (destination.stackSize > 0) {
                    player.inventory.setInventorySlotContents(i, destination);
                    slots.add(i);
                }
            }
        }
        // Finally spawn the items in the world.
        if (itemsToGive.stackSize > 0) {
            for (int i = 0; i < itemsToGive.stackSize; i++) {
                if (MuseMathUtils.nextDouble() < chanceOfSuccess) {
                    ItemStack copyStack = itemsToGive.copy();
                    copyStack.stackSize = 1;
                    player.dropPlayerItemWithRandomChoice(copyStack, false);
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
            for (IPowerModule module : ModuleManager.getValidModulesForItem(player, stack)) {
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
    public static ItemStack copyAndResize(ItemStack stack, int number) {
        ItemStack copy = stack.copy();
        copy.stackSize = number;
        return copy;
    }

    public static boolean canItemFitInInventory(EntityPlayer player, ItemStack itemstack) {
        for (int i = 0; i < player.inventory.getSizeInventory() - 4; i++) {
            if (player.inventory.getStackInSlot(i) == null) {
                return true;
            }
        }
        if (!itemstack.isItemDamaged()) {
            if (itemstack.getMaxStackSize() == 1) {
                return false;
            }
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack invstack = player.inventory.getStackInSlot(i);
                if (invstack != null && invstack.getItem() == itemstack.getItem() && invstack.isStackable() && invstack.stackSize < invstack.getMaxStackSize() && invstack.stackSize < player.inventory.getInventoryStackLimit() && (!invstack.getHasSubtypes() || invstack.getItemDamage() == itemstack.getItemDamage())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static double getFoodLevel(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Double foodLevel = itemTag.getDouble("Food");
            if (foodLevel != null) {
                return foodLevel;
            }
        }
        return 0.0;
    }

    public static void setFoodLevel(ItemStack stack, double d) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Food", d);
        }
    }

    public static double getSaturationLevel(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Double saturationLevel = itemTag.getDouble("Saturation");
            if (saturationLevel != null) {
                return saturationLevel;
            }
        }
        return 0.0F;
    }

    public static void setSaturationLevel(ItemStack stack, double d) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Saturation", d);
        }
    }

    public static int getTorchLevel(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Integer torchLevel = itemTag.getInteger("Torch");
            if (torchLevel != null) {
                return torchLevel;
            }
        }
        return 0;
    }

    public static void setTorchLevel(ItemStack stack, int i) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setInteger("Torch", i);
        }
    }

    public static double getWaterLevel(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Double waterLevel = itemTag.getDouble("Water");
            if (waterLevel != null) {
                return waterLevel;
            }
        }
        return 0;
    }

    public static void setWaterLevel(ItemStack stack, double d) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setDouble("Water", d);
        }
    }

    public static void setLiquid(ItemStack stack, String name) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setString("Liquid", name);
        }
    }

    public static String getLiquid(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            String s = itemTag.getString("Liquid");
            if (s != null) {
                return s;
            }
        }
        return "";
    }

    public static int getCoalLevel(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            Integer coalLevel = itemTag.getInteger("Coal");
            if (coalLevel != null) {
                return coalLevel;
            }
        }
        return 0;
    }
    public static void setCoalLevel(ItemStack stack, int i) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setInteger("Coal", i);
        }
    }

    public static String getEIONoCompete(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getString("eioNoCompete");
            } else {
                return "";
            }
        }
        return "";
    }

    public static void setEIONoCompete(ItemStack stack, String s) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setString("eioNoCompete", s);
        }
    }

    public static boolean getEIOFacadeTransparency(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            if (itemTag != null) {
                return itemTag.getBoolean("eioFacadeTransparency");
            }
        }
        return false;
    }

    public static void setEIOFacadeTransparency(ItemStack stack, boolean b) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            itemTag.setBoolean("eioFacadeTransparency", b);
        }
    }

    public static NBTTagCompound getFluidTermTag(ItemStack stack) {
        NBTTagCompound ret = null;
        if (stack != null && stack.getItem() instanceof IModularItem) {
            ret = MuseItemUtils.getMuseItemTag(stack).getCompoundTag("AppEng EC Wireless Fluid Terminal");
        }
        return ret;
    }

    public static void setFluidTermTag(ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound t = MuseItemUtils.getMuseItemTag(stack);
        t.setTag("AppEng EC Wireless Fluid Terminal", tag);
        stack.stackTagCompound.setTag(MuseItemTag.NBTPREFIX, t);
    }

    public static boolean getCanShrink(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = stack.getTagCompound();
            NBTTagCompound cmTag = ((itemTag.hasKey("CompactMachines")) ? itemTag.getCompoundTag("CompactMachines") : null);
            if (cmTag != null && cmTag.hasKey("canShrink")) {
                return cmTag.getBoolean("canShrink");
            }
        }
        return false;
    }

    public static void setCanShrink(ItemStack stack, boolean b) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = stack.stackTagCompound;
            NBTTagCompound cmTag = ((itemTag.hasKey("CompactMachines")) ? itemTag.getCompoundTag("CompactMachines") : (new NBTTagCompound()));
            cmTag.setBoolean("canShrink", b);
            itemTag.setTag("CompactMachines", cmTag);
        }
    }

    public static NBTTagCompound getNBTTag(ItemStack itemStack) {
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
