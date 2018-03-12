package net.machinemuse.powersuits.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class FluidLiquidNitrogen extends Fluid {
    public FluidLiquidNitrogen(String fluidName, ResourceLocation still, ResourceLocation flowing, Color color) {
        super(fluidName, still, flowing, color);
    }

    @Override
    public boolean doesVaporize(FluidStack fluidStack) {
        return true;
    }


}
