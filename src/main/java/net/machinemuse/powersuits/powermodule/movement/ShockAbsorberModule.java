package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class ShockAbsorberModule extends PowerModuleBase implements IToggleableModule {
    public ShockAbsorberModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Blocks.WOOL, 2));

//
//        addSimpleTradeoffDouble(this, "Power", SHOCK_ABSORB_ENERGY_CONSUMPTION, "RF/m", 0, 100, SHOCK_ABSORB_MULTIPLIER, "%", 0, 1);

        this.addBasePropertyDouble(MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION, 0, "RF/m");
        this.addTradeoffPropertyDouble("Power", MPSModuleConstants.SHOCK_ABSORB_ENERGY_CONSUMPTION, 10);
        this.addBasePropertyDouble(MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER, 0, "%");
        this.addTradeoffPropertyDouble("Power", MPSModuleConstants.SHOCK_ABSORB_MULTIPLIER, 1);
        this.addBasePropertyInteger(MPSModuleConstants.SLOT_POINTS, 4);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_SHOCK_ABSORBER__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.shockAbsorber;
    }
}