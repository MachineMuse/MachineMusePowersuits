package net.machinemuse.powersuits.block;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockLuxCapacitor extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");




    public static int assignedBlockID;
    protected int renderType;

//    TODO: these will be static bounding boxes instead of calculating them over and over (numbers still neeed to be filled in)
//    protected static final AxisAlignedBB SOUTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
//    protected static final AxisAlignedBB NORTH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB WEST_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB EAST_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
//    protected static final AxisAlignedBB UP_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
//    protected static final AxisAlignedBB DOWN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);



    public BlockLuxCapacitor setRenderType(int id) {
        this.renderType = id;
        return this;
    }

    public BlockLuxCapacitor() {
        super(Material.CIRCUITS);

        // Block's creative tab
        setCreativeTab(Config.getCreativeTab());

        // Block's hardness (base time to harvest it with the correct tool).
        // Sand = 0.5, Stone = 1.5, Ore = 3.0 Obsidian = 20
        setHardness(0.05F);

        // Block's resistance to explosions. Stone = 10, obsidian = 2000
        setResistance(10.0F);

        // Sound to play when player steps on the block
        setSoundType(SoundType.METAL);

        // How much light is stopped by this block; 0 for air, 255 for fully
        // opaque.
        setLightOpacity(0);

        // Light level, 0-1. Gets multiplied by 15 and truncated to find the
        // actual light level for the block.
        setLightLevel(1.0f);

        // Whether to receive random ticks e.g. plants
        setTickRandomly(false);

        // Harvest level for this block. par2 can be pickaxe, axe, or shovel, or
        // a different toolclass. par3 is the minimum level of item required to
        // break it:
        // 0=bare hands, 1=wood, 2=stone, 3=iron, 4=diamond
//        MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);

        GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, "luxCapacitor");

        setUnlocalizedName("luxCapacitor");

    }



//    public static final IProperty FACING = PropertyDirection.create("facing");
//    @Override
//    protected BlockStateContainer createBlockState() {
//        return new IBlockState(this, FACING);
//    }

    //
//    protected BlockState createBlockState()
//    {
//
//    }


//    private static float bbMin(double offset) {
//        return (offset > 0 ? 13 : offset < 0 ? 0 : 1) / 16.0f;
//    }
//
//    private static float bbMax(double offset) {
//        return (offset > 0 ? 16 : offset < 0 ? 3 : 15) / 16.0f;
//    }
//
//
//    public static AxisAlignedBB createAABBForSide(EnumFacing dir, BlockPos blockPos) {
//        double x1 = bbMin(dir.getFrontOffsetX());
//        double y1 = bbMin(dir.getFrontOffsetY());
//        double z1 = bbMin(dir.getFrontOffsetZ());
//        double x2 = bbMax(dir.getFrontOffsetX());
//        double y2 = bbMax(dir.getFrontOffsetY());
//        double z2 = bbMax(dir.getFrontOffsetZ());
//        return new AxisAlignedBB(blockPos.getX() + x1, blockPos.getY() + y1, blockPos.getZ() + z1,
//                blockPos.getX() + x2, blockPos.getY() + y2, blockPos.getZ() + z2);
//    }

//    @SideOnly(Side.CLIENT)
//    @Override
//    public void registerBlockIcons(IIconRegister iconRegister) {
//        this.blockIcon = iconRegister.registerIcon(MuseIcon.ICON_PREFIX + "bluelight");
//    }

    /**
     * returns some value from 0 to 30 or so for different models. Since we're
     * using a custom renderer, we pass in a completely different ID: the
     * assigned block ID. It won't conflict with other mods, since Forge looks
     * it up in a table anyway, but it's still best to have different internal
     * IDs.
     */
//    @Override
//    public int getRenderType() {
//        return renderType;
//    }
//
//    @Override
//    public boolean renderAsNormalBlock() {
//        return false;
//    }
//
//    @Override
//    public boolean isOpaqueCube() {
//        return false;
//    }




    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileEntityLuxCapacitor();
    }
    @SideOnly(Side.CLIENT)

//    /**
//     * Returns the bounding box of the wired rectangular prism to render.
//     */
//    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z) {
//        BlockPos blockPos = new BlockPos(x, y, z);
//
//        TileEntity te = world.getTileEntity(blockPos);
//        if (te instanceof TileEntityLuxCapacitor) {
//            EnumFacing side = ((TileEntityLuxCapacitor) te).side;
//            return createAABBForSide(side, blockPos);
//        }
//        return null;
//    }

//    /**
//     * Returns a bounding box from the pool of bounding boxes (this means this box can change after the pool has been
//     * cleared to be reused)
//     */
//    public AxisAlignedBB getCollisionBoundingBoxFromPool(World world, BlockPos blockPos) {
//        TileEntity te = world.getTileEntity(blockPos);
//        if (te instanceof TileEntityLuxCapacitor) {
//            EnumFacing side = ((TileEntityLuxCapacitor) te).side;
//            return createAABBForSide(side, blockPos);
//        }
//        return null;
//    }

//    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, BlockPos blockPos) {
//        TileEntity te = par1IBlockAccess.getTileEntity(blockPos);
//        if (te instanceof TileEntityLuxCapacitor) {
//            EnumFacing side = ((TileEntityLuxCapacitor) te).side;
//            float x1 = bbMin(side.getFrontOffsetX());
//            float y1 = bbMin(side.getFrontOffsetY());
//            float z1 = bbMin(side.getFrontOffsetZ());
//            float x2 = bbMax(side.getFrontOffsetX());
//            float y2 = bbMax(side.getFrontOffsetY());
//            float z2 = bbMax(side.getFrontOffsetZ());
//            this.setBlockBounds(x1, y1, z1, x2, y2, z2);
//        }
//    }

//    @Override
//    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
//        return super.getBoundingBox(state, source, pos);
//    }

    /**
     * Returns the quantity of items to drop on block destruction.
     */
    public int quantityDropped(Random par1Random) {
        return 0;
    }

}
