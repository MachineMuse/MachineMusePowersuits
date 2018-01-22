package net.machinemuse.powersuits.common.energy;

import ic2.api.item.ElectricItem;
import ic2.api.item.IBackupElectricItemManager;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import javax.annotation.Nullable;


public enum MuseElectricItemManager implements IBackupElectricItemManager {
    INSTANCE;

    public static void init() {
        ElectricItem.registerBackupManager(INSTANCE);
    }

    /** BackupElectricItemManager ----------------------------------------------------------------- */
    @Override
    public boolean handles(final ItemStack stack) {
        // TODO: add items here (obviously)

        return false; //stack.getItem() == MPSItems.getInstance().powerFist || stack.getItem() instanceof ItemCapacitor;
    }

    /** IElectricItemManager ---------------------------------------------------------------------- */
    @Override
    public double charge(final ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null) {
            return 0;
        }
        if (!ignoreTransferLimit)
            amount = Math.min(amount, energyStorage.getMaxEnergyStored() * .025);
        return ModCompatibility.getIC2Ratio() *
                (energyStorage.receiveEnergy((int) ((amount)/ ModCompatibility.getIC2Ratio()), simulate));
    }

    @Override
    public double discharge(final ItemStack stack, double amount, int tier, boolean ignoreTransferLimit, boolean externally, boolean simulate) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null)
            return 0;
        if (!ignoreTransferLimit)
            amount = Math.min(amount, energyStorage.getMaxEnergyStored() * .025);
        return ModCompatibility.getIC2Ratio() *
                (energyStorage.extractEnergy((int) ((amount) / ModCompatibility.getIC2Ratio()), simulate));
    }

    @Override
    public double getCharge(final ItemStack stack) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null)
            return 0;
        return ModCompatibility.getIC2Ratio() *(energyStorage.getEnergyStored());
    }

    @Override
    public double getMaxCharge(final ItemStack stack) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null)
            return 0;
        return ModCompatibility.getIC2Ratio() *(energyStorage.getMaxEnergyStored());
    }

    @Override
    public boolean canUse(final ItemStack stack, final double amount) {
        System.out.println("can use: " + stack.getDisplayName());
        return false;
    }

    @Override
    public boolean use(final ItemStack stack, final double amount, final EntityLivingBase entity) {
        System.out.println("use: " + stack.getDisplayName());
        return false;
    }

    @Override
    public void chargeFromArmor(final ItemStack stack, final EntityLivingBase entity) {
        System.out.println("charge from armor: " + stack.getDisplayName());
    }

    @Nullable
    @Override
    public String getToolTip(final ItemStack stack) {
        return null;
    }

    @Override
    public int getTier(final ItemStack stack) {
        // FIXME

//        if (stack.getItem() instanceof ItemCapacitor)
//            return stack.getMetadata() +1;
        return 1;
    }
}
