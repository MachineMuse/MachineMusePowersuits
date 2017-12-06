package net.machinemuse.powersuits.common.items.armor;

import cofh.redstoneflux.api.IEnergyContainerItem;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "cofh.redstoneflux.api.IEnergyContainerItem", modid = "redstoneflux")
public class ItemPowerArmorNew extends ItemArmor implements IEnergyContainerItem {
    public ItemPowerArmorNew(int renderIndexIn, EntityEquipmentSlot equipmentSlotIn) {
        super(ArmorMaterial.IRON, renderIndexIn, equipmentSlotIn);
    }








    @Override
    public boolean showDurabilityBar(final ItemStack stack) {
        return true;
    }

    @Override
    public double getDurabilityForDisplay(final ItemStack stack) {
        final IEnergyStorage energyStorage = stack.getCapability(CapabilityEnergy.ENERGY, null);
        if (energyStorage == null) {
            return 1;
        }

        return 1 - energyStorage.getEnergyStored() / (float) energyStorage.getMaxEnergyStored();
    }


    /** IEnergyContainerItem ---------------------------------------------------------------------- */
    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.receiveEnergy(maxReceive, simulate) : 0;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.extractEnergy(maxExtract, simulate) : 0;
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage != null ? energyStorage.getEnergyStored() : 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        final IEnergyStorage energyStorage = container.getCapability(CapabilityEnergy.ENERGY, null);
        return energyStorage!=null ? energyStorage.getMaxEnergyStored() : 0;
    }
}
