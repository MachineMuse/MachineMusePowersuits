package net.machinemuse.powersuits.item.module.weapon;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class MeleeAssistModule extends PowerModuleBase {
    public static final String PUNCH_ENERGY = "Punch Energy Consumption";
    public static final String PUNCH_DAMAGE = "Melee Damage";
    public static final String PUNCH_KNOCKBACK = "Melee Knockback";

    public MeleeAssistModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TOOLONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(PUNCH_ENERGY, 10, "J");
        addBasePropertyDouble(PUNCH_DAMAGE, 2, "pt");
        addTradeoffPropertyDouble("Impact", PUNCH_ENERGY, 100, "J");
        addTradeoffPropertyDouble("Impact", PUNCH_DAMAGE, 8, "pt");
        addTradeoffPropertyDouble("Carry-through", PUNCH_ENERGY, 20, "J");
        addTradeoffPropertyDouble("Carry-through", PUNCH_KNOCKBACK, 1, "P");
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_WEAPON;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.meleeAssist;
    }
}