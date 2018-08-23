package net.machinemuse.powersuits.powermodule.vision;

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
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class NightVisionModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    private static Potion nightvision = Potion.getPotionFromResourceLocation("night_vision");
    public NightVisionModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_VISION;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_NIGHT_VISION__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect nightVision = null;
        if (player.isPotionActive(nightvision)) {
            nightVision = player.getActivePotionEffect(nightvision);
        }
        if (totalEnergy > 5) {
            if (nightVision == null || nightVision.getDuration() < 210) {
                player.addPotionEffect(new PotionEffect(nightvision, 500, -3, false, false));
                ElectricItemUtils.drainPlayerEnergy(player, 5);
            }
        } else {
            onPlayerTickInactive(player, item);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        PotionEffect nightVision = null;
        if (player.isPotionActive(nightvision)) {
            nightVision = player.getActivePotionEffect(nightvision);
            if (nightVision.getAmplifier() == -3) {
                if (player.world.isRemote) {
                    player.removeActivePotionEffect(nightvision);
                } else {
                    player.removePotionEffect(nightvision);
                }
            }
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.nightVision;
    }
}