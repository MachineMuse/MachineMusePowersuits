package net.machinemuse.powersuits.block;


import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.client.render.model.UnlistedPropertyColor;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

import java.util.Random;

public class BlockLuxCapacitor extends BlockDirectional {
    protected static final AxisAlignedBB LUXCAPACITOR_EAST_AABB = new AxisAlignedBB(0.75, 0.0625, 0.0625, 1.0, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_WEST_AABB = new AxisAlignedBB(0.0, 0.0625, 0.0625, 0.25, 0.9375, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_SOUTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.75, 0.9375, 0.9375, 1.0);
    protected static final AxisAlignedBB LUXCAPACITOR_NORTH_AABB = new AxisAlignedBB(0.0625, 0.0625, 0.0, 0.9375, 0.9375, 0.25);
    protected static final AxisAlignedBB LUXCAPACITOR_UP_AABB = new AxisAlignedBB(0.0625, 0.75, 0.0625, 0.9375, 1.0, 0.9375);
    protected static final AxisAlignedBB LUXCAPACITOR_DOWN_AABB = new AxisAlignedBB(0.0625, 0.0, 0.0625, 0.9375, 0.25, 0.9375);

    public static UnlistedPropertyColor COLOR = new UnlistedPropertyColor();

//    public static final IUnlistedProperty<Integer> JAI = new IUnlistedProperty<Integer>();

    public static final String name = "luxCapacitor";

    public BlockLuxCapacitor() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.DOWN));
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
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        switch (state.getValue(FACING))
        {
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

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * Checks if an IBlockState represents a block that is opaque and a full cube.
     */
    public boolean isFullyOpaque(IBlockState state)
    {
        return  state.getValue(FACING) == EnumFacing.UP || state.getValue(FACING) == EnumFacing.DOWN;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, getFacingFromEntity(pos, placer).getOpposite());
    }

    /**
     * Called by ItemBlocks after a block is set in the world, to allow post-place logic. In this case, we use it
     * to set the rotation for the TileEntity which controls the rotation of the lens model. // TODO: fix lux capacitor model.
     */
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer).getOpposite()), 2);

        TileEntity tileentity = worldIn.getTileEntity(pos);

        if (tileentity instanceof TileEntityLuxCapacitor)
        {
            ((TileEntityLuxCapacitor)tileentity).setFacing(state.getValue(FACING));
        }
    }

    public static EnumFacing getFacingFromEntity(BlockPos pos, EntityLivingBase entityIn) {
        if (MathHelper.abs((float)entityIn.posX - (float)pos.getX()) < 2.0F && MathHelper.abs((float)entityIn.posZ - (float)pos.getZ()) < 2.0F) {
            double d0 = entityIn.posY + (double)entityIn.getEyeHeight();
            if (d0 - (double)pos.getY() > 2.0D) return EnumFacing.UP;

            if ((double)pos.getY() - d0 > 0.0D)
                return EnumFacing.DOWN;
        }
        return entityIn.getHorizontalFacing().getOpposite();
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getFront(meta));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getIndex();
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public BlockStateContainer createBlockState()
    {
//        return new BlockStateContainer(this, FACING);
        return new ExtendedBlockState(this, new IProperty[] { FACING  }, new IUnlistedProperty[]{ COLOR });

    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
        System.out.println("actually checking for extended state");

        TileEntity te = world.getTileEntity(pos);

        if (te != null)
            System.out.println("color is " + ((TileEntityLuxCapacitor)te).getColour().hexColour());
        else
            System.out.println("tile entity is null");


//        if (te instanceof TileEntityLuxCapacitor && state instanceof IExtendedBlockState) {
            return ((IExtendedBlockState)state).withProperty(COLOR, ((TileEntityLuxCapacitor)te).getColour());
//        }
//        return state;
    }

    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }


    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityLuxCapacitor();
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
//        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED; // this is for using a TESR only
        return EnumBlockRenderType.MODEL; // This is for using a model or a model and TESR
    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}
