package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class MeleeAssistModule extends PowerModuleBase {
    public MeleeAssistModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addBasePropertyDouble(MPSModuleConstants.PUNCH_ENERGY, 10, "J");
        addBasePropertyDouble(MPSModuleConstants.PUNCH_DAMAGE, 2, "pt");
        addTradeoffPropertyDouble("Impact", MPSModuleConstants.PUNCH_ENERGY, 100, "J");
        addTradeoffPropertyDouble("Impact", MPSModuleConstants.PUNCH_DAMAGE, 8, "pt");
        addTradeoffPropertyDouble("Carry-through", MPSModuleConstants.PUNCH_ENERGY, 20, "J");
        addTradeoffPropertyDouble("Carry-through", MPSModuleConstants.PUNCH_KNOCKBACK, 1, "P");
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
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