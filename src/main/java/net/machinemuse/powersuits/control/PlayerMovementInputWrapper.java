package net.machinemuse.powersuits.control;

import net.machinemuse.numina.capabilities.player.CapabilityPlayerValues;
import net.machinemuse.numina.capabilities.player.IPlayerValues;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerMovementInputWrapper {
    public static class PlayerMovementInput {
        public float moveStrafe;
        public float moveForward;
        public boolean jumpKey;
        public boolean downKey;
        public boolean sneakKey;

        public PlayerMovementInput(
                float moveStrafe,
                float moveForward,
                boolean jumpKey,
                boolean downKey,
                boolean sneakKey) {
            this.moveStrafe = Math.signum(moveStrafe);
            this.moveForward = Math.signum(moveForward);
            this.jumpKey = jumpKey;
            this.downKey = downKey;
            this.sneakKey = sneakKey;
        }
    }

    public static PlayerMovementInput get(EntityPlayer player) {
        if (player.world.isRemote) {
            if (player instanceof EntityOtherPlayerMP) // multiplayer not dedicated server
                return fromServer(player);
            return fromClient(player);
        }
        return fromServer(player);
    }

    static IPlayerValues getCapability(EntityPlayer player) {
        return player.getCapability(CapabilityPlayerValues.PLAYER_VALUES, null);
    }

    static PlayerMovementInput fromServer(EntityPlayer player) {
        boolean jumpKey = false;
        boolean downKey = false;

        IPlayerValues playerCap = getCapability(player);
        if (playerCap != null) {
            jumpKey = playerCap.getJumpKeyState();
            downKey = playerCap.getDownKeyState();
        }

        return new PlayerMovementInput(
                player.moveStrafing,
                player.moveForward,
                jumpKey,
                downKey,
                player.isSneaking());
    }

    static PlayerMovementInput fromClient(EntityPlayer player) {
        boolean jumpKey = false;
        boolean downKey = false;

        EntityPlayerSP clientPlayer = (EntityPlayerSP) player;

        IPlayerValues playerCap = getCapability(player);
        if (playerCap != null) {
            jumpKey = playerCap.getJumpKeyState();
            downKey = playerCap.getDownKeyState();
        }

        return new PlayerMovementInput(
                clientPlayer.movementInput.moveStrafe,
                clientPlayer.movementInput.moveForward,
                jumpKey,
                downKey,
        clientPlayer.movementInput.sneak);
    }
}