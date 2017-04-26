package net.machinemuse.powersuits.block;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/18/17.
 */
public class TestBlock extends Block {//BlockDirectional {
    static final String name = "testBlock";
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public TestBlock() {
        super(Material.CIRCUITS);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public boolean isFullyOpaque(IBlockState state) {
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing());
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }


    @SuppressWarnings("deprecation")
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
        return true;
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