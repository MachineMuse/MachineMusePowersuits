package net.machinemuse.powersuits.control;

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

	public PlayerInputMap lastSentMap;

	public float forwardKey;
	public float strafeKey;
	public boolean jumpKey;
	public boolean sneakKey;
	public boolean downKey;
	public double motionX;
	public double motionY;
	public double motionZ;

	public PlayerInputMap(PlayerInputMap master) {
		this.setTo(master);
	}

	public PlayerInputMap(String playerName) {
		playerInputs.put(playerName, this);
		lastSentMap = new PlayerInputMap(this);
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

	@Override
	public boolean equals(Object obj) {
		try {
			PlayerInputMap other = (PlayerInputMap) obj;
			return other.forwardKey == forwardKey
					&& other.strafeKey == this.strafeKey
					&& other.jumpKey == this.jumpKey
					&& other.sneakKey == this.sneakKey
					&& other.downKey == this.downKey
					&& other.motionX == this.motionX
					&& other.motionY == this.motionY
					&& other.motionZ == this.motionZ;
		} catch (ClassCastException ignored) {
		}
		return false;
	}

	public void setTo(PlayerInputMap master) {
		forwardKey = master.forwardKey;
		strafeKey = master.strafeKey;
		jumpKey = master.jumpKey;
		sneakKey = master.sneakKey;
		downKey = master.downKey;
		motionX = master.motionX;
		motionY = master.motionY;
		motionZ = master.motionZ;
	}

	public boolean hasChanged() {
		return this.equals(lastSentMap);
	}

	public void refresh() {
		this.lastSentMap.setTo(this);
	}
}
