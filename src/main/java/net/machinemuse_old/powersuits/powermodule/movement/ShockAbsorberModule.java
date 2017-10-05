package net.machinemuse_old.powersuits.powermodule.movement;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
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
        addInstallCost(new ItemStack(Blocks.WOOL, 2));
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
    public String getUnlocalizedName() {
        return "shockAbsorber";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.shockAbsorber;
    }
}