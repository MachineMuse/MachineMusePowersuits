package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IPlayerTickModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.numina.player.NuminaPlayerUtils;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.machinemuse.powersuits.api.module.ModuleManager;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.MuseCommonStrings;
import net.machinemuse.powersuits.utils.MusePlayerUtils;
import net.machinemuse.powersuits.utils.PlayerWeightUtils;
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
            hasGlider = ModuleManager.INSTANCE.itemHasActiveModule(torso, GliderModule.MODULE_GLIDER);
        }
        if (sneakkey && player.motionY < -0.1 && (!hasGlider || forwardkey <= 0)) {
            double totalVelocity = Math.sqrt(player.motionX * player.motionX + player.motionZ * player.motionZ + player.motionY * player.motionY)
                    * MusePlayerUtils.getWeightPenaltyRatio(PlayerWeightUtils.getPlayerWeight(player), 25000);
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