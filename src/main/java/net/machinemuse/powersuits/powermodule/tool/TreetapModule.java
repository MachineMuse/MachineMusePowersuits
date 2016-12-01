package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.electricity.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 7:45 PM 4/23/13
 *
 * Updated by leon on 6/14/16.
 */

public class TreetapModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_TREETAP = "Treetap";
    public static final String TREETAP_ENERGY_CONSUMPTION = "Energy Consumption";
    public static final ItemStack resin = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "misc_resource")), 1, 4);
    public static final Block rubber_wood =  Block.REGISTRY.getObject(new ResourceLocation("ic2", "rubber_wood"));
    public static final ItemStack electricTreetap = new ItemStack( Item.REGISTRY.getObject(new ResourceLocation("ic2", "electric_treetap")), 1);

    public TreetapModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(TREETAP_ENERGY_CONSUMPTION, 100);
        addInstallCost(electricTreetap);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        if (block == rubber_wood) {
            tryExtract(player, world, pos, facing, state, null);
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, TREETAP_ENERGY_CONSUMPTION));
        }
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return null;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    public static boolean tryExtract(EntityPlayer player, World world, BlockPos pos, EnumFacing side, IBlockState state, List stacks) {
        //TODO: finish


//        Block block = state.getBlock();
//        state.getPropertyNames().
//                getValue(RubberWoodState)
//
//        state.getBlock().getExtendedState(state, world, pos);
//
//
//
//
//
//        BlockRubWood.RubberWoodState rwState = (BlockRubWood.RubberWoodState)state.getValue(BlockRubWood.stateProperty);
//        if(!rwState.isPlain() && rwState.facing == side) {
//            if(rwState.wet) {
//                if(!world.isRemote) {
//                    world.setBlockState(pos, state.withProperty(BlockRubWood.stateProperty, rwState.getDry()));
//                    if(stacks != null) {
//                        stacks.add(StackUtil.copyWithSize(ItemName.misc_resource.getItemStack((Enum)MiscResourceType.resin), world.rand.nextInt(3) + 1));
//                    } else {
//                        eject(world, pos, side, world.rand.nextInt(3) + 1);
//                    }
//
////                    if(player != null) {
////                        IC2.achievements.issueAchievement(player, "acquireResin");
////                    }
//                }
//
////                if(world.isRemote && player != null) {
////                    IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/Treetap.ogg", true, IC2.audioManager.getDefaultVolume());
////                }
//
//                return true;
//            } else {
//                if(!world.isRemote && world.rand.nextInt(5) == 0) {
//                    world.setBlockState(pos, state.withProperty(BlockRubWood.stateProperty, BlockRubWood.RubberWoodState.plain_y));
//                }
//
//                if(world.rand.nextInt(5) == 0) {
//                    if(!world.isRemote) {
//                        ejectResin(world, pos, side, 1);
//                        if(stacks != null) {
//                            stacks.add(ItemName.misc_resource.getItemStack((Enum)MiscResourceType.resin));
//                        } else {
//                            eject(world, pos, side, 1);
//                        }
//                    }
////                    if(world.isRemote && player != null) {
////                        IC2.audioManager.playOnce(player, PositionSpec.Hand, "Tools/Treetap.ogg", true, IC2.audioManager.getDefaultVolume());
////                    }
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        } else {
//            return false;
//        }
//








//    int metadata = world.getBlockMetadata(x, y, z);
//    if ((metadata < 2) || (metadata % 6 != side)) {
//        return false;
//    }
//    if (metadata < 6) {
//        if (AddonUtils.isServerSide()) {
//            world.setBlockMetadataWithNotify(x, y, z, metadata + 6, 7);
//            if (stacks != null) {
//                ItemStack tempResin = resin.copy();
//                tempResin.stackSize = world.rand.nextInt(3);
//                stacks.add(tempResin);
//            } else {
//                eject(world, x, y, z, side, world.rand.nextInt(3) + 1);
//            }
//            world.scheduleBlockUpdate(x, y, z, rubber.itemID, Block.blocksList[rubber.itemID].tickRate(world));
//        }
//    }
//    if ((world.rand.nextInt(5) == 0) && (AddonUtils.isServerSide())) {
//        world.setBlockMetadataWithNotify(x, y, z, 1, 7);
//    }
//    if (world.rand.nextInt(5) == 0) {
//        if (AddonUtils.isServerSide()) {
//            eject(world, x, y, z, side, 1);
//            if (stacks != null) {
//                ItemStack tempResin = resin.copy();
//                tempResin.stackSize = world.rand.nextInt(3);
//                stacks.add(tempResin);
//            } else {
//                eject(world, x, y, z, side, 1);
//            }
//        }
//        return true;
//    }
//    return false;
//}


        return false;
    }


//    @Override
//    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
//        return false;
//    }

//    @Override
//    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
//
//    }

//    @Override
//    public EnumAction getItemUseAction(ItemStack stack) {
//        return EnumAction.BOW;
//    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }
//    @Override
//    public String getTextureFile() {
//        return "treetap";
//    }

    @Override
    public String getDataName() {
        return MODULE_TREETAP;
    }

    @Override
    public String getUnlocalizedName() {
        return "treetap";
    }

    @Override
    public String getDescription() {
        return "An IC2 treetap integrated in your power tool.";
    }













//    public ItemTreetapElectric() {
//        super(ItemName.electric_treetap, 50);
//        this.maxCharge = 10000;
//        this.transferLimit = 100;
//        this.tier = 1;
//    }

//    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
//        IBlockState state = world.getBlockState(pos);
//        Block block = state.getBlock();
//        if(block == BlockName.rubber_wood.getInstance() && ElectricItem.manager.canUse(stack, this.operationEnergyCost)) {
//            if(ItemTreetap.attemptExtract(player, world, pos, side, state, (List)null)) {
//                ElectricItem.manager.use(stack, this.operationEnergyCost, player);
//                return true;
//            } else {
//                return super.onItemUse(stack, player, world, pos, side, hitX, hitY, hitZ);
//            }
//        } else {
//            return false;
//        }
//    }

//
//
//
//
//
//
//
//
//
//
//
//
//
//
//



//    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float xOffset, float yOffset, float zOffset) {
//        IBlockState state = world.getBlockState(pos);
//        Block block = state.getBlock();
//        if(block == BlockName.rubber_wood.getInstance()) {
//            attemptExtract(player, world, pos, side, state, (List)null);
//            if(!world.isRemote) {
//                stack.damageItem(1, player);
//            }
//
//            return true;
//        } else {
//            return false;
//        }
//    }
//


    public static void eject(World world, BlockPos pos, EnumFacing side, int quantity) {
        double ejectX = (double)pos.getX() + 0.5D + (double)side.getFrontOffsetX() * 0.3D;
        double ejectY = (double)pos.getY() + 0.5D + (double)side.getFrontOffsetY() * 0.3D;
        double ejectZ = (double)pos.getZ() + 0.5D + (double)side.getFrontOffsetZ() * 0.3D;

        for(int i = 0; i < quantity; ++i) {
            EntityItem item = new EntityItem(world, ejectX, ejectY, ejectZ, resin.copy());
            item.setDefaultPickupDelay();
            world.spawnEntityInWorld(item);
        }
    }
}