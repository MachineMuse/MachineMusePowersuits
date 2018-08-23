package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

public class GliderModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public GliderModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_GLIDER__DATANAME;
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        Vec3d playerHorzFacing = player.getLookVec();
        playerHorzFacing = new Vec3d(playerHorzFacing.x, 0, playerHorzFacing.z);
        playerHorzFacing.normalize();
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        boolean sneakkey = movementInput.sneakKey;
        float forwardkey = movementInput.forwardKey;
        ItemStack torso = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        boolean hasParachute = false;
        NuminaPlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IModularItem) {
            hasParachute = ModuleManager.INSTANCE.itemHasActiveModule(torso, MPSModuleConstants.MODULE_PARACHUTE__DATANAME);
        }
        if (sneakkey && player.motionY < 0 && (!hasParachute || forwardkey > 0)) {
            if (player.motionY < -0.1) {
                float vol = (float)( player.motionX*player.motionX + player.motionZ * player.motionZ);
                double motionYchange = Math.min(0.08, -0.1 - player.motionY);
                player.motionY += motionYchange;
                player.motionX += playerHorzFacing.x * motionYchange;
                player.motionZ += playerHorzFacing.z * motionYchange;

                // sprinting speed
                player.jumpMovementFactor += 0.03f;
            }
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.glider;
    }
}