package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleJetBoots extends ItemAbstractPowerModule implements IToggleableModule, IPlayerTickModule {
    public ItemModuleJetBoots(String regName) {
        super(regName, EnumModuleTarget.FEETONLY, EnumModuleCategory.CATEGORY_MOVEMENT);
//        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.ionThruster, 2));
//        addBasePropertyDouble(MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION, 0);
//        addBasePropertyDouble(MPSModuleConstants.JETBOOTS_THRUST, 0);
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION, 750, "RF");
//        addTradeoffPropertyDouble(MPSModuleConstants.THRUST, MPSModuleConstants.JETBOOTS_THRUST, 0.08);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
//        if (player.isInWater())
//            return;
//
//        ItemStack helmet = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
//        boolean hasFlightControl = ModuleManager.INSTANCE.itemHasActiveModule(helmet, MPSModuleConstants.MODULE_FLIGHT_CONTROL__DATANAME);
//        double jetEnergy = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETBOOTS_ENERGY_CONSUMPTION);
//        double thrust = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.JETBOOTS_THRUST);
//
//        PlayerMovementInputWrapper.PlayerMovementInput playerInput = PlayerMovementInputWrapper.get(player);
//        // if player has enough energy to fly
//        if (jetEnergy < ElectricItemUtils.getPlayerEnergy(player)) {
//            if (hasFlightControl && thrust > 0) {
//                thrust = MovementManager.thrust(player, thrust, true);
//                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
//                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
//                }
//                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
//            } else if (playerInput.jumpKey && player.motionY < 0.5) {
//                thrust = MovementManager.thrust(player, thrust, false);
//                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
//                    Musique.playerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS, SoundCategory.PLAYERS, (float) (thrust * 12.5), 1.0f, true);
//                }
//                ElectricItemUtils.drainPlayerEnergy(player, (int) (thrust * jetEnergy));
//            } else {
//                if ((player.world.isRemote) && NuminaConfig.useSounds()) {
//                    Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
//                }
//            }
//        } else {
//            if (player.world.isRemote && NuminaConfig.useSounds()) {
//                Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        if (player.world.isRemote && NuminaConfig.useSounds()) {
//            Musique.stopPlayerSound(player, SoundDictionary.SOUND_EVENT_JETBOOTS);
//        }
    }
}
