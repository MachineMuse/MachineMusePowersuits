package net.machinemuse.powersuits.powermodule.movement;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.control.PlayerInputMap;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

import java.util.List;

public class GliderModule extends PowerModuleBase implements IToggleableModule, IPlayerTickModule {
    public static final String MODULE_GLIDER = "Glider";

    @Override
    public String getTextureFile() {
        return "glider";
    }

    public GliderModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.gliderWing, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_MOVEMENT;
    }

    @Override
    public String getName() {
        return MODULE_GLIDER;
    }

    @Override
    public String getDescription() {
        return "Tack on some wings to turn downward into forward momentum. Press sneak+forward while falling to activate.";
    }

    @Override
    public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        Vec3 playerHorzFacing = player.getLookVec();
        playerHorzFacing.yCoord = 0;
        playerHorzFacing.normalize();
        PlayerInputMap movementInput = PlayerInputMap.getInputMapFor(player.username);
        boolean sneakkey = movementInput.sneakKey;
        float forwardkey = movementInput.forwardKey;
        ItemStack torso = player.getCurrentArmor(2);
        boolean hasParachute = false;
        MusePlayerUtils.resetFloatKickTicks(player);
        if (torso != null && torso.getItem() instanceof IModularItem) {
            hasParachute = MuseItemUtils.itemHasActiveModule(torso, ParachuteModule.MODULE_PARACHUTE);
        }
        if (sneakkey && player.motionY < 0 && (!hasParachute || forwardkey > 0)) {
            if (player.motionY < -0.1) {
                float vol = (float)( player.motionX*player.motionX + player.motionZ * player.motionZ);
                double motionYchange = Math.min(0.08, -0.1 - player.motionY);
                player.motionY += motionYchange;
                player.motionX += playerHorzFacing.xCoord * motionYchange;
                player.motionZ += playerHorzFacing.zCoord * motionYchange;

                // sprinting speed
                player.jumpMovementFactor += 0.03f;
            }
        } else {
        }
    }

    @Override
    public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
    }

}
