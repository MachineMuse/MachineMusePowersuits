package net.machinemuse.powersuits.common.items.modules.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_SHOCK_ABSORBER;

public class ShockAbsorberModule extends PowerModuleBase implements IToggleableModule {

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