package net.machinemuse.powersuits.block;


import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.Random;

public class BlockLuxCapacitor extends BlockDirectional {
    public static final Colour defaultColor = new Colour(0.4D, 0.2D, 0.9D);
    public static final IUnlistedProperty<Colour> COLOR = new IUnlistedProperty<Colour>() {
        @Override
        public String getName() {
            return "luxCapColor";
        }

        @Override
        public boolean isValid(Colour value) {
            return value != null;
        }

        @Override
        public Class<Colour> getType() {
            return Colour.class;
        }

        @Override
        public String valueToString(Colour value) {
            return (value != null) ? value.hexColour() : defaultColor.hexColour();
        }
    };

    protected static final AxisAlignedBB LUXCAPACITOR_EAST_AABB = new AxisAlignedBB(0.75, 0.0625, 0.0625, 1.0, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_WEST_AABB = new AxisAlignedBB(0.0, 0.0625, 0.0625, 0.25, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_SOUTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.75, 0.9375, 0.9375, 1.0);
    protected static final AxisAlignedBB LUXCAPACITOR_NORTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.0, 0.9375, 0.9375, 0.25);
    protected static final AxisAlignedBB LUXCAPACITOR_UP_AABB = new AxisAlignedBB(0.0625, 0.75, 0.0625, 0.9375, 1.0, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_DOWN_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.25, 0.9375);

    public static final String translationKey = new StringBuilder(MPSModConstants.MODID).append(".").append("luxCapacitor").toString();

    public BlockLuxCapacitor(ResourceLocation regName) {
        super(Material.CIRCUITS);
        setRegistryName(regName);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
        setTranslationKey(translationKey);
        setHardness(0.05F);
        setResistance(10.0F);
        setSoundType(SoundType.METAL);
        setLightOpacity(0);
        setLightLevel(1.0f);
        setTickRandomly(false);
        setHarvestLevel("pickaxe", 0);
        GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, regName);
    }

    @SuppressWarnings("deprecation")
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            default:
            case DOWN:
                return LUXCAPACITOR_DOWN_AABB;
            case UP:
                return LUXCAPACITOR_UP_AABB;
            case NORTH:
                return LUXCAPACITOR_NORTH_AABB;
            case SOUTH:
                return LUXCAPACITOR_SOUTH_AABB;
            case WEST:
                return LUXCAPACITOR_WEST_AABB;
            case EAST:
                return LUXCAPACITOR_EAST_AABB;
        }
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        if (this.canPlaceAt(worldIn, pos, facing)) {
            return ((IExtendedBlockState) this.getDefaultState().withProperty(FACING, facing)).withProperty(COLOR, defaultColor);
        } else {
            for (EnumFacing enumfacing : EnumFacing.VALUES) {
                if (enumfacing == facing)
                    continue;
                if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                    return ((IExtendedBlockState) this.getDefaultState().withProperty(FACING, enumfacing)).withProperty(COLOR, defaultColor);
                }
            }
            return ((IExtendedBlockState) this.getDefaultState()).withProperty(COLOR, defaultColor);
        }
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new ExtendedBlockState(this, new IProperty[]{FACING}, new IUnlistedProperty[]{COLOR});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLuxCapacitor && state instanceof IExtendedBlockState)
            return ((IExtendedBlockState) state).withProperty(COLOR, ((TileEntityLuxCapacitor) te).getColor());
        return state;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return true;
            }
        }
        return false;
    }

    public boolean canPlaceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        if (!worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos))
            return false;
        BlockPos blockpos = pos.offset(facing);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        BlockFaceShape blockfaceshape = iblockstate.getBlockFaceShape(worldIn, blockpos, facing);
        return iblockstate.isSideSolid(worldIn, pos, facing) && blockfaceshape == BlockFaceShape.SOLID;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @SuppressWarnings("deprecation")
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byIndex(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (state instanceof IExtendedBlockState)
            return new TileEntityLuxCapacitor(((IExtendedBlockState) state).getValue(COLOR));
        return new TileEntityLuxCapacitor();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}