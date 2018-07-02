package net.machinemuse.powersuits.block;

import com.google.common.base.Predicate;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMaterialMatcher;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateMatcher;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 *
 * @author MachineMuse
 * <p>
 * <p>
 * Ported to Java by lehjr on 10/21/16.
 */
public class BlockTinkerTable extends BlockHorizontal {
    public static final String name = "tinkertable";

    private volatile static BlockTinkerTable INSTANCE;
    public static BlockTinkerTable getInstance() {
        if (INSTANCE == null) {
            synchronized (BlockTinkerTable.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BlockTinkerTable();
                }
            }
        }
        return INSTANCE;
    }

    private BlockTinkerTable() {
        super(Material.IRON);
        setHardness(1.5F);
        setResistance(1000.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(Config.getCreativeTab());
        setSoundType(SoundType.METAL);
        setLightOpacity(0);
        setLightLevel(0.4f);
        setTickRandomly(false);
        setUnlocalizedName(name);
        setRegistryName(ModularPowersuits.MODID, "tile." + name);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, name);
    }

//    @Override
//    public EnumBlockRenderType getRenderType(IBlockState state) {
//        return EnumBlockRenderType.MODEL;
//    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
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
        return state.getValue(FACING).getHorizontalIndex();
    }
}