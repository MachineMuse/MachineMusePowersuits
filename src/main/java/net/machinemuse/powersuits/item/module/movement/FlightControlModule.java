package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;

public class FlightControlModule extends PowerModuleBase implements IToggleableModule {
    public static final String FLIGHT_VERTICALITY = "Y-look ratio";

    public FlightControlModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.HEADONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addTradeoffPropertyDouble("Verticality", FLIGHT_VERTICALITY, 1.0, "%");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_SPECIAL;
    }
}