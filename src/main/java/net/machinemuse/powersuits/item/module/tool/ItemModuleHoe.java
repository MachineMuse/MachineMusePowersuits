package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemModuleHoe extends ItemAbstractPowerModule implements IPowerModule, IRightClickModule {
    public ItemModuleHoe(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        addBasePropertyDouble(MPSModuleConstants.HOE_ENERGY_CONSUMPTION, 500, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.RADIUS, MPSModuleConstants.HOE_ENERGY_CONSUMPTION, 9500);
//        addTradeoffPropertyDouble(MPSModuleConstants.RADIUS, MPSModuleConstants.HOE_SEARCH_RADIUS, 8, "m");
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        double energyConsumed = getEnergyUsage(itemStack);
//        if (!playerIn.canPlayerEdit(pos, facing, itemStack) || ElectricItemUtils.getPlayerEnergy(playerIn) < energyConsumed) {
            return EnumActionResult.FAIL;
//        } else {
//            int hook = net.minecraftforge.event.ForgeEventFactory.onHoeUse(itemStack, playerIn, worldIn, pos);
//            if (hook != 0) return hook > 0 ? EnumActionResult.SUCCESS : EnumActionResult.FAIL;
//
//            double radius = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.HOE_SEARCH_RADIUS);
//            for (int i = (int) Math.floor(-radius); i < radius; i++) {
//                for (int j = (int) Math.floor(-radius); j < radius; j++) {
//                    if (i * i + j * j < radius * radius) {
//                        BlockPos newPos = pos.add(i, 0, j);
//                        IBlockState iblockstate = worldIn.getBlockState(newPos);
//                        Block block = iblockstate.getBlock();
//                        if (facing != EnumFacing.DOWN && (worldIn.isAirBlock(newPos.up()) || ToolHelpers.blockCheckAndHarvest(playerIn, worldIn, newPos.up()))) {
//                            if (block == Blocks.GRASS || block == Blocks.GRASS_PATH) {
//                                this.setBlock(itemStack, playerIn, worldIn, newPos, Blocks.FARMLAND.getDefaultState());
//                            }
//
//                            if (block == Blocks.DIRT) {
//                                switch (iblockstate.getValue(BlockDirt.VARIANT)) {
//                                    case DIRT:
//                                        this.setBlock(itemStack, playerIn, worldIn, newPos, Blocks.FARMLAND.getDefaultState());
//                                        break;
//                                    case COARSE_DIRT:
//                                        this.setBlock(itemStack, playerIn, worldIn, newPos, Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT));
//                                        break;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            return EnumActionResult.SUCCESS;
//        }
    }

    protected void setBlock(ItemStack stack, EntityPlayer player, World worldIn, BlockPos pos, IBlockState state) {
//        // TODO: Proper sound effect, maybe some particle effects like dirt particles flying around.
//        // note that the isRemote check was moved here because exiting with it seems to cancel sound
//        worldIn.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
//        if (!worldIn.isRemote) {
//            ElectricItemUtils.drainPlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.HOE_ENERGY_CONSUMPTION));
//            worldIn.setBlockState(pos, state, 11);
//        }
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.HOE_ENERGY_CONSUMPTION);
    }
}
