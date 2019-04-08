package net.machinemuse.powersuits.item.module.miningenhancement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;


// Note: tried as an enchantment, but failed to function properly due to how block breaking code works
public class ItemModuleAquaAffinity extends ItemAbstractPowerModule implements IMiningEnhancementModule, IBlockBreakingModule {
    public ItemModuleAquaAffinity(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_MINING_ENHANCEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
//        addBasePropertyDouble(MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION, 0, "RF");
//        addBasePropertyDouble(MPSModuleConstants.UNDERWATER_HARVEST_SPEED, 0.2, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION, 1000);
//        addTradeoffPropertyDouble(MPSModuleConstants.POWER, MPSModuleConstants.UNDERWATER_HARVEST_SPEED, 0.8);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        return false;
    }

//    @Override
//    public boolean canHarvestBlock(@Nonnull ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos, int playerEnergy) {
//        return false;
//    }
//
//    @Override
//    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy) {
////        if (this.canHarvestBlock(itemStack, state, (EntityPlayer) entityLiving, pos, playerEnergy)) {
////            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, getEnergyUsage(itemStack));
////            return true;
////        }
//        return false;
//    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.AQUA_AFFINITY_ENERGY_CONSUMPTION);
    }

//    @Override
//    public void handleBreakSpeed(BreakSpeed event) {
//        EntityPlayer player = event.getEntityPlayer();
//        ItemStack stack = player.inventory.getCurrentItem();
//
//        if (event.getNewSpeed() > 1
//                && (player.isInsideOfMaterial(Material.WATER) || !player.onGround)
//                && ElectricItemUtils.getPlayerEnergy(player) > getEnergyUsage(stack)) {
//            event.setNewSpeed((float) (event.getNewSpeed() * 5 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.UNDERWATER_HARVEST_SPEED)));
//        }
//    }
}