package net.machinemuse.powersuits.item.module.weapon;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.math.MuseMathUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemModulePlasmaCannon extends ItemAbstractPowerModule implements IRightClickModule {
    public ItemModulePlasmaCannon(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_WEAPON);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 2));
//
//        addBasePropertyDouble(MPSModuleConstants.PLASMA_CANNON_ENERGY_PER_TICK, 100, "RF");
//        addBasePropertyDouble(MPSModuleConstants.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 2, "pt");
//        addTradeoffPropertyDouble(MPSModuleConstants.AMPERAGE, MPSModuleConstants.PLASMA_CANNON_ENERGY_PER_TICK, 1500, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.AMPERAGE, MPSModuleConstants.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE, 38, "pt");
//        addTradeoffPropertyDouble(MPSModuleConstants.VOLTAGE, MPSModuleConstants.PLASMA_CANNON_ENERGY_PER_TICK, 500, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.VOLTAGE, MPSModuleConstants.PLASMA_CANNON_EXPLOSIVENESS, 0.5, MPSModuleConstants.CREEPER);
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (hand == EnumHand.MAIN_HAND && ElectricItemUtils.getPlayerEnergy(playerIn) > 500) {
            playerIn.setActiveHand(hand);
            return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
        }
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        int chargeTicks = (int) MuseMathUtils.clampDouble(itemStack.getUseDuration() - timeLeft, 10, 50);
//        if (!worldIn.isRemote) {
//            double energyConsumption = getEnergyUsage(itemStack)* chargeTicks;
//            if (entityLiving instanceof EntityPlayer) {
//                EntityPlayer player = (EntityPlayer) entityLiving;
//                MuseHeatUtils.heatPlayer(player, energyConsumption / 5000);
//                if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
//                    ElectricItemUtils.drainPlayerEnergy(player, (int) energyConsumption);
//                    double explosiveness = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PLASMA_CANNON_EXPLOSIVENESS);
//                    double damagingness = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE);
//
//                    EntityPlasmaBolt plasmaBolt = new EntityPlasmaBolt(worldIn, player, explosiveness, damagingness, chargeTicks);
//                    worldIn.spawnEntity(plasmaBolt);
//                }
//            }
//        }
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PLASMA_CANNON_ENERGY_PER_TICK);
    }
}
