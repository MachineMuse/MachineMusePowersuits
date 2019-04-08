package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;


// FIXME!!!! this module does nothing. Maybe rewrite it to use it as an actual chisel
public class ItemModuleChisel extends ItemAbstractPowerModule implements IBlockBreakingModule, IToggleableModule {
//    // TODO Fixme put actual item.
//    private static final ItemStack emulatedTool = new ItemStack(
//            Item.REGISTRY.getObject(new ResourceLocation("chisel", "chisel_iron")), 1);

    public ItemModuleChisel(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 2));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        addBasePropertyDouble(MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION, 500, "RF");
//        addBasePropertyDouble(MPSModuleConstants.CHISEL_HARVEST_SPEED, 8, "x");
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION, 9500);
//        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.CHISEL_HARVEST_SPEED, 22);
    }
//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return (int) Math.round(ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION));
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
//        event.setNewSpeed((float) (event.getNewSpeed() *
//                ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.CHISEL_HARVEST_SPEED)));
//    }
//
//    @Override
//    public ItemStack getEmulatedTool() {
//        return emulatedTool; // FIXME TOO!!
//    }

}