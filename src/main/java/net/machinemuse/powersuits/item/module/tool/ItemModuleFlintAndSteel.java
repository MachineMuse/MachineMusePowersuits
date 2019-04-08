package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IRightClickModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * Created by User: Andrew2448
 * 10:48 PM 6/11/13
 */
public class ItemModuleFlintAndSteel extends ItemAbstractPowerModule implements IRightClickModule {
    public final ItemStack fas = new ItemStack(Items.FLINT_AND_STEEL);
    final Random ran = new Random();

    public ItemModuleFlintAndSteel(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), fas);
//        addBasePropertyDouble(MPSModuleConstants.IGNITION_ENERGY_CONSUMPTION, 10000, "RF");
    }

    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        return ActionResult.newResult(EnumActionResult.PASS, itemStackIn);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
//        int energyConsumption = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(stack, MPSModuleConstants.IGNITION_ENERGY_CONSUMPTION);
//        if (energyConsumption < ElectricItemUtils.getPlayerEnergy(playerIn)) {
//            pos = pos.offset(facing);
//            if (!playerIn.canPlayerEdit(pos, facing, stack)) {
//                return EnumActionResult.FAIL;
//            } else {
//                if (worldIn.isAirBlock(pos)) {
//                    ElectricItemUtils.drainPlayerEnergy(playerIn, energyConsumption);
//                    worldIn.playSound(playerIn, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, ran.nextFloat() * 0.4F + 0.8F);
//                    worldIn.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);
//                }
//
//                stack.damageItem(1, playerIn);
//                return EnumActionResult.SUCCESS;
//            }
//        }
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
    }
}
