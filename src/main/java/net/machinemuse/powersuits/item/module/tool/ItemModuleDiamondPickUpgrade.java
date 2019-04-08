package net.machinemuse.powersuits.item.module.tool;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemModuleDiamondPickUpgrade extends ItemAbstractPowerModule implements IBlockBreakingModule, IToggleableModule {
    public static final ItemStack emulatedTool = new ItemStack(Items.DIAMOND_PICKAXE);

    public ItemModuleDiamondPickUpgrade(String regName) {
        super(regName, EnumModuleTarget.TOOLONLY, EnumModuleCategory.CATEGORY_TOOL);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.DIAMOND, 3));
    }

//    @Override
//    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
//        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.PICKAXE_ENERGY_CONSUMPTION);
//    }
//
//    @Override
//    public boolean canHarvestBlock(@Nonnull ItemStack stack, IBlockState state, EntityPlayer player, BlockPos pos, int playerEnergy) {
//        if (!ModuleManager.INSTANCE.itemHasModule(stack, MPSModuleConstants.MODULE_PICKAXE__DATANAME))
//            return false;
//
//        IPowerModule pickaxeModule = ModuleManager.INSTANCE.getModule(MPSModuleConstants.MODULE_PICKAXE__DATANAME);
//        return !((ItemModulePickaxe) pickaxeModule).canHarvestBlock(stack, state, player, pos, playerEnergy) &&
//                playerEnergy >= this.getEnergyUsage(stack) && ToolHelpers.isToolEffective(player.getEntityWorld(), pos, getEmulatedTool());
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
//                ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.PICKAXE_HARVEST_SPEED)));
//    }
//
//    @Override
//    public ItemStack getEmulatedTool() {
//        return emulatedTool;
//    }
}