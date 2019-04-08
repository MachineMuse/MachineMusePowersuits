package net.machinemuse.powersuits.item.module.tool;

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

public class ItemModuleLuxCapacitor extends ItemAbstractPowerModule implements IRightClickModule {
    public ItemModuleLuxCapacitor(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.GLOWSTONE_DUST, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.IRON_INGOT, 2));
//        addBasePropertyDouble(MPSModuleConstants.LUX_CAPACITOR_ENERGY_CONSUMPTION, 1000, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.RED, MPSModuleConstants.LUX_CAPACITOR_RED_HUE, 1, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.GREEN, MPSModuleConstants.LUX_CAPACITOR_GREEN_HUE, 1, "%");
//        addTradeoffPropertyDouble(MPSModuleConstants.BLUE, MPSModuleConstants.LUX_CAPACITOR_BLUE_HUE, 1, "%");
//        addTradeoffPropertyDouble("alpha", )// TODO: add alpha for the lense
    }
    @Override
    public ActionResult onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
//        playerIn.setActiveHand(hand);
//        if (!worldIn.isRemote) {
//            double energyConsumption = getEnergyUsage(itemStackIn);
//            MuseHeatUtils.heatPlayer(playerIn, energyConsumption / 500);
//            if (ElectricItemUtils.getPlayerEnergy(playerIn) > energyConsumption) {
//                ElectricItemUtils.drainPlayerEnergy(playerIn, (int) energyConsumption);
//
//                double red = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_RED_HUE);
//                double green = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_GREEN_HUE);
//                double blue = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStackIn, MPSModuleConstants.LUX_CAPACITOR_BLUE_HUE);
//
//                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(worldIn, playerIn, new Colour(red, green, blue));
//                worldIn.spawnEntity(luxCapacitor);
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
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.LUX_CAPACITOR_ENERGY_CONSUMPTION);
    }
}