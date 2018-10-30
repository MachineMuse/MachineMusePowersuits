package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.capabilities.heat.CapabilityHeat;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class MPSCapProvider implements ICapabilityProvider {
    private final ItemStack container;
    MuseHeatItemWrapper heatWrapper;
    ForgeEnergyItemWrapper energyContainerWrapper;
    ItemHandlerPowerFist powerFistItemHandler;
    MPSChestPlateFluidHandler chestPlateFluidHandler;

    public MPSCapProvider(@Nonnull final ItemStack containerIn) {
        this.container = containerIn;

        // Forge Energy
        energyContainerWrapper = new ForgeEnergyItemWrapper(containerIn, ModuleManager.INSTANCE);

        // Heat
        heatWrapper = new MuseHeatItemWrapper(containerIn, MPSConfig.INSTANCE.getBaseMaxHeat(containerIn), ModuleManager.INSTANCE);

        // Scannable
        if (container.getItem() instanceof ItemPowerFist)
            powerFistItemHandler = new ItemHandlerPowerFist(container);
        // todo: fluid handler for the personal shrinking module


        if (container.getItem() instanceof ItemPowerArmorChestplate)
            chestPlateFluidHandler = new MPSChestPlateFluidHandler(container, ModuleManager.INSTANCE);


        // todo: fluid handlers for cooling system modules


    }


    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (/*capability == CapabilityHeat.HEAT ||*/ capability == CapabilityEnergy.ENERGY)
            return true;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (powerFistItemHandler != null)
                return true;
        // TODO: others

        if (capability == CapabilityHeat.HEAT)
            if (heatWrapper != null)
                return true;

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
            return chestPlateFluidHandler != null;


        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (powerFistItemHandler != null) {
                powerFistItemHandler.updateFromNBT();
                return (T) powerFistItemHandler;
            }

            // TODO: ItemHandler for other things?
            return null;
        }

        if (capability == CapabilityEnergy.ENERGY) {
            if (energyContainerWrapper != null) {
                energyContainerWrapper.updateFromNBT();
                return (T) energyContainerWrapper;
            }
        }

        if (capability == CapabilityHeat.HEAT) {
            if (heatWrapper != null) {
                heatWrapper.updateFromNBT();
                return (T) heatWrapper;
            }
        }

        if (capability == CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY) {
            if (chestPlateFluidHandler != null) {
                chestPlateFluidHandler.updateFromNBT();
                return (T) chestPlateFluidHandler;
            }
        }
        return null;
    }
}
