package net.machinemuse.powersuits.common.block;

import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.tileentities.TileEntityTinkerTable;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;

public class BlockTinkerTable extends BlockContainer {
    public static final String name = "tinkertable";
    private static BlockTinkerTable ourInstance;
    public static final PropertyDirection FACING = BlockHorizontal.FACING;

    public static BlockTinkerTable getInstance() {
        if (ourInstance == null)
            ourInstance = new BlockTinkerTable();
        return ourInstance;
    }

    private BlockTinkerTable() {
        super(Material.IRON);
        setSoundType(SoundType.METAL);
        setCreativeTab(MPSSettings.getCreativeTab());
        setHardness(1.5F);
        setResistance(1000.0F);
        setHarvestLevel("pickaxe", 2);
        setLightOpacity(0);
        setLightLevel(0.4f);
        setTickRandomly(false);
        setUnlocalizedName(MPSConstants.RESOURCE_PREFIX + name);
        setRegistryName(MODID, name);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityTinkerTable();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (playerIn.isSneaking())
            return false;
        if (worldIn.isRemote)
             playerIn.openGui(ModularPowersuits.getInstance(), 0, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullBlock(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityTinkerTable(state.getValue(FACING));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {FACING});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }
}