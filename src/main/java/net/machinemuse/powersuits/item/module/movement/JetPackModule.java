package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.client.sound.SoundDictionary;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.machinemuse.utils.ElectricItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class JetPackModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String JET_ENERGY_CONSUMPTION = "Jetpack Energy Consumption";
    public static final String JET_THRUST = "Jetpack Thrust";

    public JetPackModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TORSOONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 4));
        addBasePropertyInt(JET_ENERGY_CONSUMPTION, 0, "J/t");
        addBasePropertyDouble(JET_THRUST, 0, "N");
        addTradeoffPropertyInt("Thrust", JET_ENERGY_CONSUMPTION, 150);
        addTradeoffPropertyDouble("Thrust", JET_THRUST, 0.16);
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.isInWater()) {
            return;
        }
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean jumpkey = movementInput.jumpKey;
        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        boolean hasFlightControl = helmet != null && helmet.getItem() instanceof IMuseItem
                && ModuleManager.getInstance().itemHasActiveModule(helmet, MPSModuleConstants.MODULE_FLIGHT_CONTROL);
        double jetEnergy = 0;
        double thrust = 0;
        jetEnergy += ModuleManager.getInstance().computeModularPropertyInteger(item, JET_ENERGY_CONSUMPTION);
        thrust += ModuleManager.getInstance().computeModularPropertyDouble(item, JET_THRUST);

        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {

            thrust *= WeightHelper.getWeightPenaltyRatio(WeightHelper.getPlayerWeight(player), 25000);
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
}