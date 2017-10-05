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
import net.machinemuse_old.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class MechanicalAssistance extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_MECH_ASSISTANCE = "Mechanical Assistance";
    public static final String ASSISTANCE = "Robotic Assistance";
    public static final String POWER_USAGE = "Power Usage";


    public MechanicalAssistance(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.artificialMuscle, 4));
        addTradeoffProperty(ASSISTANCE, POWER_USAGE, 500, " Joules/Tick");
        addTradeoffProperty(ASSISTANCE, MuseCommonStrings.WEIGHT, -10000);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ARMOR;
    }

    @Override
    public String getDataName() {
        return MODULE_MECH_ASSISTANCE;
    }

    @Override
    public String getUnlocalizedName() {
        return "mechAssistance";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(item, POWER_USAGE));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.mechAssistance;
    }
}