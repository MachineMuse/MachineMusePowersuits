package net.machinemuse.powersuits.event;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.powersuits.item.ItemPowerFist;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class HarvestEventHandler {
    @ForgeSubscribe
    public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer player = event.entityPlayer;
        Block block = event.block;
        ItemStack stack = player.inventory.getCurrentItem();
        if (stack != null && stack.getItem() instanceof ItemPowerFist && ((ItemPowerFist) stack.getItem()).canHarvestBlock(stack, block, 0, player)) {
            event.success = true;
        }
    }

    @ForgeSubscribe
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        Block block = event.block;
        EntityPlayer player = event.entityPlayer;
        int meta = event.metadata;
        event.newSpeed = event.originalSpeed;
        ItemStack stack = player.getCurrentEquippedItem();
        if (stack != null && stack.getItem() instanceof IModularItem) {
            for (IBlockBreakingModule module : ModuleManager.getBlockBreakingModules()) {
                if (MuseItemUtils.itemHasActiveModule(stack, module.getName()) && module.canHarvestBlock(stack, block, meta, player)) {
                    if (event.newSpeed == 0) {
                        event.newSpeed = 1;
                    }
                    module.handleBreakSpeed(event);
                }
            }
        }

    }
}
