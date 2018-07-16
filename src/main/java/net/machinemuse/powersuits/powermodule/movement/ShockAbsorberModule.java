package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ShockAbsorberModule extends PowerModuleBase implements IToggleableModule {
    public static final String MODULE_SHOCK_ABSORBER = "Shock Absorber";
    public static final String SHOCK_ABSORB_MULTIPLIER = "Distance Reduction";
    public static final String SHOCK_ABSORB_ENERGY_CONSUMPTION = "Impact Energy consumption";

    public ShockAbsorberModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addSimpleTradeoff(this, "Power", SHOCK_ABSORB_ENERGY_CONSUMPTION, "J/m", 0, 10, SHOCK_ABSORB_MULTIPLIER, "%", 0, 1);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.WOOL, 2));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
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