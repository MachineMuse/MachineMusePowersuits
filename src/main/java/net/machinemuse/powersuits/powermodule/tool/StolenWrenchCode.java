package net.machinemuse.powersuits.powermodule.tool;

import cofh.api.block.IDismantleable;
import cofh.core.util.helpers.BlockHelper;
import cofh.core.util.helpers.ServerHelper;
import ic2.api.tile.IWrenchable;
import net.machinemuse.numina.misc.ModCompatibility;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.Iterator;
import java.util.List;

/**
 * Created by MachineMuse on 9/7/2015.
 * <p>
 * Unabashedly ripped off of the Prototype Omniwrench in Redstone Arsenal, AGAIN!!!
 */
public class StolenWrenchCode {
    public static void useEnergy(ItemStack stack, boolean simulate) {
        // TBI, maybe?
    }

    public static EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        if (stack.getItemDamage() > 0) {
            stack.setItemDamage(0);
        }

        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        TileEntity tile = world.getTileEntity(pos);
        if (world.isAirBlock(pos))
            return EnumActionResult.PASS;

        PlayerInteractEvent.RightClickBlock event = new PlayerInteractEvent.RightClickBlock(player, hand, pos, side, new Vec3d((double) hitX, (double) hitY, (double) hitZ));
        if (MinecraftForge.EVENT_BUS.post(event) || event.getResult() == Event.Result.DENY || event.getUseItem() == Event.Result.DENY || event.getUseBlock() == Event.Result.DENY)
            return EnumActionResult.PASS;

        if (ModCompatibility.isRFAPILoaded() && ServerHelper.isServerWorld(world) && player.isSneaking() &&
                block instanceof IDismantleable && ((IDismantleable) block).canDismantle(world, pos, state, player)) {
            ((IDismantleable) block).dismantleBlock(world, pos, state, player, false);

            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }
            return EnumActionResult.SUCCESS;
        } else if (ModCompatibility.isIndustrialCraftLoaded() && block instanceof IWrenchable && block.hasTileEntity(state)) {
            int hitSide = side.ordinal();
            IWrenchable wrenchable = (IWrenchable) block;
            if (player.isSneaking()) {
                hitSide = BlockHelper.SIDE_OPPOSITE[hitSide];
            }

            if (wrenchable.setFacing(world, pos, EnumFacing.VALUES[hitSide], player)) {
                return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
            } else if (wrenchable.wrenchCanRemove(world, pos, player)) {
                int fortune = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, stack);
                List<ItemStack> drops = wrenchable.getWrenchDrops(world, pos, state, tile, player, fortune);
                if (!drops.isEmpty()) {
                    world.setBlockToAir(pos);
                    if (ServerHelper.isServerWorld(world)) {
                        Iterator iterator = drops.iterator();

                        while (iterator.hasNext()) {
                            ItemStack drop = (ItemStack) iterator.next();
                            float f = 0.7F;
                            double x2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                            double y2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                            double z2 = (double) (world.rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
                            EntityItem entity = new EntityItem(world, (double) pos.getX() + x2, (double) pos.getY() + y2, (double) pos.getZ() + z2, drop);
                            entity.setPickupDelay(10);
                            world.spawnEntity(entity);
                        }
                    }
                    return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
                }
                return EnumActionResult.PASS;
            }
        } else if (BlockHelper.canRotate(block)) {
            world.setBlockState(pos, BlockHelper.rotateVanillaBlock(world, state, pos), 3);
            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }
            player.swingArm(hand);
            return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        } else if (!player.isSneaking() && block.rotateBlock(world, pos, side)) {
            if (!player.capabilities.isCreativeMode) {
                useEnergy(stack, false);
            }

            player.swingArm(hand);
            return ServerHelper.isServerWorld(world) ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
        } else {
            return EnumActionResult.PASS;
        }
        return EnumActionResult.PASS;
    }
}
