package net.machinemuse_old.powersuits.powermodule.movement;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.api.moduletrigger.IPlayerTickModule;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.general.sound.SoundDictionary;
import net.machinemuse_old.numina.common.NuminaConfig;
import net.machinemuse_old.numina.sound.Musique;
import net.machinemuse_old.powersuits.control.PlayerInputMap;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.ElectricItemUtils;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.machinemuse_old.utils.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;

import java.util.List;

public class SwimAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_SWIM_BOOST = "Swim Boost";
    public static final String SWIM_BOOST_AMOUNT = "Underwater Movement Boost";
    public static final String SWIM_BOOST_ENERGY_CONSUMPTION = "Swim Boost Energy Consumption";

    public SwimAssistModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
        addTradeoffProperty("Thrust", SWIM_BOOST_ENERGY_CONSUMPTION, 100, "J");
        addTradeoffProperty("Thrust", SWIM_BOOST_AMOUNT, 1, "m/s");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_SWIM_BOOST;
    }

    @Override
    public String getUnlocalizedName() { return "swimAssist";
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
                double swimAssistRate = ModuleManager.computeModularProperty(item, SWIM_BOOST_AMOUNT) * 0.05 * moveRatio;;
                double swimEnergyConsumption = ModuleManager.computeModularProperty(item, SWIM_BOOST_ENERGY_CONSUMPTION);
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