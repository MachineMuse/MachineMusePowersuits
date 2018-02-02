package net.machinemuse.powersuits.common.items.modules.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.NuminaSettings;
import net.machinemuse.powersuits.client.control.PlayerInputMap;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.MPSConstants;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.numina.utils.player.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import java.util.List;

import static net.machinemuse.powersuits.common.MPSConstants.MODULE_JETPACK;

public class JetPackModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {

    public static final String JET_ENERGY_CONSUMPTION = "Jetpack Energy Consumption";
    public static final String JET_THRUST = "Jetpack Thrust";

    public JetPackModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 4));
        addBaseProperty(JET_ENERGY_CONSUMPTION, 0, "J/t");
        addBaseProperty(JET_THRUST, 0, "N");
        addTradeoffProperty("Thrust", JET_ENERGY_CONSUMPTION, 150);
        addTradeoffProperty("Thrust", JET_THRUST, 0.16);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_JETPACK;
    }

    @Override
    public String getUnlocalizedName() {
        return "jetpack";
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
                && ModuleManager.itemHasActiveModule(helmet, MPSConstants.MODULE_FLIGHT_CONTROL);
        double jetEnergy = 0;
        double thrust = 0;
        jetEnergy += ModuleManager.computeModularProperty(item, JET_ENERGY_CONSUMPTION);
        thrust += ModuleManager.computeModularProperty(item, JET_THRUST);

        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {

            thrust *= MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
            if (hasFlightControl && thrust > 0) {
                thrust = MusePlayerUtils.thrust(player, thrust, true);
                if (player.world.isRemote && NuminaSettings.useSounds) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else if (jumpkey ){//&& player.motionY < 0.5) {
                thrust = MusePlayerUtils.thrust(player, thrust, false);
                if (player.world.isRemote && NuminaSettings.useSounds) {

                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
                }
                ElectricItemUtils.drainPlayerEnergy(player, thrust * jetEnergy);
            } else {
                if (player.world.isRemote && NuminaSettings.useSounds) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
                }
            }
        } else {
            if (player.world.isRemote && NuminaSettings.useSounds) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.world.isRemote && NuminaSettings.useSounds) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.jetpack;
    }
}