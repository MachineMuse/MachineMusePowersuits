package net.machinemuse.powersuits.event;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarvestEventHandler {
    @SubscribeEvent
    public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getTargetBlock();
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof ItemPowerFist && ((ItemPowerFist) stack.getItem()).canHarvestBlock(state, stack)) {
            event.setCanHarvest(true);
        }
    }

    @SubscribeEvent
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getState();
        BlockPos pos = event.getPos();
        event.setNewSpeed(event.getOriginalSpeed());
        ItemStack stack = player.getHeldItemMainhand();
        if (stack != null && stack.getItem() instanceof IModularItem) {
            for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
                if (ModuleManager.itemHasActiveModule(stack, module.getDataName()) && module.canHarvestBlock(stack, pos, state, player)) {
                    if (event.getNewSpeed() == 0) {
                        event.setNewSpeed(1);
                    }
                    module.handleBreakSpeed(event);
                }
            }
        }

    }
}
