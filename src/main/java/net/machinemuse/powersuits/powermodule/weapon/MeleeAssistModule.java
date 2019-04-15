package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class MeleeAssistModule extends PowerModuleBase {
    public MeleeAssistModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));

        addBasePropertyDouble(MPSModuleConstants.PUNCH_ENERGY, 10, "RF");
        addBasePropertyDouble(MPSModuleConstants.PUNCH_DAMAGE, 2, "pt");
        addTradeoffPropertyDouble(MPSModuleConstants.IMPACT, MPSModuleConstants.PUNCH_ENERGY, 1000, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.IMPACT, MPSModuleConstants.PUNCH_DAMAGE, 8, "pt");
        addTradeoffPropertyDouble(MPSModuleConstants.CARRY_THROUGH, MPSModuleConstants.PUNCH_ENERGY, 200, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.CARRY_THROUGH, MPSModuleConstants.PUNCH_KNOCKBACK, 1, "P");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_MELEE_ASSIST__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.meleeAssist;
    }
}