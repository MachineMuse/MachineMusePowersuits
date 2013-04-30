package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

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
    public String getName() {
        return MODULE_SWIM_BOOST;
    }

    @Override
    public String getDescription() {
        return "By refitting an ion thruster for underwater use, you may be able to add extra forward (or backward) thrust when underwater.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (player.isInWater() && !(player.isRiding())) {
            PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
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
                double swimAssistRate = ModuleManager.computeModularProperty(item, SWIM_BOOST_AMOUNT) * 0.05;
                double swimEnergyConsumption = ModuleManager.computeModularProperty(item, SWIM_BOOST_ENERGY_CONSUMPTION);
                if (swimEnergyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
                    Musique.playerSound(player, SoundLoader.SOUND_SWIMASSIST, 1.0f, 1.0f, true);
                    MusePlayerUtils.thrust(player, swimAssistRate, true);
                } else {
                    Musique.stopPlayerSound(player, SoundLoader.SOUND_SWIMASSIST);
                }
            } else {
                Musique.stopPlayerSound(player, SoundLoader.SOUND_SWIMASSIST);
            }
        } else {
            Musique.stopPlayerSound(player, SoundLoader.SOUND_SWIMASSIST);
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        Musique.stopPlayerSound(player, SoundLoader.SOUND_SWIMASSIST);
    }

    @Override
    public String getTextureFile() {
        return "swimboost";
    }

}
