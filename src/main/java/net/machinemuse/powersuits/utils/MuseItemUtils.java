package net.machinemuse.powersuits.utils;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.energy.adapater.ElectricAdapter;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.IModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.machinemuse.powersuits.api.constants.MPSNBTConstants;
import net.machinemuse.powersuits.client.render.modelspec.DefaultModelSpec;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmor;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nonnull;
import java.util.*;

public class MuseItemUtils {
    /**
     * Gets or creates stack.getTagCompound().getTag(NBTPREFIX)
     *
     * @param stack
     * @return an NBTTagCompound, may be newly created. If stack is empty,
     *         returns empty tag.
     */
    public static NBTTagCompound getMuseItemTag(@Nonnull ItemStack stack) {
        if (stack.isEmpty()) {
            return new NBTTagCompound();
        }

        NBTTagCompound stackTag;
        if (stack.hasTagCompound())
            stackTag = stack.getTagCompound();
        else {
            stackTag = new NBTTagCompound();
            stack.setTagCompound(stackTag);
        }

        NBTTagCompound properties;
        if (stackTag.hasKey(NuminaNBTConstants.NBTPREFIX))
            properties = stackTag.getCompoundTag(NuminaNBTConstants.NBTPREFIX);
        else {
            properties = new NBTTagCompound();
            stackTag.setTag(NuminaNBTConstants.NBTPREFIX, properties);
        }
        return properties;
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack, EntityEquipmentSlot armorSlot) {
        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey(MPSNBTConstants.NBT_RENDER_TAG, Constants.NBT.TAG_COMPOUND)) {
            MuseLogger.logDebug("TAG BREACH IMMINENT, PLEASE HOLD ONTO YOUR SEATBELTS");
            tag.removeTag(MPSNBTConstants.NBT_RENDER_TAG);
            tag.setTag(MPSNBTConstants.NBT_RENDER_TAG, DefaultModelSpec.makeModelPrefs(stack, armorSlot));
        }
        return tag.getCompoundTag(MPSNBTConstants.NBT_RENDER_TAG);
    }

    public static NBTTagCompound getMuseRenderTag(@Nonnull ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerArmor)
            return getMuseRenderTag(stack, ((ItemPowerArmor) stack.getItem()).armorType);

        NBTTagCompound tag = getMuseItemTag(stack);
        if (!tag.hasKey(MPSNBTConstants.NBT_RENDER_TAG, Constants.NBT.TAG_COMPOUND)) {
            tag.removeTag(MPSNBTConstants.NBT_RENDER_TAG);
            tag.setTag(MPSNBTConstants.NBT_RENDER_TAG, new NBTTagCompound());
        }
        return tag.getCompoundTag(MPSNBTConstants.NBT_RENDER_TAG);
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of ItemStacks in the inventory which implement
     *         IMuseItem
     */
    public static List<ItemStack> getModularItemsInInventory(IInventory inv) {
        ArrayList<ItemStack> stacks = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    public static List<ItemStack> getModularItemsInInventory(EntityPlayer player) {
        return getModularItemsInInventory(player.inventory);
    }

    public static List<ItemStack> getModularItemsEquipped(EntityPlayer player) {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for(EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
            ItemStack stack = player.getItemStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
                stacks.add(stack);
            }
        }
        return stacks;
    }

    /**
     * Scans a specified inventory for modular items.
     *
     * @param inv IInventory to scan.
     * @return A List of inventory slots containing an IMuseItem
     */
    public static List<Integer> getModularItemSlotsInInventory(IInventory inv) {
        ArrayList<Integer> slots = new ArrayList<>();

        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
                slots.add(i);
            }
        }
        return slots;
    }

    /**
     * Scans a specified player's inventory for equipped modular item.
     *
     * @param player IInventory to scan.
     * @return A List of inventory slots containing an IModularItemBase
     */
    public static List<Integer> getEquiptedModularItemSlotsInInventory(EntityPlayer player) {
        ArrayList<Integer> slots = new ArrayList<>();
        for (EntityEquipmentSlot c : EntityEquipmentSlot.values()) {
            ItemStack stack = player.getItemStackFromSlot(c);
            if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
                if ((c.getSlotType() == EntityEquipmentSlot.Type.ARMOR && stack.getItem() instanceof ItemPowerArmor) ||
                        (c.getSlotType() == EntityEquipmentSlot.Type.HAND && stack.getItem() instanceof ItemPowerFist)
                    // TODO: shield?
                        ) {
                    int i = getSlotFor(player, stack);
                    if (!slots.contains(i) && i != -1)
                        slots.add(i);
                }
            }
        }
        return slots;
    }

    public static int getSlotFor(EntityPlayer player, ItemStack stack) {
        for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
            if (!player.inventory.getStackInSlot(i).isEmpty() && stackEqualExact(stack, player.inventory.getStackInSlot(i))) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Checks item, NBT, and meta if the item is not damageable
     */
    public static boolean stackEqualExact(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2);
    }

    /**
     * Attempts to cast an item to IMuseItem, returns null if fails
     */
    public static IMuseItem getAsModular(Item item) {
        if (item instanceof IMuseItem) {
            return (IMuseItem) item;
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

    public static List<Integer> deleteFromInventory(List<ItemStack> cost, InventoryPlayer inventory) {
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

    public static double getOrSetModuleProperty(NBTTagCompound moduleTag, String propertyName, double defaultValue) {
        if (!moduleTag.hasKey(propertyName)) {
            moduleTag.setDouble(propertyName, defaultValue);
        }
        return moduleTag.getDouble(propertyName);
    }


    public static List<String> getItemInstalledModules(EntityPlayer player, ItemStack stack) {
        NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
        List<String> modules = new LinkedList();
        for (IModule module : ModuleManager.getInstance().getValidModulesForItem(stack)) {
            if (ModuleManager.getInstance().tagHasModule(itemTag, module.getUnlocalizedName())) {
                modules.add(module.getUnlocalizedName());
            }
        }
        return modules;
    }



    /**
     * Adds information to the item's tooltip when 'getting' it.
     *
     * @param stack            The itemstack to get the tooltip for
     * @param player           The player (client) viewing the tooltip
     * @param currentTipList   A list of strings containing the existing tooltip. When
     *                         passed, it will just contain the name of the item;
     *                         enchantments and lore are
     *                         appended afterwards.
     * @param advancedToolTips Whether or not the player has 'advanced tooltips' turned on in
     *                         their settings.
     */
    public static void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        if (stack.getItem() instanceof ItemPowerFist) {
            String mode = MuseItemUtils.getStringOrNull(stack, "Mode");
            if (mode != null) {
                currentTipList.add(I18n.format("tooltip.mode") + " " + MuseStringUtils.wrapFormatTags(mode, MuseStringUtils.FormatCodes.Red));
            } else {
                currentTipList.add(I18n.format("tooltip.changeModes"));
            }
        }
        ElectricAdapter adapter = ElectricAdapter.wrap(stack);
        if (adapter != null) {
            String energyinfo = I18n.format("tooltip.energy") + " " + MuseStringUtils.formatNumberShort(adapter.getEnergyStored()) + '/'
                    + MuseStringUtils.formatNumberShort(adapter.getMaxEnergyStored());
            currentTipList.add(MuseStringUtils.wrapMultipleFormatTags(energyinfo, MuseStringUtils.FormatCodes.Italic.character,
                    MuseStringUtils.FormatCodes.Grey));
        }
        if (MPSConfig.getInstance().doAdditionalInfo()) {
            List<String> installed = getItemInstalledModules(player, stack);
            if (installed.size() == 0) {
                String message = I18n.format("tooltip.noModules");
                currentTipList.addAll(MuseStringUtils.wrapStringToLength(message, 30));
            } else {
                currentTipList.add(I18n.format("tooltip.installedModules"));
                currentTipList.addAll(installed);
            }
        } else {
            currentTipList.add(MPSConfig.getInstance().additionalInfoInstructions());
        }
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
     * Checks the given NBTTag and returns the value if it exists, otherwise 0.
     */
    public static int getIntOrZero(NBTTagCompound itemProperties, String string) {
        int value = 0;
        if (itemProperties != null) {
            if (itemProperties.hasKey(string)) {
                value = itemProperties.getInteger(string);
            }
        }
        return value;
    }

    /**
     * Checks the given NBTTag and returns the value if it exists, otherwise 0.
     */
    public static byte getByteOrZero(NBTTagCompound itemProperties, String string) {
        byte value = 0;
        if (itemProperties != null) {
            if (itemProperties.hasKey(string)) {
                value = itemProperties.getByte(string);
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
        List<ItemStack> modulars = new ArrayList<>();
        for (EntityEquipmentSlot slot :EntityEquipmentSlot.values()) {
            ItemStack stack = player.getItemStackFromSlot(slot);

            if (!stack.isEmpty() && stack.getItem() instanceof IMuseItem) {
                modulars.add(stack);
            }
        }
        return modulars;
    }

    public static boolean canStackTogether(ItemStack stack1, ItemStack stack2) {
        if (!isSameItem(stack1, stack2)) {
            return false;
        } else if (!stack1.isStackable()) {
            return false;
        } else
            return stack1.getCount() < stack1.getMaxStackSize();
    }

    public static boolean isSameItem(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        if (!stack1.isEmpty() || !stack2.isEmpty()) {
            return false;
        } else if (stack1.getItem() != stack2.getItem()) {
            return false;
        } else
            return !((!stack1.isItemStackDamageable())
                    && (stack1.getItemDamage() != stack2.getItemDamage()));
    }

    public static void transferStackWithChance(ItemStack itemsToGive, ItemStack destinationStack, double chanceOfSuccess) {
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.lvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.mvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }
        if (MuseItemUtils.isSameItem(itemsToGive, ItemComponent.hvcapacitor)) {
            itemsToGive.setCount(0);
            return;
        }

        int maxSize = destinationStack.getMaxStackSize();
        while (itemsToGive.getCount() > 0 && destinationStack.getCount() < maxSize) {
            itemsToGive.setCount(itemsToGive.getCount()- 1);
            if (MuseMathUtils.nextDouble() < chanceOfSuccess) {
                destinationStack.setCount(destinationStack.getCount() + 1);
            }
        }
    }

    public static Set<Integer> giveOrDropItems(ItemStack itemsToGive, EntityPlayer player) {
        return giveOrDropItemWithChance(itemsToGive, player, 1.0);
    }

    public static Set<Integer> giveOrDropItemWithChance(ItemStack itemsToGive, EntityPlayer player, double chanceOfSuccess) {
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
            if (player.inventory.getStackInSlot(i).isEmpty()) {
                ItemStack destination = new ItemStack(itemsToGive.getItem(), 0, itemsToGive.getItemDamage());
                transferStackWithChance(itemsToGive, destination, chanceOfSuccess);
                if (destination.getCount() > 0) {
                    player.inventory.setInventorySlotContents(i, destination);
                    slots.add(i);
                }
            }
        }
        // Finally spawn the items in the world.
        if (itemsToGive.getCount() > 0) {
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

    public static boolean canItemFitInInventory(EntityPlayer player, ItemStack itemstack) {
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
                if (!invstack.isEmpty() && invstack.getItem() == itemstack.getItem() && invstack.isStackable() && invstack.getCount() < invstack.getMaxStackSize() && invstack.getCount() < player.inventory.getInventoryStackLimit() && (!invstack.getHasSubtypes() || invstack.getItemDamage() == itemstack.getItemDamage())) {
                    return true;
                }
            }
        }
        return false;
    }







// Placeholder // https://gist.github.com/cpw/9af398451a20459ac263
// @GameRegistry.ItemStackHolder()
// put here for future reference




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
