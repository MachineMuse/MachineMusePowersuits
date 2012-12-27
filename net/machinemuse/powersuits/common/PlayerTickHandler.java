/**
 * 
 */
package net.machinemuse.powersuits.common;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * Tick handler for Player ticks. This is where we compute all the updates that
 * should go every tick.
 * 
 * @author MachineMuse
 */
public class PlayerTickHandler implements ITickHandler {
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		List<NBTTagCompound> playerAugs = ItemUtils
				.getPlayerAugs(player);
		float totalEnergy = 0;
		float totalWeight = 0;
		Iterator<NBTTagCompound> iter = playerAugs.iterator();

		if (totalWeight > 25) {
			player.motionX *= 25 / totalWeight;
			player.motionZ *= 25 / totalWeight;
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		EntityPlayer player = toPlayer(tickData[0]);
		List<ItemStack> stacks = ItemUtils
				.getModularItemsInInventory(player.inventory);

	}

	public static World toWorld(Object data) {
		World world = null;
		try {
			world = (World) data;
		} catch (ClassCastException e) {
			MuseLogger.logDebug(
					"MMMPS: Player tick handler received invalid World object");
			e.printStackTrace();
		}
		return world;
	}

	public static EntityPlayer toPlayer(Object data) {
		EntityPlayer player = null;
		try {
			player = (EntityPlayer) data;
		} catch (ClassCastException e) {
			MuseLogger
					.logDebug(
					"MMMPS: Player tick handler received invalid Player object");
			e.printStackTrace();
		}
		return player;
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
		return "MMMPS PlayerTickHandler";
	}

}
