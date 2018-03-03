package net.machinemuse.powersuits.item.module.environmental;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

public class WaterElectrolyzerModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String WATERBREATHING_ENERGY_CONSUMPTION = "Jolt Energy";
    public WaterElectrolyzerModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.HEADONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.lvcapacitor, 1));
        addBasePropertyInt(WaterElectrolyzerModule.WATERBREATHING_ENERGY_CONSUMPTION, 400, "J");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_ENVIRONMENTAL;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double energy = ElectricItemUtils.getPlayerEnergy(player);
        int energyConsumption = ModuleManager.getInstance().computeModularPropertyInteger(item, WATERBREATHING_ENERGY_CONSUMPTION);
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