package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.control.PlayerInputMap;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IPlayerTickModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ParachuteModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public ParachuteModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_PARACHUTE__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack itemStack) {
        if (!itemStack.isEmpty() && itemStack.getItem() instanceof ItemPowerArmorChestplate
                && ItemStack.areItemStackTagsEqual(itemStack, player.getItemStackFromSlot(EntityEquipmentSlot.CHEST))) {
            PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
            float forwardkey = movementInput.forwardKey;
            boolean sneakkey = movementInput.sneakKey;
            boolean hasGlider = false;
            NuminaPlayerUtils.resetFloatKickTicks(player);
            if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
                double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY);
                if (totalVelocity > 0) {
                    player.motionX = player.motionX * 0.1 / totalVelocity;
                    player.motionY = player.motionY * 0.1 / totalVelocity;
                    player.motionZ = player.motionZ * 0.1 / totalVelocity;
                }
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.parachute;
    }
}