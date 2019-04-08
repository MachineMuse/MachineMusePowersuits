package net.machinemuse.powersuits.item.module.energy.generation;


import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.item.module.ItemAbstractPowerModule;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ItemModuleGeneratorKinetic extends ItemAbstractPowerModule implements IPlayerTickModule, IToggleableModule {
    public ItemModuleGeneratorKinetic(String regName) {
        super(regName, EnumModuleTarget.TORSOONLY, EnumModuleCategory.CATEGORY_ENERGY_GENERATION);
//        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_GENERATION, 2000);
//        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.KINETIC_ENERGY_GENERATION, 6000, "RF");
//        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0.01);
//        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0.49, "%");
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
//        if (player.capabilities.isFlying || player.isRiding() || player.isElytraFlying() || !player.onGround)
//            onPlayerTickInactive(player, itemStack);
//
//        // really hate running this check on every tick but needed for player speed adjustments
//        if (ElectricItemUtils.getPlayerEnergy(player) < ElectricItemUtils.getMaxPlayerEnergy(player)) {            // only fires if the sprint assist module isn't installed and active
//            if (!ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME)) {
//                MovementManager.setMovementModifier(itemStack, 0, player);
//            }
//
//            // server side
//            if (!player.world.isRemote &&
//                    // every 20 ticks
//                    (player.world.getTotalWorldTime() % 20) == 0 &&
//                    // player not jumping, flying, or riding
//                    player.onGround) {
//                double distance = player.distanceWalkedModified - player.prevDistanceWalkedModified;
//                ElectricItemUtils.givePlayerEnergy(player, (int) (distance * 10 *  ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.KINETIC_ENERGY_GENERATION)));
//            }
//        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
//        // only fire if sprint assist module not installed.
//        if (!ModuleManager.INSTANCE.itemHasModule(item, MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME)) {
//            MovementManager.setMovementModifier(item, 0, player);
//        }
    }
}