package net.machinemuse.powersuits.powermodule.special;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class InvisibilityModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    private final Potion invisibility = Potion.getPotionFromResourceLocation("invisibility");

    public InvisibilityModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.laserHologram, 4));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.fieldEmitter, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 2));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_ACTIVE_CAMOUFLAGE__DATANAME;
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