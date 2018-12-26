package net.machinemuse.powersuits.powermodule.energy_generation;


import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.numina.utils.energy.ElectricItemUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
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
        addBasePropertyDouble(MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0);
        addTradeoffPropertyDouble(MPSModuleConstants.ENERGY_GENERATED, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE, 0.5, "%");
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
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        // really hate running this check on every tick but needed for player speed adjustments
        if (ElectricItemUtils.getPlayerEnergy(player) < ElectricItemUtils.getMaxPlayerEnergy(player)) {
            // reduce player speed according to Kinetic Energy Generator setting
            double movementResistance = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_ENERGY_MOVEMENT_RESISTANCE);
            if (movementResistance > 0) {
                player.motionX *= movementResistance;
                player.motionZ *= movementResistance;
            }

            // server side
            if (!player.world.isRemote &&
                    // every 20 ticks
                    (player.world.getTotalWorldTime() % 20) == 0 &&
                    // player not jumping
                    player.onGround && !player.capabilities.isFlying && !player.isRiding() && !player.isElytraFlying() &&
                    // player not swimming or w/e
                    !player.isInWater()) {
                double distance = player.distanceWalkedModified - player.prevDistanceWalkedModified;
                ElectricItemUtils.givePlayerEnergy(player, (int) (distance * 10 *  ModuleManager.INSTANCE.getOrSetModularPropertyDouble(item, MPSModuleConstants.KINETIC_ENERGY_GENERATION)));
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.kineticGenerator;
    }
}