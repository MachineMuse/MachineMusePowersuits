package net.machinemuse.powersuits.capabilities;

import net.machinemuse.powersuits.api.electricity.adapter.IMuseElectricItem;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class MPSCapProvider implements ICapabilityProvider {
    ItemStack container;
//    MuseHeatItemWrapper heatWrapper;
    ForgeEnergyItemContainerWrapper energyContainerWrapper;


    public MPSCapProvider(final ItemStack container) {
        this.container = container;


        // todo: fluid handlers for cooling system modules




        if (!container.isEmpty() && container.getItem() instanceof IMuseElectricItem) {
            energyContainerWrapper = new ForgeEnergyItemContainerWrapper(container, ModuleManager.INSTANCE);
        }
    }



    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        if (/*capability == CapabilityHeat.HEAT ||*/ capability == CapabilityEnergy.ENERGY)
            return true;
        return false;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {

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
