package net.machinemuse.powersuits.capabilities;

import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class MPSCapProvider implements ICapabilityProvider {
    private final ItemStack container;
    //    MuseHeatItemWrapper heatWrapper;
    ForgeEnergyItemContainerWrapper energyContainerWrapper;
    ItemHandlerPowerFist powerFistItemHandler;



    public MPSCapProvider(@Nonnull final ItemStack containerIn) {
        this.container = containerIn;

        // Forge Energy
        energyContainerWrapper = new ForgeEnergyItemContainerWrapper(containerIn, ModuleManager.INSTANCE);

        // Scannable
        if (container.getItem() instanceof ItemPowerFist)
            powerFistItemHandler = new ItemHandlerPowerFist(container);




        // todo: fluid handlers for cooling system modules





    }



    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (/*capability == CapabilityHeat.HEAT ||*/ capability == CapabilityEnergy.ENERGY)
            return true;
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            if (powerFistItemHandler != null)
                return true;


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
//                if (modularItemWrapper != null) {
//                    modularItemWrapper.updateFromNBT();
//                } else {
//                    modeChangingItemWrapper.updateFromNBT();
//                }
                return (T) energyContainerWrapper;
            }
        }
        return null;
    }
}
