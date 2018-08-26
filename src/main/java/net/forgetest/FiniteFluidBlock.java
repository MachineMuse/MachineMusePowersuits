package net.forgetest;

import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fluids.BlockFluidFinite;

public class FiniteFluidBlock extends BlockFluidFinite {
    public static final FiniteFluidBlock instance = new FiniteFluidBlock();
    public static final String name = "finite_fluid_block";

    private FiniteFluidBlock() {
        super(FiniteFluid.instance, Material.WATER);
        setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        setUnlocalizedName(FluidPlacementTest.MODID + ":" + name);
        setRegistryName(FluidPlacementTest.MODID, name);
    }
}
