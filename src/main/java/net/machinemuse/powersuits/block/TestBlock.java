package net.machinemuse.powersuits.block;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/18/17.
 */
public class TestBlock extends BlockDirectional {
    static final String name = "testBlock";

    public TestBlock() {
        super(Material.CIRCUITS);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
        // IMPORTANT: enabling default state with extended state like the line below causes model loading issues
//        setDefaultState(((IExtendedBlockState) blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN)).withProperty(COLOR, defaultColor));
        setCreativeTab(Config.getCreativeTab());
        setUnlocalizedName(name);
        setRegistryName(ModularPowersuits.MODID, "tile." + name);
        setHardness(0.05F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
        setLightOpacity(0);
        setLightLevel(1.0f);
        setTickRandomly(false);
        setHarvestLevel("pickaxe", 0);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    public boolean isFullyOpaque(IBlockState state) {
        return  state.getValue(FACING) == EnumFacing.UP || state.getValue(FACING) == EnumFacing.DOWN;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, (state.withProperty(FACING, getFacingFromEntity(pos, placer).getOpposite())), 2);
    }

    public static EnumFacing getFacingFromEntity(BlockPos pos, EntityLivingBase entityIn) {
        if (MathHelper.abs((float)entityIn.posX - (float)pos.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - (float)pos.getZ()) < 2.0F) {
            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();
            if (d0 - (double)pos.getY() > 2.0D)
                return EnumFacing.UP;

            if ((double)pos.getY() - d0 > 0.0D)
                return EnumFacing.DOWN;
        }
        return entityIn.getHorizontalFacing().getOpposite();
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return false;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}