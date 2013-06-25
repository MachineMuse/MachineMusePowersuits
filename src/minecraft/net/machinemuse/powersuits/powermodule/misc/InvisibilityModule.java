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
import net.minecraft.util.StatCollector;

import java.util.Collection;
import java.util.List;

public class InvisibilityModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public static String MODULE_ACTIVE_CAMOUFLAGE;

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
    public String getName() {
        MODULE_ACTIVE_CAMOUFLAGE = StatCollector.translateToLocal("module.invisibility.name");
        return MODULE_ACTIVE_CAMOUFLAGE;
    }

    @Override
    public String getDescription() {
        return "Emit a hologram of your surroundings to make yourself almost imperceptible.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect invis = null;
        Collection<PotionEffect> effects = player.getActivePotionEffects();
        for (PotionEffect effect : effects) {
            if (effect.getAmplifier() == 81 && effect.getPotionID() == Potion.invisibility.id) {
                invis = effect;
                break;
            }
        }
        if (50 < totalEnergy) {
            if (invis == null || invis.getDuration() < 210) {
                player.addPotionEffect(new PotionEffect(Potion.invisibility.id, 500, 81));
                ElectricItemUtils.drainPlayerEnergy(player, 50);
            }
        } else {
            if (invis != null) {
                player.removePotionEffect(Potion.invisibility.id);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public String getTextureFile() {
        return "bluedrone";
    }
}
