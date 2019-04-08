package net.machinemuse.powersuits.item.module.miningenhancement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IEnchantmentModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;


public class ItemModuleSilkTouch extends ItemAbstractPowerModule implements IEnchantmentModule, IMiningEnhancementModule {
//    final ItemStack book;

    // TODO: add trade off and/or power consumption and a toggle mechanic... maybe through ticking

    public ItemModuleSilkTouch(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT);
//        book = new ItemStack(Items.ENCHANTED_BOOK);
//        book.addEnchantment(Enchantments.SILK_TOUCH, 1);
//
////        ModuleManager.INSTANCE.addInstallCost(getDataName(), book);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 12));
//
//        addBasePropertyDouble(MPSModuleConstants.SILK_TOUCH_ENERGY_CONSUMPTION, 2500, "RF");
    }

    /**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos Block's position in world
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
//        if (!player.world.isRemote) {
//            if (getEnergyUsage(itemstack) > ElectricItemUtils.getPlayerEnergy(player))
//                removeEnchantment(itemstack);
//            else {
//                Block block = player.world.getBlockState(pos).getBlock();
//                if (block.canSilkHarvest(player.world, pos, player.world.getBlockState(pos), player)) {
//                    ElectricItemUtils.drainPlayerEnergy(player, getEnergyUsage(itemstack));
//                }
//            }
//        }
        return false;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SILK_TOUCH_ENERGY_CONSUMPTION);
    }

    @Override
    public Enchantment getEnchantment() {
        return Enchantments.SILK_TOUCH;
    }

    @Override
    public int getLevel(@Nonnull ItemStack itemStack) {
        return 1;
    }
}