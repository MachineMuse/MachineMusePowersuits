package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.item.powersuits.module.PowerModuleBase;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.client.sound.Musique;
import net.machinemuse.numina.common.config.NuminaConfig;
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
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

public class SwimAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String SWIM_BOOST_AMOUNT = "Underwater Movement Boost";
    public static final String SWIM_BOOST_ENERGY_CONSUMPTION = "Swim Boost Energy Consumption";

    public SwimAssistModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.LEGSONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addTradeoffPropertyDouble("Thrust", SWIM_BOOST_ENERGY_CONSUMPTION, 100, "J");
        addTradeoffPropertyDouble("Thrust", SWIM_BOOST_AMOUNT, 1, "m/s");
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.isInWater() && !(player.isRiding())) {
            PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
            boolean jumpkey = movementInput.jumpKey;
            boolean sneakkey = movementInput.sneakKey;
            float forwardkey = movementInput.forwardKey;
            float strafekey = movementInput.strafeKey;
            if (forwardkey != 0 || strafekey != 0 || jumpkey || sneakkey) {
                double moveRatio = 0;
                if (forwardkey != 0) {
                    moveRatio += forwardkey * forwardkey;
                }
                if (strafekey != 0) {
                    moveRatio += strafekey * strafekey;
                }
                if (jumpkey || sneakkey) {
                    moveRatio += 0.2 * 0.2;
                }
                double swimAssistRate = ModuleManager.getInstance().computeModularPropertyDouble(item, SWIM_BOOST_AMOUNT) * 0.05 * moveRatio;;
                double swimEnergyConsumption = ModuleManager.getInstance().computeModularPropertyDouble(item, SWIM_BOOST_ENERGY_CONSUMPTION);
                if (swimEnergyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
                    if (player.world.isRemote && NuminaConfig.useSounds()) {
                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
                    }
                    MusePlayerUtils.thrust(player, swimAssistRate, true);
                } else {
                    if (player.world.isRemote && NuminaConfig.useSounds()) {
                        Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
                    }
                }
            } else {
                if (player.world.isRemote && NuminaConfig.useSounds()) {
                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
                }
            }
        } else {
            if (player.world.isRemote && NuminaConfig.useSounds()) {
                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (player.world.isRemote && NuminaConfig.useSounds()) {
            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.swimAssist;
    }
}