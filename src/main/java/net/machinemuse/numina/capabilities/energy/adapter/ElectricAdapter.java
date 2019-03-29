package net.machinemuse.numina.capabilities.energy.adapter;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.machinemuse.numina.misc.ModCompatibility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ElectricAdapter {
    @Nullable
    public static ElectricAdapter wrap(@Nonnull ItemStack itemStack) {
        //TODO: add a configurable blacklist

        if (itemStack.isEmpty())
            return null;
        Item i = itemStack.getItem();

        String itemMod = itemStack.getItem().getRegistryName().getNamespace();

        if (BlackList.blacklistModIds.contains(itemMod))
            return null;

        // Forge Energy
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            return new ForgeEnergyAdapter(itemStack);

            // TESLA (need all 3 in order to get power in and out)
        } else if (ModCompatibility.isTeslaLoaded() &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)) {
            return new TeslaEnergyAdapter(itemStack);

            // RF API
        } else if (ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) {
            return new TEElectricAdapter(itemStack);

            // Industrialcraft
        } else if (ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) {
//            System.out.println("IC2 energy");
            return new IC2ElectricAdapter(itemStack);
        } else {
            return null;
        }
    }

    public abstract int getEnergyStored();

    public abstract int getMaxEnergyStored();

    public abstract int extractEnergy(int requested, boolean simulate);

    public abstract int receiveEnergy(int provided, boolean simulate);
}