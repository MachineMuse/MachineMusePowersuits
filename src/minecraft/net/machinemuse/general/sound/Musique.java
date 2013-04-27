package net.machinemuse.general.sound;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

public class Musique {
    public static void playOneshotSound(EntityPlayer player, String soundname, float volume) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            Minecraft.getMinecraft().sndManager.playSoundFX(SoundLoader.SOUND_JUMP_ASSIST, 1.0f, 1.0f);
            // Minecraft.getMinecraft().sndManager.playEntitySound(SoundLoader.SOUND_JUMP_ASSIST, player, 1.0f, 1.0f, true);
        }
    }
}
