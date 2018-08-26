package net.forgetest;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

import static net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack.FLUID_NBT_KEY;

public class FluidContainer extends UniversalBucket {
    public static final FluidContainer instance = new FluidContainer();
    public static final String name = "fluid_container";

    private FluidContainer() {
        super(1000, new ItemStack(EmptyFluidContainer.instance), false);
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(FluidPlacementTest.MODID, name);
        setUnlocalizedName(FluidPlacementTest.MODID + ":" + name);
    }

    @Nonnull
    @Override
    public String getItemStackDisplayName(@Nonnull ItemStack stack) {
        FluidStack fluid = getFluid(stack);
        if (fluid == null || fluid.getFluid() == null) {
            return "Empty Variable Container";
        }
        return "Variable Container (" + getFluid(stack).getLocalizedName() + ")";
    }

    @Override
    @Nullable
    public FluidStack getFluid(ItemStack container) {
        container = container.copy();
        if (container.getTagCompound() != null) {
            container.setTagCompound(container.getTagCompound().getCompoundTag(FLUID_NBT_KEY));
        }
        return super.getFluid(container);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World playerIn, List<String> tooltip, ITooltipFlag advanced) {
        FluidStack fluid = getFluid(stack);
        if (fluid != null) {
            tooltip.add(fluid.amount + "/1000");
        }
    }

    @Override
    public void getSubItems(@Nullable CreativeTabs tab, @Nonnull NonNullList<ItemStack> subItems) {
        if (!this.isInCreativeTab(tab))
            return;
        Fluid[] fluids = new Fluid[]{FluidRegistry.WATER, FluidRegistry.LAVA, FiniteFluid.instance, ModelFluidTest.FLUID};
        // add 16 variable fillings
        for (Fluid fluid : fluids) {
            for (int amount = 125; amount <= 1000; amount += 125) {
                for (int offset = 63; offset >= 0; offset -= 63) {
                    FluidStack fs = new FluidStack(fluid, amount - offset);
                    ItemStack stack = new ItemStack(this);
                    NBTTagCompound tag = stack.getTagCompound();
                    if (tag == null) {
                        tag = new NBTTagCompound();
                    }
                    NBTTagCompound fluidTag = new NBTTagCompound();
                    fs.writeToNBT(fluidTag);
                    tag.setTag(FLUID_NBT_KEY, fluidTag);
                    stack.setTagCompound(tag);
                    subItems.add(stack);
                }
            }
        }
    }

    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack, NBTTagCompound nbt) {
        return new FluidHandlerItemStack.SwapEmpty(stack, new ItemStack(EmptyFluidContainer.instance), 1000);
    }
}