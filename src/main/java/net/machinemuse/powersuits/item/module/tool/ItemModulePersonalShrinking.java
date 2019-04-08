package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
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
 * Created by User: Korynkai
 * 5:41 PM 2014-11-19
 */

/*
    TODO: the mechanics have changed a bit. This module will req
 */
public class ItemModulePersonalShrinking extends ItemAbstractPowerModule implements IRightClickModule, IPlayerTickModule {
//    private final ItemStack cpmPSD = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("cm2", "psd")), 1);

    public ItemModulePersonalShrinking(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        NBTTagCompound nbt = new NBTTagCompound();
//        nbt.setInteger("fluid", 4000);
//        cpmPSD.setTagCompound(nbt);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 4));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), cpmPSD);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        return new ActionResult(EnumActionResult.FAIL, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return EnumActionResult.PASS;
    }

    @Override
    public EnumActionResult onItemUseFirst(ItemStack itemStackIn, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
//        return cpmPSD.getItem().onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
        return EnumActionResult.PASS;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (!PersonalShrinkingModuleHelper.getCanShrink(item)) {
//            PersonalShrinkingModuleHelper.setCanShrink(item, true);
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (PersonalShrinkingModuleHelper.getCanShrink(item)) {
//            PersonalShrinkingModuleHelper.setCanShrink(item, false);
//        }
    }

    public float minF(float a, float b) {
        return a < b ? a : b;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {

    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return 0;
    }
}
