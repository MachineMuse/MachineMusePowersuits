package net.forgetest;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public final class EmptyFluidContainer extends ItemBucket {
    public static final EmptyFluidContainer instance = new EmptyFluidContainer();
    public static final String name = "empty_fluid_container";

    private EmptyFluidContainer() {
        super(Blocks.AIR);
        setRegistryName(FluidPlacementTest.MODID, name);
        setUnlocalizedName(FluidPlacementTest.MODID + ":" + name);
        setMaxStackSize(16);
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        return new EmptyContainerHandler(stack);
    }
}
