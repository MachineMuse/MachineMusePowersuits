package net.forgetest;

import net.minecraft.item.ItemBucketMilk;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

import javax.annotation.Nonnull;

import static net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack.FLUID_NBT_KEY;

public class EmptyContainerHandler extends FluidBucketWrapper {
    public EmptyContainerHandler(@Nonnull ItemStack container) {
        super(container);
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if (container.getCount() != 1 || resource == null || resource.amount > Fluid.BUCKET_VOLUME || container
                .getItem() instanceof ItemBucketMilk || getFluid() != null || !canFillFluidType(resource)) {
            return 0;
        }

        if (doFill) {
            container = new ItemStack(FluidContainer.instance);
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound fluidTag = new NBTTagCompound();
            resource.writeToNBT(fluidTag);
            tag.setTag(FLUID_NBT_KEY, fluidTag);
            container.setTagCompound(tag);
        }

        return resource.amount;
    }
}