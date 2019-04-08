package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleSwimAssist extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleSwimAssist(String regName) {
        super(regName, EnumModuleTarget.LEGSONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 1));
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 2));
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.SWIM_BOOST_ENERGY_CONSUMPTION, 1000, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.SWIM_BOOST_AMOUNT, 1, "m/s");
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (player.isInWater() && !(player.isRiding())) {
//            PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//            if (playerInput.moveForward != 0 || playerInput.moveStrafe != 0 || playerInput.jumpKey || playerInput.sneakKey) {
//                double moveRatio = 0;
//                if (playerInput.moveForward != 0) {
//                    moveRatio += playerInput.moveForward * playerInput.moveForward;
//                }
//                if (playerInput.moveStrafe != 0) {
//                    moveRatio += playerInput.moveStrafe * playerInput.moveStrafe;
//                }
//                if (playerInput.jumpKey || playerInput.sneakKey) {
//                    moveRatio += 0.2 * 0.2;
//                }
//                double swimAssistRate = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.SWIM_BOOST_AMOUNT) * 0.05 * moveRatio;
//                double swimEnergyConsumption = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.SWIM_BOOST_ENERGY_CONSUMPTION);
//                if (swimEnergyConsumption < ElectricItemUtils.getPlayerEnergy(player)) {
//                    if (player.world.isRemote && NuminaConfig.useSounds()) {
//                        Musique.playerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST, SoundCategory.PLAYERS, 1.0f, 1.0f, true);
//                    }
//                    MovementManager.thrust(player, swimAssistRate, true);
//                } else {
//                    if (player.world.isRemote && NuminaConfig.useSounds()) {
//                        Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
//                    }
//                }
//            } else {
//                if (player.world.isRemote && NuminaConfig.useSounds()) {
//                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
//                }
//            }
//        } else {
//            if (player.world.isRemote && NuminaConfig.useSounds()) {
//                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (player.world.isRemote && NuminaConfig.useSounds()) {
//            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_SWIM_ASSIST);
//        }
    }
}