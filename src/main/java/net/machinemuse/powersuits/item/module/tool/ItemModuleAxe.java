package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;


public class ItemModuleAxe extends ItemAbstractPowerModule implements IBlockBreakingModule, IToggleableModule {
    private static final ItemStack emulatedTool = new ItemStack(Items.IRON_AXE);

    public ItemModuleAxe(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        addBasePropertyDouble(MPSModuleConstants.AXE_ENERGY_CONSUMPTION, 500, "RF");
//        addBasePropertyDouble(MPSModuleConstants.AXE_HARVEST_SPEED, 8, "x");
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.AXE_ENERGY_CONSUMPTION, 9500);
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.AXE_HARVEST_SPEED, 22);
    }

//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return 0;
////        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.AXE_ENERGY_CONSUMPTION);
//    }

//    @Override
//    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy) {
//        if (this.canHarvestBlock(itemStack, state, (EntityPlayer) entityLiving, pos, playerEnergy)) {
//            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, getEnergyUsage(itemStack));
//            return true;
//        }
//        return false;
//    }

//    @Override
//    public void handleBreakSpeed(BreakSpeed event) {
//        event.setNewSpeed((float) (event.getNewSpeed() * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.AXE_HARVEST_SPEED)));
//    }

//    @Override
//    public ItemStack getEmulatedTool() {
//        return emulatedTool;
//    }
}