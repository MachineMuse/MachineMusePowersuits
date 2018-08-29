package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CoolingSystemModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public CoolingSystemModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Items.ENDER_EYE, 4));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addTradeoffPropertyDouble("Water Cooling Power", MPSModuleConstants.COOLING_BONUS, 4, "%");
        addTradeoffPropertyDouble("Water Cooling Power", MPSModuleConstants.COOLING_SYSTEM_ENERGY_CONSUMPTION, 100, "RF/t");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1, "pts");
        addIntTradeoffProperty("Water Cooling Power", MPSModuleConstants.SLOT_POINTS, 4, "m", 1, 0);
    }


    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_COOLING_SYSTEM__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double heatBefore = MuseHeatUtils.getPlayerHeat(player);
        MuseHeatUtils.coolPlayer(player, 0.1 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.COOLING_BONUS));
        double cooling = heatBefore - MuseHeatUtils.getPlayerHeat(player);
        ElectricItemUtils.drainPlayerEnergy(player, (int) (cooling * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.COOLING_SYSTEM_ENERGY_CONSUMPTION)));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.coolingSystem;
    }
}