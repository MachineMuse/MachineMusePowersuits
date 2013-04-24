package net.machinemuse.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 12:51 AM, 4/24/13
 */
public class PlayerAccess extends EntityPlayer {

    public PlayerAccess(World par1World) {
        super(par1World);
    }

    public void setPlayerFOVMultiplier(float mult) {
        speedOnGround = mult;
    }

    @Override
    public void sendChatToPlayer(String s) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canCommandSenderUseCommand(int i, String s) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ChunkCoordinates getPlayerCoordinates() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
