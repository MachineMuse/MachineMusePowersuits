package net.machinemuse.powersuits.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class CoalGenCapability extends ItemStackHandler {
    private final ItemStack container;
    LazyOptional<IEnergyStorage> energyHolder;
    LazyOptional<IItemHandler> fuelSlot;

    public CoalGenCapability(@Nonnull ItemStack container) {
        super(1);
        this.container = container;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return TileEntityFurnace.isItemFuel(stack);
    }

    public class EnergyWrapper extends EnergyStorage {
        public EnergyWrapper() {
            super(2000);
        }











    }
}
