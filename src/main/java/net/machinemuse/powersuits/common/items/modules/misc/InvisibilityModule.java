package net.machinemuse.powersuits.common.items.modules.misc;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.client.events.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_ACTIVE_CAMOUFLAGE;

public class InvisibilityModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {

    private final Potion invisibility = Potion.getPotionFromResourceLocation("invisibility");

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
    public String getUnlocalizedName() {
        return "invisibility";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect invis = null;
        if (player.isPotionActive(invisibility)) {
            invis = player.getActivePotionEffect(invisibility);
        }
        if (50 < totalEnergy) {
            if (invis == null || invis.getDuration() < 210) {
                player.addPotionEffect(new PotionEffect(invisibility, 500, -3, false, false));
                ElectricItemUtils.drainPlayerEnergy(player, 50);
            }
        } else {
            onPlayerTickInactive(player, item);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        PotionEffect invis = null;
        if (player.isPotionActive(invisibility)) {
            invis = player.getActivePotionEffect(invisibility);
        }
        if (invis != null && invis.getAmplifier() == -3) {
            if (player.world.isRemote) {
                player.removeActivePotionEffect(invisibility);
            } else {
                player.removePotionEffect(invisibility);
            }
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.invisibility;
    }
}