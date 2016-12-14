package net.machinemuse.powersuits.item;

import appeng.api.config.AccessRestriction;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.api.item.IElectricItemManager;
import net.machinemuse.api.ModuleManager;
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
        ElectricItemUtils.drainPlayerEnergy(player, joulesToGive);
    }


    /* MuseElectricItem --------------------------------------------------------------------------- */
    @Override
    public double getCurrentEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(itemStack);
    }

    @Override
    public double getMaxEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCurrentEnergy(itemStack);
    }

    @Override
    public void setCurrentEnergy(ItemStack itemStack, double energy) {
        MuseElectricItem.getInstance().setCurrentEnergy(itemStack, energy);
    }

    @Override
    public double drainEnergyFrom(ItemStack itemStack, double requested) {
        return MuseElectricItem.getInstance().drainEnergyFrom(itemStack, requested);
    }

    @Override
    public double giveEnergyTo(ItemStack itemStack, double provided) {
        return MuseElectricItem.getInstance().giveEnergyTo(itemStack, provided);
    }

    @Override
    public int getMaxDamage(ItemStack itemStack) {
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public String getToolTip(ItemStack itemStack) {
        return itemStack.getTooltip(Minecraft.getMinecraft().thePlayer, false).toString();
    }


    /* Industrialcraft 2 -------------------------------------------------------------------------- */
    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return MuseElectricItem.getInstance().canProvideEnergy(itemStack);
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getChargedItem(itemStack);
    }

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEmptyItem(itemStack);
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
    public double getCharge(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getCharge(itemStack);
    }

    @Override
    public boolean canUse(ItemStack itemStack, double amount) {
        return MuseElectricItem.getInstance().canUse(itemStack, amount);
    }

    @Override
    public boolean use(ItemStack itemStack, double amount, EntityLivingBase entity) {
        return MuseElectricItem.getInstance().use(itemStack, amount, entity);
    }

    @Override
    public void chargeFromArmor(ItemStack itemStack, EntityLivingBase entity) {
        MuseElectricItem.getInstance().chargeFromArmor(itemStack, entity);
    }


    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getManager(itemStack);
    }


    /* Thermal Expansion -------------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack itemStack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().receiveEnergy(itemStack, energy, simulate);
    }

    @Override
    public int extractEnergy(ItemStack itemStack, int energy, boolean simulate) {
        return MuseElectricItem.getInstance().extractEnergy(itemStack, energy, simulate);
    }

    @Override
    public int getEnergyStored(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getEnergyStored(itemStack);
    }

    @Override
    public int getMaxEnergyStored(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getMaxEnergyStored(itemStack);
    }


    /* Applied Energistics 2 ---------------------------------------------------------------------- */
    @Override
    public double injectAEPower(ItemStack itemStack, double ae) {
        return MuseElectricItem.getInstance().injectAEPower(itemStack, ae);
    }

    @Override
    public double extractAEPower(ItemStack itemStack, double ae) {
        return MuseElectricItem.getInstance().extractAEPower(itemStack, ae);
    }

    @Override
    public double getAEMaxPower(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getAEMaxPower(itemStack);
    }

    @Override
    public double getAECurrentPower(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getAECurrentPower(itemStack);
    }

    @Override
    public AccessRestriction getPowerFlow(ItemStack itemStack) {
        return MuseElectricItem.getInstance().getPowerFlow(itemStack);
    }
}
