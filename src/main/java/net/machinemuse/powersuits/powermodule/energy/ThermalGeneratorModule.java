package net.machinemuse.powersuits.powermodule.energy;


import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 6:43 PM 4/23/13
 */
public class ThermalGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public ThermalGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        // Fixme: and maybe add options for a Magmatic Dynamo and maybe a stirling generator
//        if (ModCompatibility.isIndustrialCraftLoaded()) {
//            ModuleManager.INSTANCE.addInstallCost(getDataName(),ModCompatibility.getIC2Item("geothermalGenerator"));
        // <ic2:te:4> = geothermal generator

//            ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        } else {
        ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.ironPlating, 1));
//        }

        addBasePropertyDouble(MPSModuleConstants.THERMAL_ENERGY_GENERATION, 250);
        addTradeoffPropertyDouble("Energy Generated", MPSModuleConstants.THERMAL_ENERGY_GENERATION, 250, "RF");
        addBasePropertyDouble(MPSModuleConstants.SLOT_POINTS, 1);
        addIntTradeoffProperty("Energy Generated", MPSModuleConstants.SLOT_POINTS, 4, "pts", 1, 0);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_THERMAL_GENERATOR__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double currentHeat = MuseHeatUtils.getPlayerHeat(player);
        double maxHeat = MuseHeatUtils.getPlayerMaxHeat(player);
        if (player.world.getTotalWorldTime() % 20 == 0) {
            if (player.isBurning()) {
                ElectricItemUtils.givePlayerEnergy(player, (int) (4 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION)));
            } else if (currentHeat >= 200) {
                ElectricItemUtils.givePlayerEnergy(player, (int) (2 * ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION)));
            } else if ((currentHeat / maxHeat) >= 0.5) {
                ElectricItemUtils.givePlayerEnergy(player, (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.THERMAL_ENERGY_GENERATION));
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.thermalGenerator;
    }
}