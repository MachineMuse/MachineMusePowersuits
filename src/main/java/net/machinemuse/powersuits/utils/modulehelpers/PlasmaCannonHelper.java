package net.machinemuse.powersuits.utils.modulehelpers;

import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by leon on 4/9/17.
 */
public class PlasmaCannonHelper {
    public static int getPlayerPlasma(EntityPlayer player) {
        ItemStack powerfist = player.getHeldItemMainhand();
        if (!powerfist.isEmpty() && player.isHandActive() && ModuleManager.INSTANCE.itemHasActiveModule(powerfist, MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME)) {
            int actualCount = (-player.getItemInUseCount() + 72000);
            return (actualCount > 50 ? 50 : actualCount) * 2;
        }
        return 0;
    }

    public static int getMaxPlasma(EntityPlayer player) {
        ItemStack powerfist = player.getHeldItemMainhand();
        if (!powerfist.isEmpty() && player.isHandActive() && ModuleManager.INSTANCE.itemHasActiveModule(powerfist, MPSModuleConstants.MODULE_PLASMA_CANNON__DATANAME)) {
            return 100;
        }
        return 0;
    }
}