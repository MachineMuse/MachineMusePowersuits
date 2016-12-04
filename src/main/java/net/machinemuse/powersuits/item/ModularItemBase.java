package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IMuseElectricItem;
import net.machinemuse.api.electricity.MuseElectricItem;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.powermodule.misc.CosmeticGlowModule;
import net.machinemuse.powersuits.powermodule.misc.TintModule;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseStringUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static net.machinemuse.numina.general.MuseMathUtils.clampDouble;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 7:49 PM, 4/23/13
 *
 * Ported to Java by lehjr on 11/4/16.
 */
public class ModularItemBase extends Item implements IModularItemBase {
    private static ModularItemBase INSTANCE;

    public static ModularItemBase getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ModularItemBase();
        }
        return INSTANCE;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getColorFromItemStack(ItemStack stack, int par2) {
        return getColorFromItemStack(stack).getInt();
    }

    @Override
    public Colour getGlowFromItemStack(ItemStack stack) {
        if (!ModuleManager.itemHasActiveModule(stack, CosmeticGlowModule.MODULE_GLOW)) {
            return Colour.LIGHTBLUE;
        }
        double computedred = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.RED_GLOW);
        double computedgreen = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.GREEN_GLOW);
        double computedblue = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.BLUE_GLOW);
        return new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
    }

    @Override
    public Colour getColorFromItemStack(ItemStack stack) {
        if (!ModuleManager.itemHasActiveModule(stack, TintModule.MODULE_TINT)) {
            return Colour.WHITE;
        }
        double computedred = ModuleManager.computeModularProperty(stack, TintModule.RED_TINT);
        double computedgreen = ModuleManager.computeModularProperty(stack, TintModule.GREEN_TINT);
        double computedblue = ModuleManager.computeModularProperty(stack, TintModule.BLUE_TINT);
        return new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
        MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips);
    }

    @Override
    public String formatInfo(String string, double value) {
        return string + '\t' + MuseStringUtils.formatNumberShort(value);
    }


    /* IModularItem ------------------------------------------------------------------------------- */
    @SideOnly(Side.CLIENT)
    @Override
    public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
        List<String> info = new ArrayList<>();

        info.add("Detailed Summary");
        info.add(formatInfo("Armor", getArmorDouble(player, stack)));
        info.add(formatInfo("Energy Storage", getCurrentEnergy(stack)) + 'J');
        info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + 'g');
        return info;
    }

    @Override
    public double getArmorDouble(EntityPlayer player, ItemStack stack) {
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
        ElectricItemUtils.drainPlayerEnergy(player, joulesToGive);
    }


    /* MuseElectricItem --------------------------------------------------------------------------- */
    @Override
    public double getCurrentEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(stack);
    }

    @Override
    public double getMaxEnergy(ItemStack stack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(stack);
    }

    @Override
    public void setCurrentEnergy(ItemStack stack, double energy) {
        MuseElectricItem.getInstance().setCurrentEnergy(stack, energy);
    }

    @Override
    public double drainEnergyFrom(ItemStack stack, double requested) {
        return MuseElectricItem.getInstance().drainEnergyFrom(stack, requested);
    }

    @Override
    public double giveEnergyTo(ItemStack stack, double provided) {
        return MuseElectricItem.getInstance().giveEnergyTo(stack, provided);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getToolTip(ItemStack itemStack) {
        return itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false).toString();
    }

    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    public IMuseElectricItem getManager(ItemStack stack) {
        return MuseElectricItem.getInstance().getManager(stack);
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
    }

    @Override
    public double getCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCharge(itemStack);
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxCharge(itemStack);
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTier(itemStack);
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getTransferLimit(itemStack);
    }

    @Override
    public double charge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        return MuseElectricItem.getInstance().charge(itemStack, amount, tier, ignoreTransferLimit, simulate);
    }

    @Override
    public double discharge(ItemStack itemStack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        return MuseElectricItem.getInstance().discharge(itemStack, amount, tier, ignoreTransferLimit, externally, simulate);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return MuseElectricItem.getInstance().canUse(itemStack, amount);
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getChargedItem(itemStack);
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(stack, energy, simulate);
    }

    @Override
    public int extractEnergy(ItemStack stack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(stack, energy, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getEnergyStored(theItem);
    }

    @Override
    public int getMaxEnergyStored(ItemStack theItem) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(theItem);
    }


    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    public double injectAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().injectAEPower(stack, ae);
    }

    @Override
    public double extractAEPower(ItemStack stack, double ae) {
        return MuseElectricItem.getInstance().extractAEPower(stack, ae);
    }

    @Override
    public double getAEMaxPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAEMaxPower(stack);
    }

    @Override
    public double getAECurrentPower(ItemStack stack) {
        return MuseElectricItem.getInstance().getAECurrentPower(stack);
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack stack) {
        return MuseElectricItem.getInstance().getPowerFlow(stack);
    }
}
