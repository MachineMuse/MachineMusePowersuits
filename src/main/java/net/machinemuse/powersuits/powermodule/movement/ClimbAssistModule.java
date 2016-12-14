package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ClimbAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_CLIMB_ASSIST = "Uphill Step Assist";

    public ClimbAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_CLIMB_ASSIST;
    }

    @Override
    public String getUnlocalizedName() { return "climbAssist";
    }

    @Override
    public String getDescription() {
        return "A pair of dedicated servos allow you to effortlessly step up 1m-high ledges.";
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

    @Override
    public String getTextureFile() {
        // TODO Auto-generated method stub
        return "climbassist";
    }
}
