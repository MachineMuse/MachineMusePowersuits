package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import java.util.List;

public class WaterElectrolyzerModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {

    public static final String WATERBREATHING_ENERGY_CONSUMPTION = "Jolt Energy";
    public static final String MODULE_WATER_ELECTROLYZER = "Water Electrolyzer";

    public WaterElectrolyzerModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
        addBaseProperty(WaterElectrolyzerModule.WATERBREATHING_ENERGY_CONSUMPTION, 1000, "J");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public String getName() {
        return MODULE_WATER_ELECTROLYZER;
    }

    @Override
    public String getDescription() {
        return "When you run out of air, this module will jolt the water around you, electrolyzing a small bubble to breathe from.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double energyConsumption = ModuleManager.computeModularProperty(item, WATERBREATHING_ENERGY_CONSUMPTION);
        if (energy > energyConsumption && player.getAir() < 10) {
            Musique.playClientSound(SoundLoader.SOUND_ELECTROLYZER, 1.0f);
            ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
            player.setAir(300);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getTextureFile() {
        // TODO Auto-generated method stub
        return "waterelectrolyzer";
    }

}
