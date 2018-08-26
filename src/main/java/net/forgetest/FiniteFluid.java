package net.forgetest;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FiniteFluid extends Fluid {
    public static final String name = "finitefluid";
    public static final FiniteFluid instance = new FiniteFluid();

    private FiniteFluid() {
        super(name, new ResourceLocation("blocks/water_still"), new ResourceLocation("blocks/water_flow"), new ResourceLocation("blocks/water_overlay"));
    }

    @Override
    public int getColor() {
        return 0xFFFFFF00;
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        return "Finite Fluid";
    }
}
