package net.machinemuse.powersuits.powermodule.movement;

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
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Created by Eximius88 on 1/17/14.
 */
public class MechanicalAssistance extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_MECH_ASSISTANCE = "Mechanical Assistance";
    public static final String ASSISTANCE = "Robotic Assistance";
    public static final String POWER_USAGE = "Power Usage";


    public MechanicalAssistance(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.artificialMuscle, 4));
        addTradeoffProperty(ASSISTANCE, POWER_USAGE, 500, " Joules/Tick");
        addTradeoffProperty(ASSISTANCE, MPSModuleConstants.WEIGHT, -10000);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ARMOR;
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
        ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.INSTANCE.computeModularProperty(item, POWER_USAGE));
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.mechAssistance;
    }
}