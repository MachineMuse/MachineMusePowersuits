package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class ParachuteModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static String MODULE_PARACHUTE;

    public ParachuteModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getName() {
        MODULE_PARACHUTE = StatCollector.translateToLocal("module.parachute.name");
        return MODULE_PARACHUTE;
    }

    @Override
    public String getDescription() {
        return "Add a parachute to slow your descent. Activate by pressing sneak (defaults to Shift) in midair.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
        float forwardkey = movementInput.forwardKey;
        boolean sneakkey = movementInput.sneakKey;
        ItemStack torso = player.getCurrentArmor(2);
        boolean hasGlider = false;
        MusePlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IModularItem) {
            hasGlider = MuseItemUtils.itemHasActiveModule(torso, GliderModule.MODULE_GLIDER);
        }
        if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
            double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
                    * MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
            if (totalVelocity > 0) {
                player.motionX = player.motionX * 0.1 / totalVelocity;
                player.motionY = player.motionY * 0.1 / totalVelocity;
                player.motionZ = player.motionZ * 0.1 / totalVelocity;
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getTextureFile() {
        return "parachute";
    }

}
