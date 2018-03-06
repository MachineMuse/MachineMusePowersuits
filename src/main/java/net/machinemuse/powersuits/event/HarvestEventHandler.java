package net.machinemuse.powersuits.event;

import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarvestEventHandler {
    @SubscribeEvent
    public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer player = event.getEntityPlayer();
        IBlockState state = event.getTargetBlock();
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof ItemPowerFist && ((ItemPowerFist) stack.getItem()).canHarvestBlock(stack, state, player)) {
            event.setCanHarvest(true);
        }
    }

    @SubscribeEvent
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        // Note: here we can actually get the position if needed. we can't om the harvest check.
        IBlockState state = event.getState();
        EntityPlayer player = event.getEntityPlayer();
        if (event.getNewSpeed() < event.getOriginalSpeed())
            event.setNewSpeed(event.getOriginalSpeed());
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof IMuseItem) {
            // TODO: add a way to look for the actual tool required instead of looping through multiple checks.

            for (ItemStack module : ModuleManager.getInstance().getBlockBreakingModules()) {
                if (ModuleManager.getInstance().itemHasActiveModule(stack, module.getUnlocalizedName()) && ((IBlockBreakingModule)module.getItem()).canHarvestBlock(stack, state, player)) {
                    if (event.getNewSpeed() == 0) {
                        event.setNewSpeed(1);
                    }
                    ((IBlockBreakingModule)module.getItem()).handleBreakSpeed(event);
                }
            }
        }
    }
}