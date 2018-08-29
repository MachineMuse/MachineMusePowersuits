package net.machinemuse.powersuits.powermodule.environmental;

import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.MPSItems;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.fluids.UniversalBucket;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class NitrogenCoolingSystem extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public NitrogenCoolingSystem(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        //ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.netherStar, 1));

        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(
                UniversalBucket.getFilledBucket(ForgeModContainer.getInstance().universalBucket, MPSItems.INSTANCE.liquidNitrogen), 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.rubberHose, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.computerChip, 2));

        addTradeoffPropertyDouble("Nitrogen Cooling Power", MPSModuleConstants.COOLING_BONUS, 7, "%");
        addTradeoffPropertyDouble("Nitrogen Cooling Power", MPSModuleConstants.NITROGEN_COOLING_SYSTEM_ENERGY_CONSUMPTION, 160, "RF/t");

        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1, "pts");
        addIntTradeoffProperty("Nitrogen Cooling Power", MPSModuleConstants.SLOT_POINTS, 4, "m", 1, 0);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double heatBefore = MuseHeatUtils.getPlayerHeat(player);
        MuseHeatUtils.coolPlayer(player, 0.210 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.COOLING_BONUS));
        double cooling = heatBefore - MuseHeatUtils.getPlayerHeat(player);
        ElectricItemUtils.drainPlayerEnergy(player, (int) (cooling * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.NITROGEN_COOLING_SYSTEM_ENERGY_CONSUMPTION)));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_NITROGEN_COOLING_SYSTEM__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.nitrogenCoolingSystem;
    }
}