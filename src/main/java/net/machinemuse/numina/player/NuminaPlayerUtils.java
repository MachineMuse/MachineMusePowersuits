package net.machinemuse.numina.player;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by Claire Semple on 9/9/2014.
 * <p>
 * Ported to Java by lehjr on 10/24/16.
 */
public final class NuminaPlayerUtils {
    public static void resetFloatKickTicks(EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) player;
            entityPlayerMP.connection.floatingTickCount = 0;
        }
    }
}