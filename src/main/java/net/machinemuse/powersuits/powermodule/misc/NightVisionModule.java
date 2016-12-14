package net.machinemuse.powersuits.powermodule.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;

public class NightVisionModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_NIGHT_VISION = "Night Vision";

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
    public String getUnlocalizedName() { return "nightVision";
    }

    @Override
    public String getDescription() {
        return "A pair of augmented vision goggles to help you see at night and underwater.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect nightVision = null;
        if (player.isPotionActive(Potion.nightVision.id)) {
            nightVision = player.getActivePotionEffect(Potion.nightVision);
        }
        if (5 < totalEnergy) {
            if (nightVision == null || nightVision.getDuration() < 210) {
                player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 500, -3));
                ElectricItemUtils.drainPlayerEnergy(player, 5);
            }
        } else {
            onPlayerTickInactive(player, item);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        PotionEffect nightVision = null;
        if (player.isPotionActive(Potion.nightVision.id)) {
            nightVision = player.getActivePotionEffect(Potion.nightVision);
        }
        if (nightVision != null && nightVision.getAmplifier() == -3) {
            if (player.worldObj.isRemote) {
                player.removePotionEffectClient(Potion.nightVision.id);
            } else {
                player.removePotionEffect(Potion.nightVision.id);
            }
        }
    }

    @Override
    public String getTextureFile() {
        return "nightvision";
    }
}
