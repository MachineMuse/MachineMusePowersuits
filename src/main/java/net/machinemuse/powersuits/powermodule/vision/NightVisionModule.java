package net.machinemuse.powersuits.powermodule.vision;

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
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class NightVisionModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    static final int powerDrain = 50;
    private static final Potion nightvision = MobEffects.NIGHT_VISION;

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
        if (player.world.isRemote)
            return;

        double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
        PotionEffect nightVisionEffect = player.isPotionActive(nightvision) ? player.getActivePotionEffect(nightvision) : null;

        if (totalEnergy > powerDrain) {
            if (nightVisionEffect == null || nightVisionEffect.getDuration() < 250 && nightVisionEffect.getAmplifier() == -3) {
                player.addPotionEffect(new PotionEffect(nightvision, 500, -3, false, false));
                ElectricItemUtils.drainPlayerEnergy(player, powerDrain);
            }
        } else
            onPlayerTickInactive(player, item);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        PotionEffect nightVisionEffect = null;
        if (player.isPotionActive(nightvision)) {
            nightVisionEffect = player.getActivePotionEffect(nightvision);
            if (nightVisionEffect.getAmplifier() == -3) {
                player.removePotionEffect(nightvision);
            }
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.nightVision;
    }
}