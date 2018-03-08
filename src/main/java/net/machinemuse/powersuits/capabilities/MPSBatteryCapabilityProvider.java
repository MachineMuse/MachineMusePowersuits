package net.machinemuse.powersuits.capabilities;

import net.machinemuse.numina.api.constants.NuminaNBTConstants;
import net.machinemuse.numina.api.capability_ports.itemwrapper.ForgeEnergyItemWrapper;
import net.machinemuse.numina.utils.nbt.NuminaNBTUtils;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.item.module.energy.AdvancedBatteryModule;
import net.machinemuse.powersuits.item.module.energy.BasicBatteryModule;
import net.machinemuse.powersuits.item.module.energy.EliteBatteryModule;
import net.machinemuse.powersuits.item.module.energy.UltimateBatteryModule;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MPSBatteryCapabilityProvider implements ICapabilityProvider {
    ForgeEnergyItemWrapper energyWrapper;
    ItemStack container;

    public MPSBatteryCapabilityProvider(@Nonnull ItemStack container) {
        this.container = container;
        NBTTagCompound containerNBT = NuminaNBTUtils.getMuseItemTag(container);
        Item item = container.getItem();

        int currentEnergy = 0;
        int maxEnergy = 0;
        int maxEnergyTransfer = 0;

        if (item instanceof BasicBatteryModule)
            maxEnergy = MPSConfig.getInstance().maxEnergyBasicBattery();
        else if (item instanceof AdvancedBatteryModule)
            maxEnergy = MPSConfig.getInstance().maxEnergyAdvancedBattery();
        else if (item instanceof EliteBatteryModule)
            maxEnergy = MPSConfig.getInstance().maxEnergyEliteBattery();
        else if (item instanceof UltimateBatteryModule)
            maxEnergy = MPSConfig.getInstance().maxEnergyUltimateBattery();

        if (containerNBT.hasKey(NuminaNBTConstants.CURRENT_ENERGY))
            currentEnergy = containerNBT.getInteger(NuminaNBTConstants.CURRENT_ENERGY);
        else
            containerNBT.setInteger(NuminaNBTConstants.CURRENT_ENERGY, 0);

        if (containerNBT.hasKey(NuminaNBTConstants.MAXIMUM_ENERGY))
            maxEnergyTransfer = maxEnergy = containerNBT.getInteger(NuminaNBTConstants.MAXIMUM_ENERGY);
        else
            containerNBT.setInteger(NuminaNBTConstants.MAXIMUM_ENERGY, maxEnergy);
        energyWrapper = new ForgeEnergyItemWrapper(container, maxEnergy, maxEnergyTransfer, currentEnergy);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityEnergy.ENERGY;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == CapabilityEnergy.ENERGY) {
            if (energyWrapper != null) {
                energyWrapper.updateFromNBT();
                return (T) energyWrapper;
            }
        }
        return null;
    }
}
