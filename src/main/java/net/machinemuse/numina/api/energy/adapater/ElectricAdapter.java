package net.machinemuse.numina.api.energy.adapater;

import cofh.redstoneflux.api.IEnergyContainerItem;
import ic2.api.item.IElectricItem;
import mekanism.api.energy.IEnergizedItem;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.machinemuse.numina.api.energy.IMuseElectricItem;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nullable;

/**
 * Ported to Java by lehjr on 11/4/16.
 */
public abstract class ElectricAdapter {
    @Nullable
    public static ElectricAdapter wrap(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty())
            return null;
        Item i = itemStack.getItem();

        // Numina native power implentation
        if (i instanceof IMuseElectricItem)
            return new MuseElectricAdapter(itemStack);

        // Forge Energy
        else if (itemStack.hasCapability(CapabilityEnergy.ENERGY, null))
            return new ForgeEnergyAdapter(itemStack);

        // TESLA (need all 3 in order to get power in and out)
        else if (itemStack.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null) &&
                itemStack.hasCapability(TeslaCapabilities.CAPABILITY_PRODUCER, null))
            return new TeslaEnergyAdapter(itemStack);

        // RF API
        else if (ModCompatibility.isRFAPILoaded() && i instanceof IEnergyContainerItem) {
            return new TEElectricAdapter(itemStack);

        // Mekanism
        } else if (ModCompatibility.isMekanismLoaded() && i instanceof IEnergizedItem) {
            if (!((IEnergizedItem) i).canSend(itemStack)) // don't count items that can't supply power
                return null;
            return new MekanismElectricAdapter(itemStack);

        // Industrialcraft
        } else if (ModCompatibility.isIndustrialCraftLoaded() && i instanceof IElectricItem) {
            return new IC2ElectricAdapter(itemStack);
        } else {
            return null;
        }
    }

    public abstract int getCurrentMPSEnergy();

    public abstract int getMaxMPSEnergy();

    public abstract int drainMPSEnergy(int requested);

    public abstract int giveMPSEnergy(int provided);
}