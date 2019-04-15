package net.machinemuse.powersuits.powermodule.energy_generation;


import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class KineticGeneratorModule extends PowerModuleBase implements IPlayerTickModule, IToggleableModule {
    public KineticGeneratorModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));

        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_GENERATION, 2000);
        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.KINETIC_ENERGY_GENERATION, 6000, "RF");
        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0.01);
        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0.49, "%");
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_ENERGY_GENERATION;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_KINETIC_GENERATOR__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (player.capabilities.isFlying || player.isRiding() || player.isElytraFlying() || !player.onGround)
            onPlayerTickInactive(player, itemStack);

        // really hate running this check on every tick but needed for player speed adjustments
        if (ElectricItemUtils.getPlayerEnergy(player) < ElectricItemUtils.getMaxPlayerEnergy(player)) {            // only fires if the sprint assist module isn't installed and active
            if (!ModuleManager.INSTANCE.itemHasActiveModule(itemStack, MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME)) {
                MovementManager.setMovementModifier(itemStack, 0, player);
            }

            // server side
            if (!player.world.isRemote &&
                    // every 20 ticks
                    (player.world.getTotalWorldTime() % 20) == 0 &&
                    // player not jumping, flying, or riding
                    player.onGround) {
                double distance = player.distanceWalkedModified - player.prevDistanceWalkedModified;
                ElectricItemUtils.givePlayerEnergy(player, (int) (distance * 10 *  ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.KINETIC_ENERGY_GENERATION)));
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        // only fire if sprint assist module not installed.
        if (!ModuleManager.INSTANCE.itemHasModule(item, MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME)) {
            MovementManager.setMovementModifier(item, 0, player);
        }
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.kineticGenerator;
    }
}