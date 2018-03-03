package net.machinemuse.numina.api.energy.adapater;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.machinemuse.powersuits.common.ModCompatibility;
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
        if (itemStack == null || itemStack.isEmpty())
            return null;
        Item i = itemStack.getItem();

        System.out.println(itemStack.getTagCompound());

        // Forge Energy
        if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null)) {
            System.out.println("using forge energy");

            return new ForgeEnergyAdapter(itemStack);

            // TESLA (need all 3 in order to get power in and out)
        } else if (itemStack.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null)) {

            System.out.println("using Tesla energy");
            return new TeslaEnergyAdapter(itemStack);

            // RF API
        } else if (ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) {
            System.out.println("using RF energy");
            return new TEElectricAdapter(itemStack);

            // Industrialcraft
        } else if (ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) {
            System.out.println("using Industrialcraft energy");

            return new IC2ElectricAdapter(itemStack);
        } else {
            System.out.println("NO MATCHING POWER INTERFACE!!!");

            return null;
        }
    }

    public abstract int getEnergyStored();

    public abstract int getMaxEnergyStored();

    public abstract int extractEnergy(int requested, boolean simulate);

    public abstract int receiveEnergy(int provided, boolean simulate);
}