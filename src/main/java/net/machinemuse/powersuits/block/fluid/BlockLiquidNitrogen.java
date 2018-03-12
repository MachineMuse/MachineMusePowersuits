package net.machinemuse.powersuits.block.fluid;

import net.machinemuse.powersuits.common.MPSFluid;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;

import static net.machinemuse.powersuits.api.constants.MPSModConstants.MODID;

public class BlockLiquidNitrogen extends BlockFluidClassic {
    public static final String name = "liquid_nitrogen";

    public BlockLiquidNitrogen() {
        super(MPSFluid.liquidNitrogen, Material.WATER);
        setCreativeTab(MPSConfig.getInstance().getCreativeTab());
        setRegistryName(MODID, name);
        setUnlocalizedName(name);
    }

    @Override
    public int place(World world, BlockPos pos, @Nonnull FluidStack fluidStack, boolean doPlace) {
        System.out.println("fluidStack: " + fluidStack.getLocalizedName());
        System.out.println("do Place: " + doPlace);




        return super.place(world, pos, fluidStack, doPlace);
    }

    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
        super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);
    }
}
