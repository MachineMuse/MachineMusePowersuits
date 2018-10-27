package net.machinemuse.powersuits.powermodule.mining_enhancement;

import mekanism.common.HashList;
import net.machinemuse.numina.api.module.*;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import javax.annotation.Nonnull;

/**
 * Created by Eximius88 on 1/29/14.
 */
public class AOEPickUpgradeModule extends PowerModuleBase implements IToggleableModule, IMiningEnhancementModule {
    public AOEPickUpgradeModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        //ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.diamond, 3));
        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 100, "RF");
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos posIn, EntityPlayer player) {
        if (player.world.isRemote)
            return false; // fixme : check?

        RayTraceResult rayTraceResult = MusePlayerUtils.raytraceBlocks(player.world, player, true, 10);
        if (rayTraceResult == null)
            return false;

        EnumFacing side = rayTraceResult.sideHit;

        Iterable<BlockPos> posList;

        switch(side) {
            case UP:
            case DOWN:
                posList = BlockPos.getAllInBox(posIn.north().west(), posIn.south().east());
                break;

            case EAST:
            case WEST:
                posList = BlockPos.getAllInBox(posIn.up().north(), posIn.down().south());
                break;

            case NORTH:
            case SOUTH:
                posList = BlockPos.getAllInBox(posIn.up().west(), posIn.down().east());
                break;

            default:
                posList = new HashList<>();
        }
        int energyUsage = this.getEnergyUsage(itemstack);

        boolean harvested = false;
        for (BlockPos blockPos : posList) {
            IBlockState state = player.world.getBlockState(blockPos);
            Block block = state.getBlock();

            for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
                int playerEnergy = ElectricItemUtils.getPlayerEnergy(player);

                if (ModuleManager.INSTANCE.itemHasActiveModule(itemstack, module.getDataName()) && ((IBlockBreakingModule) module).canHarvestBlock(itemstack, state, player, blockPos, playerEnergy - energyUsage)) {
                    if (posIn == blockPos) // center block
                        harvested = true;
                    block.onBlockDestroyedByPlayer(player.world, blockPos, state);
                    player.world.playEvent(null, 2001, blockPos, Block.getStateId(state));
                    player.world.setBlockToAir(blockPos);
                    block.breakBlock(player.world, blockPos, state);
                    block.dropBlockAsItem(player.world, blockPos, state, 0);

                    ElectricItemUtils.drainPlayerEnergy(player, ((IBlockBreakingModule) module).getEnergyUsage(itemstack) + energyUsage);
                    break;
                }
            }
        }
        return harvested;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_AOE_PICK_UPGRADE__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aoePickUpgrade;
    }
}