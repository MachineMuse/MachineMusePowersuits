package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItemManager;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.ElectricConversions;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.powermodule.misc.CosmeticGlowModule;
import net.machinemuse.powersuits.powermodule.misc.TintModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.numina.general.MuseMathUtils.clampDouble;

/**
 * Ported to Java by lehjr on 10/26/16.
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "cofh.api.energy.IEnergyContainerItem", modid = "CoFHAPI|energy", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.IElectricItemManager", modid = "IC2", striprefs = true),
        @Optional.Interface(iface = "ic2.api.item.ISpecialElectricItem", modid = "IC2", striprefs = true),
        @Optional.Interface(iface = "appeng.api.implementations.items.IAEItemPowerStorage", modid = "appliedenergistics2", striprefs = true)
})
public abstract class ItemElectricArmor extends ItemArmor implements IModularItemBase, IMuseElectricItem {
    public ItemElectricArmor(final ItemArmor.ArmorMaterial material, final int index1, final int index2) {
        super(material, index1, index2);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack itemStack, int par2) {
        return getColorFromItemStack(itemStack).getInt();
    }

    @Override
    public Colour getGlowFromItemStack(ItemStack itemStack) {
        if (!ModuleManager.itemHasActiveModule(itemStack, CosmeticGlowModule.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.computeModularProperty(itemStack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.computeModularProperty(itemStack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.computeModularProperty(itemStack, CosmeticGlowModule.BLUE_GLOW);
        return new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
    }

    @Override
    public Colour getColorFromItemStack(ItemStack itemStack) {
        if (!ModuleManager.itemHasActiveModule(itemStack, TintModule.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.computeModularProperty(itemStack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.computeModularProperty(itemStack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.computeModularProperty(itemStack, TintModule.BLUE_TINT);
        return new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return false;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseCommonStrings.addInformation(itemStack, player, currentTipList, advancedToolTips);
    }

    @Override
    public String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }


    /* IModularItem ------------------------------------------------------------------------------- */
    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack itemStack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, itemStack)));
        info.add(formatInfo("Energy Storage", getCurrentEnergy(itemStack)) + 'J');
        info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(itemStack)) + 'g');
        return info;
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack itemStack) {
        return 0;
    }

    @Override
    public double getPlayerEnergy(EntityPlayer player) {
        return ElectricItemUtils.getPlayerEnergy(player);
    }

    @Override
    public void drainPlayerEnergy(EntityPlayer player, double drainEnergy) {
        ElectricItemUtils.drainPlayerEnergy(player, drainEnergy);
    }

    @Override
    public void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
        ElectricItemUtils.givePlayerEnergy(player, joulesToGive);
    }

    @Override
    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getToolTip(ItemStack itemStack) {
        return null;
    }

    /**
     * Call to get the energy of an item
     *
     * @param stack ItemStack to set
     * @return Current energy level
     */
    @Override
    public double getCurrentEnergy(ItemStack stack) {
        return MuseItemUtils.getDoubleOrZero(stack, ElectricItemUtils.CURRENT_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @return Maximum energy level
     */
    @Override
    public double getMaxEnergy(ItemStack stack) {
        return ModuleManager.computeModularProperty(stack, ElectricItemUtils.MAXIMUM_ENERGY);
    }

    /**
     * Call to set the energy of an item
     *
     * @param stack ItemStack to set
     * @param energy Level to set it to
     */
    @Override
    public void setCurrentEnergy(ItemStack stack, double energy) {
        MuseItemUtils.setDoubleOrRemove(stack, ElectricItemUtils.CURRENT_ENERGY, Math.min(energy, getMaxEnergy(stack)));
    }

    /**
     * Call to drain energy from an item
     *
     * @param stack ItemStack being requested for energy
     * @param requested Amount of energy to drain
     * @return Amount of energy successfully drained
     */
    @Override
    public double drainEnergyFrom(ItemStack stack, double requested) {
        double available = getCurrentEnergy(stack);
        if (available > requested) {
            setCurrentEnergy(stack, available - requested);
            return requested;
        } else {
            setCurrentEnergy(stack, 0);
            return available;
        }
    }

    /**
     * Call to give energy to an item
     *
     * @param stack ItemStack being provided with energy
     * @param provided Amount of energy to add
     * @return Amount of energy added
     */
    @Override
    public double giveEnergyTo(ItemStack stack, double provided) {
        double available = getCurrentEnergy(stack);
        double max = getMaxEnergy(stack);
        if (available + provided < max) {
            setCurrentEnergy(stack, available + provided);
            return provided;
        } else {
            setCurrentEnergy(stack, max);
            return max - available;
        }
    }

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Optional.Method(modid = "IC2")
    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return true;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getMaxEnergy(itemStack));
    }

    @Optional.Method(modid = "IC2")
    @Override
    public int getTier(ItemStack itemStack) {
        return ElectricConversions.getTier(itemStack);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(Math.sqrt(getMaxEnergy(itemStack)));
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate){
        double current = getCurrentEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double given = giveEnergyTo(itemStack, transfer);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(given);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double transfer = (ignoreTransferLimit || amount < getTransferLimit(itemStack)) ? ElectricConversions.museEnergyFromEU(amount) : getTransferLimit(itemStack);
        double taken = drainEnergyFrom(itemStack, transfer);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToEU(taken);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public double getCharge(ItemStack itemStack) {
        return ElectricConversions.museEnergyToEU(getCurrentEnergy(itemStack));
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return ElectricConversions.museEnergyFromEU(amount) < getCurrentEnergy(itemStack);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return ElectricItem.rawManager.use(itemStack, ElectricConversions.museEnergyToEU(amount), entity);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        ElectricItem.rawManager.chargeFromArmor(itemStack, entity);
    }

    @Optional.Method(modid = "IC2")
    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return this;
    }
    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack itemStack, int energy, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double receivedME = ElectricConversions.museEnergyFromRF(energy);
        double eatenME = giveEnergyTo(itemStack, receivedME);
        if (simulate) setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToRF(eatenME);
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int energy, boolean simulate) {
        double current = getCurrentEnergy(itemStack);
        double requesteddME = ElectricConversions.museEnergyFromRF(energy);
        double takenME = drainEnergyFrom(itemStack, requesteddME);
        if (simulate) {
            setCurrentEnergy(itemStack, current);
        }
        return ElectricConversions.museEnergyToRF(takenME);
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        return ElectricConversions.museEnergyToRF(getCurrentEnergy(itemStack));
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return ElectricConversions.museEnergyToRF(getMaxEnergy(itemStack));
    }

    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    public double injectAEPower(ItemStack itemStack, double ae) {
        double current = getCurrentEnergy(itemStack);
        double recieved = ElectricConversions.museEnergyFromAE(ae);
        setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToAE(recieved);
    }

    @Override
    public double extractAEPower(ItemStack itemStack, double ae) {
        double current = getCurrentEnergy(itemStack);
        double taken = ElectricConversions.museEnergyFromAE(ae);
        setCurrentEnergy(itemStack, current);
        return ElectricConversions.museEnergyToAE(taken);
    }

    @Override
    public double getAEMaxPower(ItemStack itemStack) {
        return ElectricConversions.museEnergyToAE(getCurrentEnergy(itemStack));
    }

    @Override
    public double getAECurrentPower(ItemStack itemStack) {
        return ElectricConversions.museEnergyToAE(getCurrentEnergy(itemStack));
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        return AccessRestriction.READ_WRITE;
    }
}
