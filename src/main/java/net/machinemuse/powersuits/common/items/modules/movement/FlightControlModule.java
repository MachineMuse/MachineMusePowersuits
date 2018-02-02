package net.machinemuse.powersuits.common.items.modules.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_FLIGHT_CONTROL;

public class FlightControlModule extends PowerModuleBase implements IToggleableModule {
    public static final String FLIGHT_VERTICALITY = "Y-look ratio";

    public FlightControlModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addTradeoffProperty("Verticality", FLIGHT_VERTICALITY, 1.0, "%");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_FLIGHT_CONTROL;
    }

    @Override
    public String getUnlocalizedName() {
        return "flightControl";
    }

    @SideOnly(Side.CLIENT)
    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.flightControl;
    }
}
