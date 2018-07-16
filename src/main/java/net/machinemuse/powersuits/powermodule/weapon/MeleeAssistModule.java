package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class MeleeAssistModule extends PowerModuleBase {
    public static final String MODULE_MELEE_ASSIST = "Melee Assist";
    public static final String PUNCH_ENERGY = "Punch Energy Consumption";
    public static final String PUNCH_DAMAGE = "Melee Damage";
    public static final String PUNCH_KNOCKBACK = "Melee Knockback";

    public MeleeAssistModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addBaseProperty(PUNCH_ENERGY, 10, "J");
        addBaseProperty(PUNCH_DAMAGE, 2, "pt");
        addTradeoffProperty("Impact", PUNCH_ENERGY, 100, "J");
        addTradeoffProperty("Impact", PUNCH_DAMAGE, 8, "pt");
        addTradeoffProperty("Carry-through", PUNCH_ENERGY, 20, "J");
        addTradeoffProperty("Carry-through", PUNCH_KNOCKBACK, 1, "P");
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_MELEE_ASSIST;
    }

    @Override
    public String getUnlocalizedName() {
        return "meleeAssist";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.meleeAssist;
    }
}