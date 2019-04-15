package net.machinemuse.powersuits.event;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IPowerModule;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class HarvestEventHandler {
    @SubscribeEvent
    public void handleHarvestCheck(PlayerEvent.HarvestCheck event) {
        EntityPlayer player = event.getEntityPlayer();
        ItemStack stack = player.inventory.getCurrentItem();

        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerFist) {
            IBlockState state = event.getTargetBlock();
            if (state == null)
                return;
            RayTraceResult rayTraceResult = MusePlayerUtils.raytraceBlocks(player.world, player, true, 10);
            if (rayTraceResult == null)
                return;

            BlockPos pos = rayTraceResult.getBlockPos();
            if (pos == null || rayTraceResult.typeOfHit != RayTraceResult.Type.BLOCK)
                return;

            if (state.getBlock() == null)
                return;

            int playerEnergy = ElectricItemUtils.getPlayerEnergy(player);
            event.setCanHarvest(((ItemPowerFist) stack.getItem()).canHarvestBlock(stack, state, player, pos, playerEnergy));
        }
    }

    @SubscribeEvent
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
        // Note: here we can actually get the position if needed. we can't om the harvest check.
        IBlockState state = event.getState();
        EntityPlayer player = event.getEntityPlayer();

        ItemStack stack = player.inventory.getCurrentItem();
        if (!stack.isEmpty() && stack.getItem() instanceof ItemPowerFist) {
            if (event.getNewSpeed() < event.getOriginalSpeed())
                event.setNewSpeed(event.getOriginalSpeed());

            // TODO: add a way to look for the actual tool required instead of looping through multiple checks.

            int playerEnergy = ElectricItemUtils.getPlayerEnergy(player);
            for (IPowerModule module : ModuleManager.INSTANCE.getModulesOfType(IBlockBreakingModule.class)) {
                if (ModuleManager.INSTANCE.itemHasActiveModule(stack, module.getDataName()) && ((IBlockBreakingModule) module).canHarvestBlock(stack, state, player, event.getPos(), playerEnergy)) {
                    if (event.getNewSpeed() == 0)
                        event.setNewSpeed(1);
                    ((IBlockBreakingModule) module).handleBreakSpeed(event);
//                }
                }
            }
        }
    }

//    @SubscribeEvent
//    public void onBlockBreak(BlockEvent.BreakEvent e) {
//
//    }

//    @SubscribeEvent
//    public void lang(BlockEvent.HarvestDropsEvent event) {
//        if (event.getHarvester() != null) {
//            System.out.println("made it here");
//
//            EntityPlayer player = event.getHarvester();
//            ItemStack tool = player.getHeldItemMainhand();
//
//            List<ItemStack> originalDrops = event.getDrops();
//
//
//            for (ItemStack item:  originalDrops) {
//                System.out.println(item.serializeNBT());
//            }
//
//            System.out.println("original drops size: " + originalDrops.size());
//
//            if (tool.getItem() instanceof ItemPowerFist && ModuleManager.INSTANCE.itemHasModule(tool, MPSModuleConstants.MODULE_SILK_TOUCH__DATANAME)) {
//                event.setResult(new BlockEvent.HarvestDropsEvent(event.getWorld(), event.getPos(), event.getState(), event.getFortuneLevel(), event.getDropChance(), event.getDrops(), event.getHarvester(), true).getResult());
//
//                List<ItemStack> newList = event.getDrops();
//
//
//                for (ItemStack item:  newList) {
//                    System.out.println(item.serializeNBT());
//                }
//
//                System.out.println("new drops size: " + newList.size());
//
//                System.out.println("new list same as old list? : " + Objects.equals(originalDrops, newList));
//
//            }
//        }
//    }
}