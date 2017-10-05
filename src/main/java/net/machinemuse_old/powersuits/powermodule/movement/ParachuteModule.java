package net.machinemuse_old.powersuits.powermodule.movement;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.ModuleManager;
import net.machinemuse_old.api.moduletrigger.IPlayerTickModule;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;
import net.machinemuse_old.general.gui.MuseIcon;
import net.machinemuse_old.numina.player.NuminaPlayerUtils;
import net.machinemuse_old.powersuits.control.PlayerInputMap;
import net.machinemuse_old.powersuits.item.ItemComponent;
import net.machinemuse_old.powersuits.powermodule.PowerModuleBase;
import net.machinemuse_old.utils.MuseCommonStrings;
import net.machinemuse_old.utils.MuseItemUtils;
import net.machinemuse_old.utils.MusePlayerUtils;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ParachuteModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_PARACHUTE = "Parachute";

    public ParachuteModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.parachute, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getDataName() {
        return MODULE_PARACHUTE;
    }

    @Override
    public String getUnlocalizedName() {
        return "parachute";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.getCommandSenderEntity().getName());
        float forwardkey = movementInput.forwardKey;
        boolean sneakkey = movementInput.sneakKey;
        ItemStack torso = player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
        boolean hasGlider = false;
        NuminaPlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IModularItem) {
            hasGlider = ModuleManager.itemHasActiveModule(torso, GliderModule.MODULE_GLIDER);
        }
        if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
            double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
                    * MusePlayerUtils.getWeightPenaltyRatio(MuseItemUtils.getPlayerWeight(player), 25000);
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