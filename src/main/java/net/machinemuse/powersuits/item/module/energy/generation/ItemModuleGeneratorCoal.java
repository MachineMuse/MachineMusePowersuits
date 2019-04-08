package net.machinemuse.powersuits.item.module.energy.generation;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Eximius88 on 1/16/14.
 *
 *
 *
 * // TODO: everything
 *
 * things to figure out:
 * ---------------------
  - use all burnable fuel or just coal
  - draw from player inventory or set up some other type of slot?
  - charge player energy through electric item utils...

 *
 *
 *
 */
public class ItemModuleGeneratorCoal extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {
    public ItemModuleGeneratorCoal(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_ENERGY_GENERATION);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.FURNACE));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//
//        addBasePropertyDouble(MPSModuleConstants.MAX_COAL_STORAGE, 128);
//        addBasePropertyDouble(MPSModuleConstants.HEAT_GENERATION, 2.5);
//        addBasePropertyDouble(MPSModuleConstants.ENERGY_PER_COAL, 300);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {

        // TODO: add charging code, change to more generic combustion types... maybe add GUI

//        IInventory inv = player.inventory;
//        int coalNeeded = (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item);
//        if (coalNeeded > 0) {
//            for (int i = 0; i < inv.getSizeInventory(); i++) {
//                ItemStack stack = inv.getStackInSlot(i);
//                if (!stack.isEmpty() && stack.getItem() == Items.COAL) {
//                    int loopTimes = coalNeeded < stack.getCount() ? coalNeeded : stack.getCount();
//                    for (int i2 = 0; i2 < loopTimes; i2++) {
//                        CoalGenHelper.setCoalLevel(item, CoalGenHelper.getCoalLevel(item) + 1);
//                        player.inventory.decrStackSize(i, 1);
//                        if (stack.getCount() == 0) {
//                            player.inventory.setInventorySlotContents(i, ItemStack.EMPTY);
//                        }
//                    }
//
//
//                    if (ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.MAX_COAL_STORAGE) - CoalGenHelper.getCoalLevel(item) < 1) {
//                        i = inv.getSizeInventory() + 1;
//                    }
//                }
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY_GENERATION;
    }
}