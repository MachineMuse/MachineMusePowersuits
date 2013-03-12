/**
 * 
 */
package net.machinemuse.powersuits.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.network.packets.MusePacketFallDistance;
import net.machinemuse.powersuits.network.packets.MusePacketInstallModuleRequest;
import net.machinemuse.powersuits.network.packets.MusePacketInventoryRefresh;
import net.machinemuse.powersuits.network.packets.MusePacketModeChangeRequest;
import net.machinemuse.powersuits.network.packets.MusePacketPlasmaBolt;
import net.machinemuse.powersuits.network.packets.MusePacketPlayerUpdate;
import net.machinemuse.powersuits.network.packets.MusePacketSalvageModuleRequest;
import net.machinemuse.powersuits.network.packets.MusePacketToggleRequest;
import net.machinemuse.powersuits.network.packets.MusePacketTweakRequest;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

/**
 * @author MachineMuse
 * 
 */
public class MusePacketHandler implements IPacketHandler {
	public MusePacketHandler register() {
		addPacketType(1, MusePacketInventoryRefresh.class);
		addPacketType(2, MusePacketInstallModuleRequest.class);
		addPacketType(3, MusePacketSalvageModuleRequest.class);
		addPacketType(4, MusePacketTweakRequest.class);
		addPacketType(5, MusePacketFallDistance.class);
		addPacketType(6, MusePacketPlayerUpdate.class);
		addPacketType(7, MusePacketToggleRequest.class);
		addPacketType(8, MusePacketPlasmaBolt.class);
		addPacketType(9, MusePacketModeChangeRequest.class);

		NetworkRegistry.instance().registerChannel(this,
				Config.getNetworkChannelName());
		return this;
	}

	public static BiMap<Integer, Constructor<? extends MusePacket>> packetConstructors = HashBiMap
			.create();

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload payload, Player player) {
		if (payload.channel.equals(Config.getNetworkChannelName())) {
			MusePacket repackaged = repackage(payload, player);
			if (repackaged != null) {
				Side side = FMLCommonHandler.instance().getEffectiveSide();
				if (side == Side.CLIENT) {
					repackaged.handleClient((EntityClientPlayerMP) player);
				} else if (side == Side.SERVER) {
					repackaged.handleServer((EntityPlayerMP) player);
				}

			}
		}
	}

	public static MusePacket repackage(Packet250CustomPayload payload,
			Player player) {
		MusePacket repackaged = null;
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(
				payload.data));
		EntityPlayer target = (EntityPlayer) player;
		int packetType;
		try {
			packetType = data.readInt();
			repackaged = useConstructor(packetConstructors.get(packetType), data, player);
		} catch (IOException e) {
			MuseLogger.logError("PROBLEM READING PACKET TYPE D:");
			e.printStackTrace();
			return null;
		}
		return repackaged;
	}

	/**
	 * @param type
	 * @return
	 */
	public static int getTypeID(MusePacket packet) {
		try {
			return packetConstructors.inverse().get(
					getConstructor(packet.getClass()));
		} catch (NoSuchMethodException e) {
			MuseLogger.logError("INVALID PACKET CONSTRUCTOR D:");
			e.printStackTrace();
		} catch (SecurityException e) {
			MuseLogger.logError("PACKET SECURITY PROBLEM D:");
			e.printStackTrace();
		}
		return -150;
	}

	/**
	 * Returns the constructor of the given object. Keep in sync with
	 * useConstructor.
	 * 
	 * @param packetType
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	protected static Constructor<? extends MusePacket> getConstructor(
			Class<? extends MusePacket> packetType)
			throws NoSuchMethodException, SecurityException {
		return packetType.getConstructor(DataInputStream.class, Player.class);
	}

	/**
	 * Returns a new instance of the object, created via the constructor in
	 * question. Keep in sync with getConstructor.
	 * 
	 * @param constructor
	 * @return
	 */
	protected static MusePacket useConstructor(
			Constructor<? extends MusePacket> constructor,
			DataInputStream data, Player player) {
		try {
			return constructor.newInstance(data, player);
		} catch (InstantiationException e) {
			MuseLogger.logError("PROBLEM INSTATIATING PACKET D:");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			MuseLogger.logError("PROBLEM ACCESSING PACKET D:");
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			MuseLogger.logError("INVALID PACKET CONSTRUCTOR D:");
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			MuseLogger.logError("PROBLEM INVOKING PACKET CONSTRUCTOR D:");
			e.printStackTrace();
		}
		return null;
	}

	public static boolean addPacketType(int id,
			Class<? extends MusePacket> packetType) {
		try {
			// Add constructor to the list
			packetConstructors.put(id, getConstructor(packetType));
			return true;
		} catch (NoSuchMethodException e) {
			MuseLogger.logError("UNABLE TO REGISTER PACKET TYPE: "
					+ packetType + ": INVALID CONSTRUCTOR");
			e.printStackTrace();
		} catch (SecurityException e) {
			MuseLogger.logError("UNABLE TO REGISTER PACKET TYPE: "
					+ packetType + ": SECURITY PROBLEM");
			e.printStackTrace();
		}
		return false;
	}

}
