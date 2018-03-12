package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.fluid.FluidLiquidNitrogen;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.awt.*;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

public class MPSFluid {
    private static MPSFluid INSTANCE;

    public static MPSFluid getInstance() {
        if (INSTANCE == null) synchronized (MPSFluid.class) {
            if (INSTANCE == null) INSTANCE = new MPSFluid();
        }
        return INSTANCE;
    }
    private MPSFluid() {}




    // Fluid :P
    public static Fluid liquidNitrogen = new FluidLiquidNitrogen("liquid_nitrogen",
            new ResourceLocation(MODID, "blocks/liquid_nitrogen_still"),
            new ResourceLocation(MODID, "blocks/liquid_nitrogen_flow"),
            new Color(11, 168, 252))
            .setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY)
            .setFillSound(SoundEvents.ITEM_BUCKET_FILL)
            .setTemperature(70) // temperature in Kelvin
            .setViscosity(157)
            .setGaseous(false);



    public static void registerFluids() {
        if (FluidRegistry.registerFluid(liquidNitrogen))
            FluidRegistry.addBucketForFluid(liquidNitrogen);
        else
            liquidNitrogen = FluidRegistry.getFluid("liquid_nitrogen");
        System.out.println("liquid nitrogen name: " + liquidNitrogen.getName());


    }
}
