package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.network.MusePacketHandler;
import net.machinemuse.powersuits.tick.PlayerTickHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MovingObjectPosition;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

/**
 * Server side of the CommonProxy/ClientProxy paradigm. Provides functions which
 * the ClientProxy will override if the behaviour is different for client and
 * server.
 * 
 * @author MachineMuse
 * 
 */
public class CommonProxy {
	public static String ITEMS_PNG = "/tutorial/generic/items.png";
	public static String BLOCK_PNG = "/tutorial/generic/block.png";

	public static MusePacketHandler packetHandler;
	public static PlayerTickHandler playerTickHandler;

	/**
	 * Only the client needs to register renderers.
	 */
	public void registerRenderers() {}

	/**
	 * Register the server-side tickhandler and packethandler.
	 */
	public void registerHandlers() {
		playerTickHandler = new PlayerTickHandler();
		TickRegistry.registerTickHandler(playerTickHandler, Side.SERVER);

		packetHandler = new MusePacketHandler();
		packetHandler.register();
	}

	public void postInit() {
		// TODO Auto-generated method stub

	}

	public void teleportEntity(EntityPlayer entityPlayer, MovingObjectPosition hitMOP) {
		if (hitMOP != null && entityPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) entityPlayer;
			if (!player.playerNetServerHandler.connectionClosed) {
				switch (hitMOP.typeOfHit) {
				case ENTITY:
					player.setPositionAndUpdate(hitMOP.entityHit.posX, hitMOP.entityHit.posY, hitMOP.entityHit.posZ);
					break;
				case TILE:
					int hitx = hitMOP.blockX;
					int hity = hitMOP.blockY;
					int hitz = hitMOP.blockZ;
					switch (hitMOP.sideHit) {
					case 0: // Bottom
						hity -= 2;
						break;
					case 1: // Top
						hity += 1;
						break;
					case 2: // East
						hitz -= 1;
						break;
					case 3: // West
						hitz += 1;
						break;
					case 4: // North
						hitx -= 1;
						break;
					case 5: // South
						hitx += 1;
						break;
					}
					player.setPositionAndUpdate(hitx + 0.5, hity, hitz + 0.5);
					break;
				default:
					break;

				}
			}
		}
	}
}
