package net.machinemuse.powersuits.item.module.movement;

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

public class ItemModuleBlinkDrive extends ItemAbstractPowerModule implements IRightClickModule {
    public ItemModuleBlinkDrive(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
//
//        addBasePropertyDouble(MPSModuleConstants.BLINK_DRIVE_ENERGY_CONSUMPTION, 10000, "RF");
//        addBasePropertyDouble(MPSModuleConstants.BLINK_DRIVE_RANGE, 5, "m");
//        addTradeoffPropertyDouble(MPSModuleConstants.RANGE, MPSModuleConstants.BLINK_DRIVE_ENERGY_CONSUMPTION, 30000);
//        addTradeoffPropertyDouble(MPSModuleConstants.RANGE, MPSModuleConstants.BLINK_DRIVE_RANGE, 59);
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        SoundEvent enderman_portal = SoundEvent.REGISTRY.getObject(new ResourceLocation("entity.endermen.teleport"));
//        int range = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.BLINK_DRIVE_RANGE);
//        int energyConsumption = getEnergyUsage(itemStackIn);
//        if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
//            NuminaPlayerUtils.resetFloatKickTicks(playerIn);
//            int amountDrained = ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
//
//            worldIn.playSound(playerIn, playerIn.getPosition(), enderman_portal, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
//            MuseLogger.logDebug("Range: " + range);
//            RayTraceResult hitRayTrace = MusePlayerUtils.doCustomRayTrace(playerIn.world, playerIn, true, range);
//
//            MuseLogger.logDebug("Hit:" + hitRayTrace);
//            MusePlayerUtils.teleportEntity(playerIn, hitRayTrace);
//            worldIn.playSound(playerIn, playerIn.getPosition(), enderman_portal, SoundCategory.PLAYERS, 0.5F, 0.4F / ((float) Math.random() * 0.4F + 0.8F));
//
//            MuseLogger.logDebug("blink drive anount drained: " + amountDrained);
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
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.BLINK_DRIVE_ENERGY_CONSUMPTION);
    }
}
