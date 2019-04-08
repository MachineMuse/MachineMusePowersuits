package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleClimbAssist extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleClimbAssist(String regName) {
        super(regName, EnumModuleTarget.LEGSONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        player.stepHeight = 1.001F;
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.stepHeight == 1.001F) {
            player.stepHeight = 0.5001F;
        }
    }
}