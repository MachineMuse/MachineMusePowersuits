package net.machinemuse.powersuits.powermodule.energy;


import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.MuseHeatUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 6:43 PM 4/23/13
 */
public class ThermalGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public static final String MODULE_THERMAL_GENERATOR = "Thermal Generator";
    public static final String THERMAL_ENERGY_GENERATION = "Energy Generation";

    public ThermalGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        addBaseProperty(THERMAL_ENERGY_GENERATION, 25);
        addBaseProperty(MPSModuleConstants.WEIGHT, 1000);
        addTradeoffProperty("Energy Generated", THERMAL_ENERGY_GENERATION, 25, " Joules");
        addTradeoffProperty("Energy Generated", MPSModuleConstants.WEIGHT, 1000, "g");
// Fixme: and maybe add options for a Magmatic Dynamo and maybe a stirling generator
//        if (ModCompatibility.isIndustrialCraftLoaded()) {
//            ModuleManager.INSTANCE.addInstallCost(getDataName(),ModCompatibility.getIC2Item("geothermalGenerator"));
//            ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        } else {
            ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
            ModuleManager.INSTANCE.addInstallCost(getDataName(),MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
//        }
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY;
    }

    @Override
    public String getDataName() {
        return MODULE_THERMAL_GENERATOR;
    }

    @Override
    public String getUnlocalizedName() {
        return "thermalGenerator";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double currentHeat = MuseHeatUtils.getPlayerHeat(player);
        double maxHeat = MuseHeatUtils.getMaxHeat(player);
        if (player.world.getTotalWorldTime() % 20 == 0) {
            if (player.isBurning()) {
                ElectricItemUtils.givePlayerEnergy(player, 4 * ModuleManager.INSTANCE.computeModularProperty(item, THERMAL_ENERGY_GENERATION));
            } else if (currentHeat >= 200) {
                ElectricItemUtils.givePlayerEnergy(player, 2 * ModuleManager.INSTANCE.computeModularProperty(item, THERMAL_ENERGY_GENERATION));
            } else if ((currentHeat / maxHeat) >= 0.5) {
                ElectricItemUtils.givePlayerEnergy(player, ModuleManager.INSTANCE.computeModularProperty(item, THERMAL_ENERGY_GENERATION));
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