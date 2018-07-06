package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.sound.Musique;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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
    public String getDataName() {
        return MODULE_WATER_ELECTROLYZER;
    }

    @Override
    public String getUnlocalizedName() {
        return "waterElectrolyzer";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        double energyConsumption = ModuleManager.INSTANCE.computeModularProperty(item, WATERBREATHING_ENERGY_CONSUMPTION);
        if (energy > energyConsumption && player.getAir() < 10) {

            if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
                Musique.playClientSound(SoundDictionary.SOUND_EVENT_ELECTROLYZER, SoundCategory.PLAYERS, 1.0f, player.getPosition());
            }
            ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
            player.setAir(300);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.waterElectrolyzer;
    }
}