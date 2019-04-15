package net.machinemuse.powersuits.powermodule.movement;

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
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

/**
 * Ported by leon on 10/18/16.
 */
public class SprintAssistModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public SprintAssistModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 4));

        addBasePropertyDouble(MPSModuleConstants.SPRINT_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.SPRINT_ASSIST, MPSModuleConstants.SPRINT_ENERGY_CONSUMPTION, 100);
        addBasePropertyDouble(MPSModuleConstants.SPRINT_SPEED_MULTIPLIER, .01, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.SPRINT_ASSIST, MPSModuleConstants.SPRINT_SPEED_MULTIPLIER, 2.49);

        addBasePropertyDouble(MPSModuleConstants.SPRINT_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.SPRINT_ENERGY_CONSUMPTION, 20);
        addBasePropertyDouble(MPSModuleConstants.SPRINT_FOOD_COMPENSATION, 0, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.COMPENSATION, MPSModuleConstants.SPRINT_FOOD_COMPENSATION, 1);

        addBasePropertyDouble(MPSModuleConstants.WALKING_ENERGY_CONSUMPTION, 0, "RF");
        addTradeoffPropertyDouble(MPSModuleConstants.WALKING_ASSISTANCE, MPSModuleConstants.WALKING_ENERGY_CONSUMPTION, 100);
        addBasePropertyDouble(MPSModuleConstants.WALKING_SPEED_MULTIPLIER, 0.01, "%");
        addTradeoffPropertyDouble(MPSModuleConstants.WALKING_ASSISTANCE, MPSModuleConstants.WALKING_SPEED_MULTIPLIER, 1.99);
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (player.capabilities.isFlying || player.isRiding() || player.isElytraFlying())
            onPlayerTickInactive(player, itemStack);

        ItemStack armorLeggings = player.getItemStackFromSlot(EntityEquipmentSlot.LEGS);
        // now you actually have to wear these to get the speed boost
        if (!armorLeggings.isEmpty() && armorLeggings.getItem() instanceof ItemPowerArmorLeggings) {
            double horzMovement = player.distanceWalkedModified - player.prevDistanceWalkedModified;
            double totalEnergy = ElectricItemUtils.getPlayerEnergy(player);
            if (horzMovement > 0) { // stop doing drain calculations when player hasn't moved
                if (player.isSprinting()) {
                    double exhaustion =  Math.round(horzMovement) * 0.1F;
                    double sprintCost = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SPRINT_ENERGY_CONSUMPTION);
                    if (sprintCost < totalEnergy) {
                        double sprintMultiplier = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SPRINT_SPEED_MULTIPLIER);
                        double exhaustionComp = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.SPRINT_FOOD_COMPENSATION);
                        ElectricItemUtils.drainPlayerEnergy(player, (int) (sprintCost * horzMovement * 5));
                        MovementManager.setMovementModifier(itemStack, sprintMultiplier, player);
                        player.getFoodStats().addExhaustion((float) (-1 * exhaustion * exhaustionComp));
                        player.jumpMovementFactor = player.getAIMoveSpeed() * .2f;
                    }
                } else {
                    double cost = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.WALKING_ENERGY_CONSUMPTION);
                    if (cost < totalEnergy) {
                        double walkMultiplier = ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.WALKING_SPEED_MULTIPLIER);
                        ElectricItemUtils.drainPlayerEnergy(player, (int) (cost * horzMovement * 5));
                        MovementManager.setMovementModifier(itemStack, walkMultiplier, player);
                        player.jumpMovementFactor = player.getAIMoveSpeed() * .2f;
                    }
                }
            }
        } else
            onPlayerTickInactive(player, itemStack);
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack itemStack) {
        MovementManager.setMovementModifier(itemStack, 0, player);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_SPRINT_ASSIST__DATANAME;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.sprintAssist;
    }
}