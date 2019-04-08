package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;

/**
 * Created by User: Sergey Popov aka Pinkbyte
 * Date: 9/08/15
 * Time: 5:53 PM
 */
public class ItemModuleScoop extends ItemAbstractPowerModule implements IBlockBreakingModule {
//    public static final ItemStack emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("forestry", "scoop")), 1);

    public ItemModuleScoop(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), emulatedTool);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        addBasePropertyDouble(MPSModuleConstants.SCOOP_ENERGY_CONSUMPTION, 20000, "RF");
//        addBasePropertyDouble(MPSModuleConstants.SCOOP_HARVEST_SPEED, 5, "x");
    }
//
//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SCOOP_ENERGY_CONSUMPTION);
//    }
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
//        event.setNewSpeed((float) (event.getNewSpeed() *
//                ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.SCOOP_HARVEST_SPEED)));
//    }
//
//    @Override
//    public ItemStack getEmulatedTool() {
//        return emulatedTool;
//    }
}