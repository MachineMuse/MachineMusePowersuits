package net.machinemuse_old.powersuits.powermodule.armor;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.api.moduletrigger.IPlayerTickModule;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.ElectricItemUtils;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseHeatUtils;
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class NitrogenCoolingSystem extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_NITROGEN_COOLING_SYSTEM = "Liquid Nitrogen Cooling System";
    public static final String COOLING_BONUS = "Cooling Bonus";
    public static final String ENERGY = "Energy Consumption";

    public NitrogenCoolingSystem(List<IModularItem> validItems) {
        super(validItems);
        //addInstallCost(new ItemStack(Item.netherStar, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.liquidNitrogen, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.rubberHose, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.computerChip, 2));
        addTradeoffProperty("Power", COOLING_BONUS, 7, "%");
        addTradeoffProperty("Power", ENERGY, 16, "J/t");
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double heatBefore = MuseHeatUtils.getPlayerHeat(player);
        MuseHeatUtils.coolPlayer(player, 0.210 * ModuleManager.computeModularProperty(item, COOLING_BONUS));
        double cooling = heatBefore - MuseHeatUtils.getPlayerHeat(player);
        ElectricItemUtils.drainPlayerEnergy(player, cooling * ModuleManager.computeModularProperty(item, ENERGY));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getDataName() {
        return MODULE_NITROGEN_COOLING_SYSTEM;
    }

    @Override
    public String getUnlocalizedName() {
        return "nitrogenCoolingSystem";
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.nitrogenCoolingSystem;
    }
}