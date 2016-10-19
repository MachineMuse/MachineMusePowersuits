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

public class InvisibilityModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static final String MODULE_ACTIVE_CAMOUFLAGE = "Active Camouflage";

    public InvisibilityModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 4));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_ACTIVE_CAMOUFLAGE;
    }

    @Override
    public String getUnlocalizedName() { return "invisibility";
    }

    @Override
    public String getDescription() {
        return "Emit a hologram of your surroundings to make yourself almost imperceptible.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect invis = null;
        if (player.isPotionActive(Potion.invisibility.id)) {
            invis = player.getActivePotionEffect(Potion.invisibility);
        }
        if (50 < totalEnergy) {
            if (invis == null || invis.getDuration() < 210) {
                player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 500, -3));
                ElectricItemUtils.drainPlayerEnergy(player, 50);
            }
        } else {
            onPlayerTickInactive(player, item);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        PotionEffect invis = null;
        if (player.isPotionActive(Potion.invisibility.id)) {
            invis = player.getActivePotionEffect(Potion.invisibility);
        }
        if (invis != null && invis.getAmplifier() == -3) {
            if (player.worldObj.isRemote) {
                player.removePotionEffectClient(Potion.invisibility.id);
            } else {
                player.removePotionEffect(Potion.invisibility.id);
            }
        }
    }

    @Override
    public String getTextureFile() {
        return "bluedrone";
    }
}
