package net.machinemuse.powersuits.item.module.weapon;


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

/**
 * Created by User: Andrew2448
 * 5:56 PM 6/14/13
 */
public class ItemModuleLightning extends ItemAbstractPowerModule implements IRightClickModule {
    public ItemModuleLightning(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_WEAPON);
//        super(EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_WEAPON);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.hvcapacitor, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));

//        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 4900000, "RF");
//        addBasePropertyDouble(MPSModuleConstants.HEAT_EMISSION, 100, "");
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        if (hand == EnumHand.MAIN_HAND) {
//            try {
//                double range = 64;
//                int energyConsumption = getEnergyUsage(itemStackIn);
//                if (energyConsumption < ElectricItemUtils.getPlayerEnergy(playerIn)) {
//                    ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
//                    MuseHeatUtils.heatPlayer(playerIn, ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.HEAT_EMISSION));
//                    RayTraceResult raytraceResult = MusePlayerUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);
//                    worldIn.spawnEntity(new EntityLightningBolt(playerIn.world, raytraceResult.hitVec.x, raytraceResult.hitVec.y, raytraceResult.hitVec.z, false));
//                }
//            } catch (Exception ignored) {
//                return ActionResult.newResult(EnumActionResult.FAIL, itemStackIn);
//            }
//            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStackIn);
//        }
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
    }
}