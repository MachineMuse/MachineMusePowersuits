package net.machinemuse.powersuits.tick;

import java.util.EnumSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;

/**
 * World tick handler for the mod. This is where you put code that should be
 * executed once every world tick. ~Not yet used~
 * 
 * @author MachineMuse
 * 
 */
public class WorldTickHandler implements ITickHandler {
	/**
	 * Called at the "start" phase of a tick
	 * 
	 * Multiple tick types may fire simultaneously- you will only be called once
	 * with all the firing ticks
	 */
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		World world;
		EntityPlayer player;
		// if (type.contains(TickType.WORLD)) {
		// world = (World) tickData[0];
		// }
		// TODO: Find a better way to handle this^

	}

	/**
	 * Called at the "end" phase of a tick
	 * 
	 * Multiple ticks may fire simultaneously- you will only be called once with
	 * all the firing ticks
	 */
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		// TODO Auto-generated method stub
	}

	/**
	 * Returns the list of ticks this tick handler is interested in receiving at
	 * the minute
	 */
	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(
				TickType.WORLD,
				TickType.PLAYER);
		// return EnumSet.of(TickType.WORLD);
	}

	/**
	 * A profiling label for this tick handler
	 */
	@Override
	public String getLabel() {
		return "MMMPS: World Tick";
	}

	/**
	 * Tick types:
	 * 
	 * WORLD - server and client side - Fired during the world evaluation loop
	 * 
	 * arg 0 : world object of the world that is ticking
	 * 
	 * 
	 * RENDER - client side Fired during the render processing phase
	 * 
	 * arg 0 : float "partial render time"
	 * 
	 * GUI - client side Fired during the render processing phase if a GUI is
	 * open
	 * 
	 * arg 0 : float "partial render time"
	 * 
	 * arg 1 : the open gui or null if no gui is open
	 * 
	 * 
	 * CLIENTGUI - client side - Fired during the client evaluation loop arg 0 :
	 * The open gui or null if no gui is open
	 * 
	 * WORLDLOAD - server side - Fired once as the world loads from disk
	 * 
	 * CLIENT - client side - Fired once per client tick loop.
	 * 
	 * PLAYER - client and server side. - Fired whenever the player's update
	 * loop runs.
	 * 
	 * arg 0 : the player
	 * 
	 * arg 1 : the world the player is in
	 * 
	 * 
	 * SERVER - server side - This is the server game tick. Fired once per tick
	 * loop on the server.
	 */

}
