package net.machinemuse.powersuits.item.module.movement;

import net.machinemuse.powersuits.item.module.PowerModuleBase;
import net.machinemuse.numina.api.item.IMuseItem;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.api.module.ModuleManager;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.module.helpers.WeightHelper;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.utils.MuseItemUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class ParachuteModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public ParachuteModule(String resourceDommain, String UnlocalizedName) {
        super(EnumModuleTarget.TORSOONLY, resourceDommain, UnlocalizedName);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
    }

    @Override
    public String getCategory() {
        return MPSModuleConstants.CATEGORY_MOVEMENT;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        float forwardkey = movementInput.forwardKey;
        boolean sneakkey = movementInput.sneakKey;
        ItemStack torso = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        boolean hasGlider = false;
        NuminaPlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IMuseItem) {
            hasGlider = ModuleManager.getInstance().itemHasActiveModule(torso, MPSModuleConstants.MODULE_GLIDER);
        }
        if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
            double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
                    * WeightHelper.getWeightPenaltyRatio(WeightHelper.getPlayerWeight(player), 25000);
            if (totalVelocity > 0) {
                player.motionX = player.motionX * 0.1 / totalVelocity;
                player.motionY = player.motionY * 0.1 / totalVelocity;
                player.motionZ = player.motionZ * 0.1 / totalVelocity;
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