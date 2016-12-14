package net.machinemuse.powersuits.powermodule.tool;

import cofh.api.block.IDismantleable;
import cofh.lib.util.helpers.BlockHelper;
import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.eventhandler.Event;
import ic2.api.tile.IWrenchable;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

/**
 * Created by MachineMuse on 9/7/2015.
 *
 * Unabashedly ripped off of the Prototype Omniwrench in Redstone Arsenal.
 *
 */
public class StolenWrenchCode {
    public static void useEnergy(ItemStack stack, boolean simulate) {
        // TBI, maybe?
    }

    public static boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int hitSide, float hitX, float hitY, float hitZ) {

        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(0);
        }
//        if (!player.capabilities.isCreativeMode && getEnergyStored(stack) < getEnergyPerUse(stack)) {
//            return false;
//        }
        Block block = world.getBlock(x, y, z);

        if (block == null) {
            return false;
        }
        PlayerInteractEvent event = new PlayerInteractEvent(player, PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, x, y, z, hitSide, world);
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY || event.useBlock == Event.Result.DENY || event.useItem == Event.Result.DENY) {
            return false;
        }
        if (ModCompatibility.isRFAPILoaded() && ServerHelper.isServerWorld(world) && player.isSneaking() && block instanceof IDismantleable
                && ((IDismantleable) block).canDismantle(player, world, x, y, z)) {
            ((IDismantleable) block).dismantleBlock(player, world, x, y, z, false);

            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }
            return true;
        }
//        if (BlockHelper.canRotate(block)) {
//            if (player.isSneaking()) {
//                world.setBlockMetadataWithNotify(x, y, z, BlockHelper.rotateVanillaBlockAlt(world, block, x, y, z), 3);
//                world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, block.stepSound.getBreakSound(), 1.0F, 0.6F);
//            } else {
//                world.setBlockMetadataWithNotify(x, y, z, BlockHelper.rotateVanillaBlock(world, block, x, y, z), 3);
//                world.playSoundEffect(x + 0.5, y + 0.5, z + 0.5, block.stepSound.getBreakSound(), 1.0F, 0.8F);
//            }
//            if (!player.capabilities.isCreativeMode) {
//                useEnergy(stack, false);
//            }
//            return ServerHelper.isServerWorld(world);
//        } else if (!player.isSneaking() && block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(hitSide))) {
//            if (!player.capabilities.isCreativeMode) {
//                useEnergy(stack, false);
//            }
//            return ServerHelper.isServerWorld(world);
//        }
        TileEntity tile = world.getTileEntity(x, y, z);

        if (ModCompatibility.isIndustrialCraftLoaded() && tile instanceof IWrenchable) {
            IWrenchable wrenchable = (IWrenchable) tile;

            if (player.isSneaking()) {
                hitSide = BlockHelper.SIDE_OPPOSITE[hitSide];
            }
            if (wrenchable.wrenchCanSetFacing(player, hitSide)) {
                if (ServerHelper.isServerWorld(world)) {
                    wrenchable.setFacing((short) hitSide);
                }
            } else if (wrenchable.wrenchCanRemove(player)) {
                ItemStack dropBlock = wrenchable.getWrenchDrop(player);

                if (dropBlock != null) {
                    world.setBlockToAir(x, y, z);
                    if (ServerHelper.isServerWorld(world)) {
                        List<ItemStack> drops = block.getDrops(world, x, y, z, world.getBlockMetadata(x, y, z), 0);

                        if (drops.isEmpty()) {
                            drops.add(dropBlock);
                        } else {
                            drops.set(0, dropBlock);
                        }
                        for (ItemStack drop : drops) {
                            float f = 0.7F;
                            double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                            double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                            double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                            EntityItem entity = new EntityItem(world, x + x2, y + y2, z + z2, drop);
                            entity.delayBeforeCanPickup = 10;
                            world.spawnEntityInWorld(entity);
                        }
                    }
                }
            }
            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }
            return ServerHelper.isServerWorld(world);
        }
        return false;
    }
}
