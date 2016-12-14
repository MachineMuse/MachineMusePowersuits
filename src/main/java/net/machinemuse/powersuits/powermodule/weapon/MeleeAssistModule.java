package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;

import java.util.List;

public class MeleeAssistModule extends PowerModuleBase {
    public static final String MODULE_MELEE_ASSIST = "Melee Assist";
    public static final String PUNCH_ENERGY = "Punch Energy Consumption";
    public static final String PUNCH_DAMAGE = "Melee Damage";
    public static final String PUNCH_KNOCKBACK = "Melee Knockback";

    public MeleeAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(PUNCH_ENERGY, 10, "J");
        addBaseProperty(PUNCH_DAMAGE, 2, "pt");
        addTradeoffProperty("Impact", PUNCH_ENERGY, 100, "J");
        addTradeoffProperty("Impact", PUNCH_DAMAGE, 8, "pt");
        addTradeoffProperty("Carry-through", PUNCH_ENERGY, 20, "J");
        addTradeoffProperty("Carry-through", PUNCH_KNOCKBACK, 1, "P");
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
    public String getUnlocalizedName() {
        return "meleeAssist";
    }

    @Override
    public String getDescription() {
        return "A much simpler addon, makes your powertool punches hit harder.";
    }
}
