package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.util.StatCollector;

import java.util.List;

public class MeleeAssistModule extends PowerModuleBase {
    public static final String MODULE_MELEE_ASSIST = "Melee Assist";
    public static final String PUNCH_ENERGY = "Punch Energy Consumption";
    public static final String PUNCH_DAMAGE = "Melee Damage";
    public static final String PUNCH_KNOCKBACK = "Melee Knockback";
    protected static final String IMPACT = "Impact";
    protected static final String CARRY_THROUGH = "Carry-through";
    
    public MeleeAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(PUNCH_ENERGY, 10, "J");
        addBaseProperty(PUNCH_DAMAGE, 2, "pt");
        addTradeoffProperty(IMPACT, PUNCH_ENERGY, 100, "J");
        addTradeoffProperty(IMPACT, PUNCH_DAMAGE, 8, "pt");
        addTradeoffProperty(CARRY_THROUGH, PUNCH_ENERGY, 20, "J");
        addTradeoffProperty(CARRY_THROUGH, PUNCH_KNOCKBACK, 1, "P");
        addPropertyLocalString(PUNCH_ENERGY, StatCollector.translateToLocal("module.meleeAssist.energy"));
        addPropertyLocalString(PUNCH_DAMAGE, StatCollector.translateToLocal("module.meleeAssist.damage"));
        addPropertyLocalString(PUNCH_KNOCKBACK, StatCollector.translateToLocal("module.meleeAssist.knockback"));
        addPropertyLocalString(IMPACT, StatCollector.translateToLocal("module.meleeAssist.impact"));
        addPropertyLocalString(CARRY_THROUGH, StatCollector.translateToLocal("module.meleeAssist.carrythrough"));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
    }

    @Override
    public String getTextureFile() {
        return "toolfist";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_MELEE_ASSIST;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.meleeAssist.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.meleeAssist.desc");
    }
}
