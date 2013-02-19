package net.machinemuse.powersuits.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PlayerInputMap {
	protected static Map<String, PlayerInputMap> playerInputs = new HashMap();

	public static PlayerInputMap getInputMapFor(String playerName) {
		PlayerInputMap map = playerInputs.get(playerName);
		if (map == null) {
			map = new PlayerInputMap(playerName);
		}
		return map;
	}

	public float forwardKey;
	public float strafeKey;
	public boolean jumpKey;
	public boolean sneakKey;
	public boolean downKey;
	public double motionX;
	public double motionY;
	public double motionZ;

	public PlayerInputMap(String playerName) {
		playerInputs.put(playerName, this);
	}

	public boolean writeToStream(DataOutputStream stream) {
		try {
			stream.writeFloat(forwardKey);
			stream.writeFloat(strafeKey);
			stream.writeBoolean(jumpKey);
			stream.writeBoolean(sneakKey);
			stream.writeBoolean(downKey);
			stream.writeDouble(motionX);
			stream.writeDouble(motionY);
			stream.writeDouble(motionZ);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean readFromStream(DataInputStream stream) {
		try {
			forwardKey = stream.readFloat();
			strafeKey = stream.readFloat();
			jumpKey = stream.readBoolean();
			sneakKey = stream.readBoolean();
			downKey = stream.readBoolean();
			motionX = stream.readDouble();
			motionY = stream.readDouble();
			motionZ = stream.readDouble();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
