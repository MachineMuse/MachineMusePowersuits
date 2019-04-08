package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class ItemModulePickaxe extends ItemAbstractPowerModule implements IBlockBreakingModule, IToggleableModule {
    protected static final ItemStack emulatedTool = new ItemStack(Items.IRON_PICKAXE);

    public ItemModulePickaxe(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.IRON_INGOT, 3));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        addBasePropertyDouble(MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION, 500, "RF");
//        addBasePropertyDouble(MPSModuleConstants.PICKAXE_HARVEST_SPEED, 8, "x");
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION, 9500);
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.PICKAXE_HARVEST_SPEED, 52);
    }
//
//    @Override
//    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy) {
//        if (this.canHarvestBlock(itemStack, state, (EntityPlayer) entityLiving, pos, playerEnergy)) {
//            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, getEnergyUsage(itemStack));
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void handleBreakSpeed(BreakSpeed event) {
//        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.PICKAXE_HARVEST_SPEED)));
//    }
//
//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION);
//    }
//
//    @Override
//    public ItemStack getEmulatedTool() {
//        return emulatedTool;
//    }
}