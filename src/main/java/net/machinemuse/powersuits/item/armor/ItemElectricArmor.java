package net.machinemuse.powersuits.item.armor;

//import appeng.api.config.AccessRestriction;

import ic2.api.item.ElectricItem;
import mekanism.api.energy.IEnergizedItem;
import net.machinemuse.numina.api.energy.ElectricConversions;
import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.math.geometry.Colour;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.item.module.cosmetic.CosmeticGlowModule;
import net.machinemuse.powersuits.item.module.cosmetic.TintModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.numina.utils.string.MuseStringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.numina.math.MuseMathUtils.clampDouble;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
public abstract class ItemElectricArmor extends ItemArmor implements IMuseItem, IMuseElectricItem {
    public ItemElectricArmor(ItemArmor.ArmorMaterial material, int renderIndexIn, EntityEquipmentSlot slot) {
        super(material, renderIndexIn, slot);
    }

    @Override
    public String getToolTip(ItemStack itemStack) {
        return "";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return getColorFromItemStack(stack).getInt();
    }

    @Override
    public Colour getGlowFromItemStack(ItemStack stack) {
        if (!ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.getInstance().computeModularPropertyDouble(stack, CosmeticGlowModule.BLUE_GLOW);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
        return colour;
    }

    @Override
    public Colour getColorFromItemStack(ItemStack stack) {
        if (!ModuleManager.getInstance().itemHasActiveModule(stack, MPSModuleConstants.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.getInstance().computeModularPropertyDouble(stack, TintModule.BLUE_TINT);
        Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
        return colour;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseItemUtils.addInformation(stack, player, currentTipList, advancedToolTips);
    }

    @Override
    public String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }

    /* IMuseItem ------------------------------------------------------------------------------- */
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", getCurrentMPSEnergy(stack)) + 'J');
        info.add(formatInfo("Weight", WeightHelper.getTotalWeight(stack)) + 'g');
        return info;
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
        return 0;
    }

    //=================================================================================
    @Override
    public boolean hasColor(ItemStack stack) {
        return true;
    }

    @Override
    public int getColor(ItemStack stack) {
        Colour c = this.getColorFromItemStack(stack);
//        setColor(stack, c.getInt());

        return c.getInt();
    }

//    @Override
//    public void setColor(ItemStack stack, int color) {
//        NBTTagCompound nbttagcompound = stack.getTagCompound();
//
//        if (nbttagcompound == null)
//        {
//            nbttagcompound = new NBTTagCompound();
//            stack.setTagCompound(nbttagcompound);
//        }
//
//        NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");
//
//        if (!nbttagcompound.hasKey("display", 10))
//        {
//            nbttagcompound.setTag("display", nbttagcompound1);
//        }
//
//        nbttagcompound1.setInteger("color", color);
//    }

    @Override
    public boolean hasOverlay(ItemStack stack) {
        return true;
    }

    //===========================================================================================

    @Override
    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    /**
     * Call to get the energy of an item
     *
     * @param container ItemStack to set
     * @return Current energy level
     */
    public int getCurrentMPSEnergy(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    /**
     * Returns the maximum energy of an item
     *
     * @param container ItemStack to set
     * @return Maximum energy level
     */
    public int getMaxMPSEnergy(ItemStack container)  {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.getMaxEnergyStored() : 0;
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    @Override
    public void setCurrentMPSEnergy(ItemStack stack, int energy) {
        MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxMPSEnergy(stack)));
    }

    /**
     * Call to drain energy from an item
     *
     * @param container ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    @Override
    public int drainMPSEnergyFrom(ItemStack container, int requested) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.extractEnergy(requested, false) : 0;
    }

    /**
     * Call to give energy to an item
     *
     * @param container ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    @Override
    public int giveMPSEnergyTo(ItemStack container, int provided) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(provided, false) : 0;
    }

    /**
     * Call to give energy to an item
     *
     * @param container ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    public int giveMPSEnergyTo(ItemStack container, int provided, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(provided, simulate) : 0;
    }


    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    public IMuseElectricItem getManager(ItemStack stack) {
        return this;
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
    }

    public boolean canProvideEnergy(ItemStack itemStack) {
        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (itemStack.getItem() instanceof IEnergizedItem)
                return ((IEnergizedItem)item).canSend(itemStack);
        }
        return true;
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getCurrentMPSEnergy(itemStack));
    }

    public double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxMPSEnergy(itemStack));
    }

    public int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    public double getTransferLimit(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxMPSEnergy(itemStack)));
    }

    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
        double current = getCurrentMPSEnergy(itemStack);
        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : ElectricConversions.museEnergyFromEU(getTransferLimit(itemStack));
        int given = giveMPSEnergyTo(itemStack, transfer);
        if (simulate) {
            setCurrentMPSEnergy(itemStack, (int) current);
        }
        return ElectricConversions.museEnergyToEU(given);
    }

    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        double current = getCurrentMPSEnergy(itemStack);
        int transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : ElectricConversions.museEnergyFromEU(getTransferLimit(itemStack));
        double taken = drainMPSEnergyFrom(itemStack, transfer);
        if (simulate) {
            setCurrentMPSEnergy(itemStack, (int) current);
        }
        return ElectricConversions.museEnergyToEU(taken);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getCurrentMPSEnergy(itemStack);
    }

    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    /* Thermal Expansion -------------------------------------------------------------------------- */
    public int receiveEnergy(ItemStack container, int energy, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(energy, simulate) : 0;
    }

    public int extractEnergy(ItemStack container, int energy, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.extractEnergy(energy, simulate) : 0;
    }

    public int getEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    public int getMaxEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.getMaxEnergyStored() : 0;
    }

    /* Mekanism ----------------------------------------------------------------------------------- */
    @Override
    public double getEnergy(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getCurrentMPSEnergy(itemStack));
    }

    @Override
    public void setEnergy(ItemStack itemStack, double v) {
        setCurrentMPSEnergy(itemStack, ElectricConversions.museEnergyFromMek(v));
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getMaxMPSEnergy(itemStack));
    }

    @Override
    public double getMaxTransfer(ItemStack itemStack) {
        return ElectricConversions.museEnergyToMek(getMaxMPSEnergy(itemStack));
    }

    @Override
    public boolean canReceive(ItemStack itemStack) {
        return getMaxMPSEnergy(itemStack) > 0;
    }

    @Override
    public boolean canSend(ItemStack itemStack) {
        return getCurrentMPSEnergy(itemStack) < 0;
    }
}