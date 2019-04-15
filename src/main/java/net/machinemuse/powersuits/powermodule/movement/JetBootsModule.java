package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.control.PlayerMovementInputWrapper;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class JetBootsModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {


    public JetBootsModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 2));
        addBasePropertyDouble(MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION, 0);
        addBasePropertyDouble(MPSModuleConstants.JETBOOTS_THRUST, 0);
        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION, 750, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETBOOTS_THRUST, 0.08);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_JETBOOTS__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.isInWater())
            return;

        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        boolean hasFlightControl = ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME);
        double jetEnergy = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION);
        double thrust = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETBOOTS_THRUST);

        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
        // if player has enough energy to fly
        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
            if (hasFlightControl && thrust > 0) {
                thrust = MovementManager.thrust(player, thrust, true);
                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
            } else if (playerInput.jumpKey && player.motionY < 0.5) {
                thrust = MovementManager.thrust(player, thrust, false);
                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
            } else {
                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
                }
            }
        } else {
            if (player.world.isRemote && NuminaConfig.useSounds()) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.world.isRemote && NuminaConfig.useSounds()) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jetBoots;
    }
}
