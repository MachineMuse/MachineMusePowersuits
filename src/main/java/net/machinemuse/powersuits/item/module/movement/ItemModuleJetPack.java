package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleJetPack extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleJetPack(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 4));
//        addBasePropertyDouble(MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION, 0, "RF/t");
//        addBasePropertyDouble(MPSModuleConstants.JETPACK_THRUST, 0, "N");
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION, 1500);
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETPACK_THRUST, 0.16);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (player.isInWater())
//            return;
//
//        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
//        boolean hasFlightControl = ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME);
//        double jetEnergy = 0;
//        double thrust = 0;
//        jetEnergy += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETPACK_ENERGY_CONSUMPTION);
//        thrust += ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETPACK_THRUST);
//
//        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
//            if (hasFlightControl && thrust > 0) {
//                thrust = MovementManager.thrust(player, thrust, true);
//                if (player.world.isRemote && NuminaConfig.useSounds()) {
//                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
//                }
//                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
//            } else if (playerInput.jumpKey) {//&& player.motionY < 0.5) {
//                thrust = MovementManager.thrust(player, thrust, false);
//                if (player.world.isRemote && NuminaConfig.useSounds()) {
//                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETPACK, SoundCategory.PLAYERS, (float) (thrust * 6.25), 1.0f, true);
//                }
//                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
//            } else {
//                if (player.world.isRemote && NuminaConfig.useSounds()) {
//                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
//                }
//            }
//        } else {
//            if (player.world.isRemote && NuminaConfig.useSounds()) {
//                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (player.world.isRemote && NuminaConfig.useSounds()) {
//            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETPACK);
//        }
    }
}