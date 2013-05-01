package net.machinemuse.utils;

import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.network.MusePacket;
import net.machinemuse.powersuits.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.powersuits.tick.RenderTickHandler;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.*;

public class MuseItemUtils {
    public static final String NBTPREFIX = "mmmpsmod";
    public static final String ONLINE = "Active";

    public static List<IPowerModule> getValidModulesForItem(EntityPlayer player, ItemStack stack) {
        List<IPowerModule> validModules = new ArrayList();
        for (IPowerModule module : ModuleManager.getAllModules()) {
            if (module.isValidForItem(stack, player)) {
                validModules.add(module);
            }
        }
        return validModules;
    }

    private static int clampMode(int selection, int modesSize) {
        if (selection > 0) {
            return selection % modesSize;
        } else {
            return (selection + modesSize * (-selection)) % modesSize;
        }
    }

    @SideOnly(Side.CLIENT)
    public static void cycleMode(ItemStack stack, EntityClientPlayerMP player, int dMode) {
        if (stack != null && stack.getItem() instanceof IModularItem) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            String mode = itemTag.getString("Mode");
            List<String> modes = MuseItemUtils.getModes(stack, player);
            if (mode.isEmpty() && modes.size() > 0) {
                mode = modes.get(0);
            }
            if (modes.size() > 0 && dMode != 0) {
                int modeIndex = modes.indexOf(mode);
                String newMode = modes.get(clampMode(modeIndex + dMode, modes.size()));
                itemTag.setString("Mode", newMode);
                RenderTickHandler.lastSwapTime = System.currentTimeMillis();
                RenderTickHandler.lastSwapDirection = (int) Math.signum(dMode);
                MusePacket modeChangePacket = new MusePacketModeChangeRequest((Player) player, newMode, player.inventory.currentItem);
                player.sendQueue.addToSendQueue(modeChangePacket.getPacket250());
            }
        }
    }

    public static boolean tagHasModule(NBTTagCompound tag, String moduleName) {
        return tag.hasKey(moduleName);
    }

    public static List<String> getValidModes(ItemStack stack, EntityPlayer player) {
        List<String> modes = new ArrayList();
        if (stack.getItem() instanceof IModularItem) {
            for (IPowerModule module : ModuleManager.getAllModules()) {
                if (module.isValidForItem(stack, player) && module instanceof IRightClickModule) {
                    modes.add(module.getName());
                }
            }
        }
        return modes;
    }

    public static List<String> getModes(ItemStack stack, EntityPlayer player) {
        List<String> modes = new ArrayList();
        if (stack.getItem() instanceof IModularItem) {
            for (IPowerModule module : ModuleManager.getAllModules()) {
                if (module.isValidForItem(stack, player) && module instanceof IRightClickModule && itemHasModule(stack, module.getName())) {
                    modes.add(module.getName());
                }
            }
        }
        return modes;
    }

    public static boolean isModuleOnline(NBTTagCompound itemTag, String moduleName) {
        if (MuseItemUtils.tagHasModule(itemTag, moduleName) && !itemTag.getCompoundTag(moduleName).hasKey(ONLINE)) {
            return true;
        } else if (MuseItemUtils.tagHasModule(itemTag, moduleName) && itemTag.getCompoundTag(moduleName).getBoolean(ONLINE)) {
            return true;
        }
        return false;
    }

    public static void toggleModule(NBTTagCompound itemTag, String name, boolean toggleval) {
        if (MuseItemUtils.tagHasModule(itemTag, name)) {
            NBTTagCompound moduleTag = itemTag.getCompoundTag(name);
            moduleTag.setBoolean(ONLINE, toggleval);
        }
    }

    public static boolean itemHasModule(ItemStack stack, String moduleName) {
        return tagHasModule(getMuseItemTag(stack), moduleName);
    }

    public static void tagAddModule(NBTTagCompound tag, IPowerModule module) {
        tag.setCompoundTag(module.getName(), module.getNewTag());
    }

    public static void itemAddModule(ItemStack stack, IPowerModule moduleType) {
        tagAddModule(getMuseItemTag(stack), moduleType);
    }

    public static boolean removeModule(NBTTagCompound tag, String moduleName) {
        if (tag.hasKey(moduleName)) {
            tag.removeTag(moduleName);
            return true;
        } else {
            return false;
        }
    }

    public static boolean removeModule(ItemStack stack, String moduleName) {
        return removeModule(getMuseItemTag(stack), moduleName);
    }

    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is null,
     *         returns null.
     */
    public static NBTTagCompound getMuseItemTag(ItemStack stack) {
        if (stack == null) {
            return null;
        }
        NBTTagCompound stackTag;
        if (stack.hasTagCompound()) {
            stackTag = stack.getTagCompound();
        } else {
            stackTag = new NBTTagCompound();
            stack.setTagCompound(stackTag);
        }

        NBTTagCompound properties;
        if (stackTag.hasKey(NBTPREFIX)) {
            properties = stackTag.getCompoundTag(NBTPREFIX);
        } else {
            properties = new NBTTagCompound();
            stackTag.setCompoundTag(NBTPREFIX, properties);
        }
        return properties;
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
        ItemStack[] equipped = {
                player.inventory.armorInventory[0],
                player.inventory.armorInventory[1],
                player.inventory.armorInventory[2],
                player.inventory.armorInventory[3],
                player.inventory.getCurrentItem()};
        return equipped;
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
        } else if (stack1.itemID != stack2.itemID) {
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
                ItemStack destination = new ItemStack(itemsToGive.itemID, 0, itemsToGive.getItemDamage());
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
                    player.dropPlayerItem(copyStack);
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
            for (IPowerModule module : MuseItemUtils.getValidModulesForItem(player, stack)) {
                if (tagHasModule(itemTag, module.getName())) {
                    installedModules.add(module);
                }
            }
        }
        return installedModules;
    }

    public static void toggleModuleForPlayer(EntityPlayer player, String name, boolean toggleval) {
        for (ItemStack stack : MuseItemUtils.modularItemsEquipped(player)) {
            NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
            toggleModule(itemTag, name, toggleval);
        }

    }

    public static boolean itemHasActiveModule(ItemStack itemStack, String moduleName) {
        IPowerModule module = ModuleManager.getModule(moduleName);
        if (module == null || itemStack == null || !module.isAllowed() || !(itemStack.getItem() instanceof IModularItem)) {
            // playerEntity.sendChatToPlayer("Server has disallowed this module. Sorry!");
            return false;
        }
        if (module instanceof IRightClickModule) {
            // MuseLogger.logDebug("Module: " + moduleName + " vs Mode: " +
            // MuseItemUtils.getActiveMode(itemStack));

            return moduleName.equals(MuseItemUtils.getActiveMode(itemStack));
        } else {
            return isModuleOnline(getMuseItemTag(itemStack), moduleName);
        }
    }

    public static String getActiveMode(ItemStack itemStack) {
        return getMuseItemTag(itemStack).getString("Mode");
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

}
