package net.machinemuse.powersuits.block;

import net.machinemuse.numina.math.Colour;
import net.machinemuse.powersuits.tileentity.TileEntityLuxCapacitor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import javax.annotation.Nullable;
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

    protected static final VoxelShape LUXCAPACITOR_EAST_AABB = Block.makeCuboidShape(12, 1, 1, 16, 15, 15);
    protected static final VoxelShape LUXCAPACITOR_WEST_AABB = Block.makeCuboidShape(0, 1, 1, 4, 15, 15);
    protected static final VoxelShape LUXCAPACITOR_SOUTH_AABB = Block.makeCuboidShape(1, 1, 12, 15, 15, 16);
    protected static final VoxelShape LUXCAPACITOR_NORTH_AABB = Block.makeCuboidShape(1, 1, 0.0, 15, 15, 4);
    protected static final VoxelShape LUXCAPACITOR_UP_AABB = Block.makeCuboidShape(1, 12, 1, 15, 16.0, 15);
    protected static final VoxelShape LUXCAPACITOR_DOWN_AABB = Block.makeCuboidShape(1, 0.0, 1, 15, 4, 15);

    public BlockLuxCapacitor(String regName) {
        super(Block.Properties.create(Material.CIRCUITS)
                .hardnessAndResistance(0.05F, 10.0F)
                .sound(SoundType.METAL)
                .variableOpacity()
                .lightValue(1));
        setRegistryName(regName);
        setDefaultState(this.stateContainer.getBaseState().with(FACING, EnumFacing.DOWN));
    }

    @Override
    public int getHarvestLevel(IBlockState blockState) {
        return 0;
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(IBlockState p_getHarvestTool_1_) {
        return ToolType.PICKAXE;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockReader world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLuxCapacitor && state instanceof IExtendedBlockState)
            return ((IExtendedBlockState) state).withProperty(COLOR, ((TileEntityLuxCapacitor) te).getColor());
        return state;
    }

    @Override
    public int getItemsToDropCount(IBlockState state, int fortune, World worldIn, BlockPos pos, Random random) {
        return 0;
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection());
    }

    @SuppressWarnings( "deprecation" )
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        switch (state.get(FACING)) {
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
    protected void fillStateContainer(StateContainer.Builder<Block, IBlockState> stateContainer) {
        stateContainer.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        if (state instanceof IExtendedBlockState)
            return new TileEntityLuxCapacitor(((IExtendedBlockState) state).getValue(COLOR));
        return new TileEntityLuxCapacitor();
    }

    @SuppressWarnings("deprecation")
    @Override
    public BlockFaceShape getBlockFaceShape(IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
}
