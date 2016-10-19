package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ShockAbsorberModule extends PowerModuleBase implements IToggleableModule {
    public static final String MODULE_SHOCK_ABSORBER = "Shock Absorber";
    public static final String SHOCK_ABSORB_MULTIPLIER = "Distance Reduction";
    public static final String SHOCK_ABSORB_ENERGY_CONSUMPTION = "Impact Energy consumption";

    public ShockAbsorberModule(List<IModularItem> validItems) {
        super(validItems);
        addSimpleTradeoff(this, "Power", SHOCK_ABSORB_ENERGY_CONSUMPTION, "J/m", 0, 10, SHOCK_ABSORB_MULTIPLIER, "%", 0, 1);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(new ItemStack(Blocks.wool, 2));
        }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_SHOCK_ABSORBER;
    }

    @Override
    public String getUnlocalizedName() { return "shockAbsorber";
    }

    @Override
    public String getDescription() {
        return "With some servos, springs, and padding, you should be able to negate a portion of fall damage.";
    }

    @Override
    public String getTextureFile() {
        return "shockabsorber";
    }
}
