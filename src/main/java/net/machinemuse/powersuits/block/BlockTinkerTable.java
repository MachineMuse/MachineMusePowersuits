package net.machinemuse.powersuits.block;

import net.machinemuse.powersuits.api.constants.MPSModConstants;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    public static final String translationKey = new StringBuilder(MPSModConstants.MODID).append(".").append("tinkerTable").toString();

    public BlockTinkerTable(ResourceLocation regName) {
        super(Material.IRON);
        setRegistryName(regName);
        setTranslationKey(translationKey);
        setHardness(1.5F);
        setResistance(1000.0F);
        setHarvestLevel("pickaxe", 2);
        setCreativeTab(MPSConfig.INSTANCE.mpsCreativeTab);
        setSoundType(SoundType.METAL);
        setLightOpacity(0);
        setLightLevel(0.4f);
        setTickRandomly(false);
        setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, regName);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.byHorizontalIndex(meta));
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
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getHorizontalIndex();
    }
}