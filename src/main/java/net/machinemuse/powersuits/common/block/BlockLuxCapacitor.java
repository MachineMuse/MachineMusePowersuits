package net.machinemuse.powersuits.common.block;

import net.machinemuse.powersuits.client.helpers.EnumColour;
import net.machinemuse.powersuits.common.config.MPSSettings;
import net.machinemuse.powersuits.common.tileentities.TileEntityLuxCapacitor;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Random;

import static net.machinemuse.powersuits.common.MPSConstants.MODID;

public class BlockLuxCapacitor extends BlockDirectional {
    protected static final AxisAlignedBB LUXCAPACITOR_EAST_AABB = new AxisAlignedBB(0.75, 0.0625, 0.0625, 1.0, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_WEST_AABB = new AxisAlignedBB(0.0, 0.0625, 0.0625, 0.25, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_SOUTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.75, 0.9375, 0.9375, 1.0);
    protected static final AxisAlignedBB LUXCAPACITOR_NORTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.0, 0.9375, 0.9375, 0.25);
    protected static final AxisAlignedBB LUXCAPACITOR_UP_AABB = new AxisAlignedBB(0.0625, 0.75, 0.0625, 0.9375, 1.0, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_DOWN_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.25, 0.9375);


    //TODO: move direction to tileentity


    public static final EnumColour defaultColor = EnumColour.BLUEVIOLET;

    public static final IUnlistedProperty<EnumColour> COLOUR = new IUnlistedProperty<EnumColour>() {
        @Override
        public String getName() {
            return "luxCapColor";
        }

        @Override
        public boolean isValid(EnumColour value) {
            return value != null;
        }

        @Override
        public Class<EnumColour> getType() {
            return EnumColour.class;
        }

        @Override
        public String valueToString(EnumColour value) {
            return (value != null) ? value.toString() :  defaultColor.toString();
        }
    };

    public static final String name = "luxcapacitor";
    private static BlockLuxCapacitor ourInstance;
    public static BlockLuxCapacitor getInstance() {
        if (ourInstance == null)
            ourInstance = new BlockLuxCapacitor();
        return ourInstance;
    }

    protected BlockLuxCapacitor() {
        super(Material.CIRCUITS);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        // IMPORTANT: enabling default state with extended state like the line below causes model loading issues
        //        setDefaultState(((IExtendedBlockState) blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN)).withProperty(COLOUR, defaultColor));
        setCreativeTab(MPSSettings.getCreativeTab());
        setUnlocalizedName(name);
        setRegistryName(MODID, "tile." + name);
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

    @SuppressWarnings("deprecation")
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false; //state.getValue(FACING) == EnumFacing.UP || state.getValue(FACING) == EnumFacing.DOWN;
    }

    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        worldIn.setBlockState(pos, ((IExtendedBlockState)state.withProperty(FACING, getFacingFromEntity(pos, placer).getOpposite())).withProperty(COLOUR, defaultColor), 2);
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
        return new ExtendedBlockState(this, new IProperty[] { FACING  }, new IUnlistedProperty[]{COLOUR});
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityLuxCapacitor && state instanceof IExtendedBlockState) {
            return ((IExtendedBlockState) state).withProperty(COLOUR, ((TileEntityLuxCapacitor) te).getColor());
        }
        return state;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
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
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        if (state instanceof IExtendedBlockState)
            return new TileEntityLuxCapacitor(((IExtendedBlockState)state).getValue(COLOUR));
        return new TileEntityLuxCapacitor();
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}
