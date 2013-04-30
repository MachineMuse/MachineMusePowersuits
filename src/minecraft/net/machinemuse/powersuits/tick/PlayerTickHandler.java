/**
 *
 */
package net.machinemuse.powersuits.tick;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.general.MuseLogger;
import net.machinemuse.general.sound.Musique;
import net.machinemuse.general.sound.SoundLoader;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.utils.MuseHeatUtils;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseMathUtils;
import net.machinemuse.utils.MusePlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;

/**
 * Tick handler for Player update step. tickStart() is queued before the entity
 * is updated, and tickEnd() is queued afterwards.
 * <p/>
 * Player update step: "Called to update the entity's position/logic."
 * <p/>
 * tickData: EntityPlayer of the entity being updated.
 *
 * @author MachineMuse
 */
public class PlayerTickHandler implements ITickHandler {

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        EntityPlayer player = MusePlayerUtils.toPlayer(tickData[0]);
        handle(player);

    }

    // int gliderTicker = 0, swimTicker = 0;

    public void handle(EntityPlayer player) {
        List<ItemStack> modularItemsEquipped = MuseItemUtils.modularItemsEquipped(player);

        double totalWeight = MuseItemUtils.getPlayerWeight(player);
        double weightCapacity = 25000;

        // double totalEnergyDrain = 0;

        for (ItemStack stack : modularItemsEquipped) {
            if (stack.getTagCompound().hasKey("ench")) {
                stack.getTagCompound().removeTag("ench");
            }
        }

        boolean foundItem = modularItemsEquipped.size() > 0;
        if (foundItem) {
            for (IPlayerTickModule module : ModuleManager.getPlayerTickModules()) {
                for (ItemStack itemStack : modularItemsEquipped) {
                    if (module.isValidForItem(itemStack, player)) {
                        if (MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
                            module.onPlayerTickActive(player, itemStack);
                        } else {
                            module.onPlayerTickInactive(player, itemStack);
                        }
                    }
                }
            }
            player.fallDistance = (float) MovementManager.computeFallHeightFromVelocity(MuseMathUtils.clampDouble(player.motionY, -1000.0, 0.0));

            // Weight movement penalty
            if (totalWeight > weightCapacity) {
                player.motionX *= weightCapacity / totalWeight;
                player.motionZ *= weightCapacity / totalWeight;
            }
            MuseHeatUtils.coolPlayer(player, MusePlayerUtils.getPlayerCoolingBasedOnMaterial(player));
            double maxHeat = MuseHeatUtils.getMaxHeat(player);
            double currHeat = MuseHeatUtils.getPlayerHeat(player);
            if (currHeat > maxHeat) {
                player.attackEntityFrom(MuseHeatUtils.overheatDamage, (int) Math.sqrt(currHeat - maxHeat) / 4);
                player.setFire(1);
            } else {
                player.extinguish();
            }
            double velsq2 = MuseMathUtils.sumsq(player.motionX, player.motionY, player.motionZ) - 0.5;
            if(player.isAirBorne && velsq2 > 0) {
                Musique.playerSound(player, SoundLoader.SOUND_GLIDER, (float) (velsq2/3), 1.0f, true);
            } else {
                Musique.stopPlayerSound(player, SoundLoader.SOUND_GLIDER);
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
        EntityPlayer player = MusePlayerUtils.toPlayer(tickData[0]);
        List<ItemStack> stacks = MuseItemUtils.getModularItemsInInventory(player.inventory);

    }

    public static World toWorld(Object data) {
        World world = null;
        try {
            world = (World) data;
        } catch (ClassCastException e) {
            MuseLogger.logError("MMMPS: Player tick handler received invalid World object");
            e.printStackTrace();
        }
        return world;
    }

    /**
     * Type of tick handled by this handler
     */
    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.PLAYER);
    }

    /**
     * Profiling label for this handler
     */
    @Override
    public String getLabel() {
        return "MMMPS: Player Tick";
    }

}
