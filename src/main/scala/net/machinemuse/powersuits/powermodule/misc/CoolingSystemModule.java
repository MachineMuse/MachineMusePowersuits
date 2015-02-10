package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import java.util.List;

public class CoolingSystemModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_COOLING_SYSTEM = "Cooling System";
    public static final String COOLING_BONUS = "Cooling Bonus";
    public static final String ENERGY = "Cooling System Energy Consumption";

    public CoolingSystemModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.ender_eye, 4));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addTradeoffProperty("Power", COOLING_BONUS, 4, "%");
        addTradeoffProperty("Power", ENERGY, 10, "J/t");
        addPropertyLocalString(ENERGY, StatCollector.translateToLocal("module.coolingSystem.energy"));
        addPropertyLocalString(COOLING_BONUS, StatCollector.translateToLocal("module.coolingSystem.bonus"));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MODULE_COOLING_SYSTEM;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.coolingSystem.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.coolingSystem.desc");
    }

    @Override
    public String getTextureFile() {
        return "coolingsystem";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double heatBefore = MuseHeatUtils.getPlayerHeat(player);
        MuseHeatUtils.coolPlayer(player, 0.1 * ModuleManager.computeModularProperty(item, COOLING_BONUS));
        double cooling = heatBefore - MuseHeatUtils.getPlayerHeat(player);
        ElectricItemUtils.drainPlayerEnergy(player, cooling * ModuleManager.computeModularProperty(item, ENERGY));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

}
