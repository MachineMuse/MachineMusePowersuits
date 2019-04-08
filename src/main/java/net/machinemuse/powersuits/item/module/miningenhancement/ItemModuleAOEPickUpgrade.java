package net.machinemuse.powersuits.item.module.miningenhancement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

/**
 * Created by Eximius88 on 1/29/14.
 */
public class ItemModuleAOEPickUpgrade extends ItemAbstractPowerModule implements IMiningEnhancementModule {
    public ItemModuleAOEPickUpgrade(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        //ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.diamond, 3));
//
//        addBasePropertyDouble(MPSModuleConstants.AOE_ENERGY_CONSUMPTION, 500, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.DIAMETER, MPSModuleConstants.AOE_ENERGY_CONSUMPTION, 9500);
//        addIntTradeoffProperty(MPSModuleConstants.DIAMETER, MPSModuleConstants.AOE_MINING_RADIUS, 5, "m", 2, 1);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemStack, BlockPos posIn, EntityPlayer player) {
//        if (player.world.isRemote)
//            return false; // fixme : check?
//
//        RayTraceResult rayTraceResult = MusePlayerUtils.raytraceBlocks(player.world, player, true, 10);
//        if (rayTraceResult == null)
//            return false;
//
//        int radius =  (int) (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.AOE_MINING_RADIUS) -1)/2 ;
//
//        if (radius == 0)
//            return false;
//
//        EnumFacing side = rayTraceResult.sideHit;
//        Iterable<BlockPos> posList;
//        switch(side) {
//            case UP:
//            case DOWN:
//                posList = BlockPos.getAllInBox(posIn.north(radius).west(radius), posIn.south(radius).east(radius));
//                break;
//
//            case EAST:
//            case WEST:
//                posList = BlockPos.getAllInBox(posIn.up(radius).north(radius), posIn.down(radius).south(radius));
//                break;
//
//            case NORTH:
//            case SOUTH:
//                posList = BlockPos.getAllInBox(posIn.up(radius).west(radius), posIn.down(radius).east(radius));
//                break;
//
//            default:
//                posList = new HashList<>();
//        }
//        int energyUsage = this.getEnergyUsage(itemStack);
//
//        boolean harvested = false;
//        for (BlockPos blockPos : posList) {
//            IBlockState state = player.world.getBlockState(blockPos);
//            Block block = state.getBlock();
//
//            for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
//                int playerEnergy = ElectricItemUtils.getPlayerEnergy(player);
//
//                if (ModuleManager.INSTANCE.itemHasActiveModule(itemStack, module.getDataName()) && ((IBlockBreakingModule) module).canHarvestBlock(itemStack, state, player, blockPos, playerEnergy - energyUsage)) {
//                    if (posIn == blockPos) // center block
//                        harvested = true;
//                    block.onPlayerDestroy(player.world, blockPos, state);
//                    player.world.playEvent(null, 2001, blockPos, Block.getStateId(state));
//                    player.world.setBlockToAir(blockPos);
//                    block.breakBlock(player.world, blockPos, state);
//                    block.dropBlockAsItem(player.world, blockPos, state, 0);
//
//                    ElectricItemUtils.drainPlayerEnergy(player, ((IBlockBreakingModule) module).getEnergyUsage(itemStack) + energyUsage);
//                    break;
//                }
//            }
//        }
//        return harvested;
        return false;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.AOE_ENERGY_CONSUMPTION);
    }
}