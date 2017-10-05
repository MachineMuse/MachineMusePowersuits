package net.machinemuse_old.powersuits.powermodule.misc;

import net.machinemuse_old.api.IModularItem;
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
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public class NightVisionModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_NIGHT_VISION = "Night Vision";
    private static Potion nightvision = Potion.getPotionFromResourceLocation("night_vision");

    public NightVisionModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_VISION;
    }

    @Override
    public String getDataName() {
        return MODULE_NIGHT_VISION;
    }

    @Override
    public String getUnlocalizedName() {
        return "nightVision";
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