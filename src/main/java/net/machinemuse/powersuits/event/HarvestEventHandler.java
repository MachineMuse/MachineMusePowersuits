package net.machinemuse.powersuits.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class HarvestEventHandler {
    @SubscribeEvent
    public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer player = event.entityPlayer;
        Block block = event.block;
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof ItemPowerFist && ((ItemPowerFist) stack.getItem()).canHarvestBlock(stack, block, 0, player)) {
            event.success = true;
        }
    }

    @SubscribeEvent
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        Block block = event.block;
        EntityPlayer player = event.entityPlayer;
        int meta = event.metadata;
        event.newSpeed = event.originalSpeed;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() instanceof IModularItem) {
            for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
                if (ModuleManager.itemHasActiveModule(stack, module.getDataName()) && module.canHarvestBlock(stack, block, meta, player)) {
                    if (event.newSpeed == 0) {
                        event.newSpeed = 1;
                    }
                    module.handleBreakSpeed(event);
                }
            }
        }

    }
}
