package net.machinemuse.powersuits.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;

public class FluidLiquidNitrogen extends Fluid {
    public FluidLiquidNitrogen(String fluidName, ResourceLocation still, ResourceLocation flowing, Color color) {
        super(fluidName, still, flowing, color);
    }

    public FluidLiquidNitrogen(String fluidName, ResourceLocation still, ResourceLocation flowing, int color) {
        super(fluidName, still, flowing, color);
    }

    public FluidLiquidNitrogen(String fluidName, ResourceLocation still, ResourceLocation flowing) {
        super(fluidName, still, flowing);

        FluidRegistry.registerFluid(this);
    }
}
