package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class JetPackModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public JetPackModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 4));
        addBasePropertyDouble(MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION, 0, "J/t");
        addBasePropertyDouble(MPSModuleConstants.JETPACK_THRUST, 0, "N");
        addTradeoffPropertyDouble("Thrust", MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION, 150);
        addTradeoffPropertyDouble("Thrust", MPSModuleConstants.JETPACK_THRUST, 0.16);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_JETPACK__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.isInWater()) {
            return;
        }
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        boolean hasFlightControl = helmet != null && helmet.getItem() instanceof IModularItem
                && ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME);
        double jetEnergy = 0;
        double thrust = 0;
        jetEnergy += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION);
        thrust += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETPACK_THRUST);

        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
            if (hasFlightControl && thrust > 0) {
                thrust = MusePlayerUtils.thrust(player, thrust, true);
                if (player.world.isRemote && NuminaConfig.useSounds()) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
            } else if (jumpkey ){//&& player.motionY < 0.5) {
                thrust = MusePlayerUtils.thrust(player, thrust, false);
                if (player.world.isRemote && NuminaConfig.useSounds()) {

                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
            } else {
                if (player.world.isRemote && NuminaConfig.useSounds()) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
                }
            }
        } else {
            if (player.world.isRemote && NuminaConfig.useSounds()) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.world.isRemote && NuminaConfig.useSounds()) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jetpack;
    }
}