package net.machinemuse.powersuits.item.module.energy;


import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.utils.heat.MuseHeatUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by User: Andrew2448
 * 6:43 PM 4/23/13
 */
public class ThermalGeneratorModule extends PowerModuleBase implements IPlayerTickModule {
    public static final String THERMAL_ENERGY_GENERATION = "Energy Generation";

    public ThermalGeneratorModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TORSOONLY, resourceDommain, UnlocalizedName);
        addBasePropertyDouble(THERMAL_ENERGY_GENERATION, 25);
        addBasePropertyDouble(MPSModuleConstants.WEIGHT, 1000);
        addTradeoffPropertyInt("Energy Generated", THERMAL_ENERGY_GENERATION, 10, " Joules");
        addTradeoffPropertyDouble("Energy Generated", MPSModuleConstants.WEIGHT, 1000, "g");
// Fixme: and maybe add options for a Magmatic Dynamo and maybe a stirling generator
//        if (ModCompatibility.isIndustrialCraftLoaded()) {
//            addInstallCost(ModCompatibility.getIC2Item("geothermalGenerator"));
//            addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
//        } else {
            addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
            addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.basicPlating, 1));
//        }
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENERGY;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double currentHeat = MuseHeatUtils.getPlayerHeatLegacy(player);
        double maxHeat = MuseHeatUtils.getMaxHeatLegacy(player);
        if (player.world.getTotalWorldTime() % 20 == 0) {
            if (player.isBurning()) {
                ElectricItemUtils.givePlayerEnergy(player, 4 * ModuleManager.getInstance().computeModularPropertyInteger(item, THERMAL_ENERGY_GENERATION));
            } else if (currentHeat >= 200) {
                ElectricItemUtils.givePlayerEnergy(player, 2 * ModuleManager.getInstance().computeModularPropertyInteger(item, THERMAL_ENERGY_GENERATION));
            } else if ((currentHeat / maxHeat) >= 0.5) {
                ElectricItemUtils.givePlayerEnergy(player, ModuleManager.getInstance().computeModularPropertyInteger(item, THERMAL_ENERGY_GENERATION));
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