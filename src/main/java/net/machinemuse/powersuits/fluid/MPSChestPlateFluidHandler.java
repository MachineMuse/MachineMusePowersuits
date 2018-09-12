package net.machinemuse.powersuits.fluid;

import com.google.common.collect.Lists;
import net.machinemuse.numina.api.module.IModuleManager;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class MPSChestPlateFluidHandler implements IFluidHandler, IFluidHandlerItem {
    public class WaterTank extends FluidTank {
        public WaterTank() {
            super(100000);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            if (fluid != null)
                return fluid.getFluid() == FluidRegistry.WATER;
            return true;
        }

        // TODO:
        @Override
        public FluidTank readFromNBT(NBTTagCompound nbt) {
            System.out.println("NBT: " + nbt);

            return super.readFromNBT(nbt);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            System.out.println("NBT: " + nbt);

            return super.writeToNBT(nbt);
        }
    }

    public class AdvancedCoolingTank extends FluidTank {
        public AdvancedCoolingTank() {
            super(100);
        }

        @Override
        public boolean canFillFluidType(FluidStack fluid) {
            if (fluid != null)
                return fluid.getFluid() != FluidRegistry.WATER;
            return true;
        }

        // TODO:
        @Override
        public FluidTank readFromNBT(NBTTagCompound nbt) {
            System.out.println("NBT: " + nbt);
            return super.readFromNBT(nbt);
        }

        @Override
        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            System.out.println("NBT: " + nbt);
            return super.writeToNBT(nbt);
        }
    }

    private final WaterTank waterTank;
    AdvancedCoolingTank advancedCoolingTank;

    ItemStack container;
    IModuleManager moduleManager;
    public MPSChestPlateFluidHandler(@Nonnull ItemStack container, IModuleManager moduleManager) {
        this.container = container;
        this.moduleManager = moduleManager;
        this.waterTank = new WaterTank();
        this.advancedCoolingTank = new AdvancedCoolingTank();
    }

    @Nonnull
    @Override
    public ItemStack getContainer() {
        return container;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        List<IFluidTankProperties> tanks = Lists.newArrayList();
        Collections.addAll(tanks, waterTank.getTankProperties());
        Collections.addAll(tanks, advancedCoolingTank.getTankProperties());
        return tanks.toArray(new IFluidTankProperties[tanks.size()]);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (resource == null)
            return 0;
        IFluidHandler handler = null;
        if (resource.getFluid() == FluidRegistry.WATER && moduleManager.itemHasModule(container, MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME))
            handler = waterTank;
        else if (resource.getFluid() != FluidRegistry.WATER && moduleManager.itemHasModule(container, MPSModuleConstants.ADVANCED_COOLING_SYSTEM__DATANAME))
            handler = advancedCoolingTank;
        if (handler == null)
            return 0;

        System.out.println("trying to add fluid: " + (resource != null ? resource.getFluid().getName() : "NULL"));
        return handler.fill(resource, doFill);
    }

    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {

        if (resource == null)
            return null;
        IFluidHandler handler;
        if (resource.getFluid() == FluidRegistry.WATER && moduleManager.itemHasModule(container, MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME))
            handler = waterTank;
        else if (resource.getFluid() != FluidRegistry.WATER && moduleManager.itemHasModule(container, MPSModuleConstants.ADVANCED_COOLING_SYSTEM__DATANAME));
        handler = advancedCoolingTank;
        if (handler == null)
            return null;
        System.out.println("trying to drain fluid: " + (resource != null ? resource.getFluid().getName() : "NULL"));

        return handler.drain(resource, doDrain);
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        System.out.println("trying to drain any fluid: " + maxDrain);
        FluidStack drain = null;
        if (moduleManager.itemHasModule(container, MPSModuleConstants.BASIC_COOLING_SYSTEM__DATANAME))
            drain = waterTank.drain(maxDrain, doDrain);
        if (drain != null)
            return drain;
        if (moduleManager.itemHasModule(container, MPSModuleConstants.ADVANCED_COOLING_SYSTEM__DATANAME));
            drain = advancedCoolingTank.drain(maxDrain, doDrain);
        if (drain != null)
            return drain;
        return null;
    }
}
