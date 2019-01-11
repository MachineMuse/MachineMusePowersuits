package net.machinemuse.numina.control;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class PlayerInputMap {
    protected static final Map<String, PlayerInputMap> playerInputs = new HashMap();
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

    public static PlayerInputMap getInputMapFor(String playerName) {
        PlayerInputMap map = playerInputs.get(playerName);
        if (map == null) {
            map = new PlayerInputMap(playerName);
        }
        return map;
    }

    public boolean writeToByteBuf(ByteBuf buf) {
        buf.writeFloat(forwardKey);
        buf.writeFloat(strafeKey);
        buf.writeBoolean(jumpKey);
        buf.writeBoolean(sneakKey);
        buf.writeBoolean(downKey);
        buf.writeDouble(motionX);
        buf.writeDouble(motionY);
        buf.writeDouble(motionZ);
        return true;
    }

    public PlayerInputMap readFromByteBuf(ByteBuf buf) {
        forwardKey = buf.readFloat();
        strafeKey = buf.readFloat();
        jumpKey = buf.readBoolean();
        sneakKey = buf.readBoolean();
        downKey = buf.readBoolean();
        motionX = buf.readDouble();
        motionY = buf.readDouble();
        motionZ = buf.readDouble();
        return this;
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