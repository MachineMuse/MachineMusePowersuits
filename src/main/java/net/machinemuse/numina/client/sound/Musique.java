package net.machinemuse.numina.client.sound;

import net.machinemuse.numina.config.NuminaConfig;
import net.machinemuse.numina.basemod.MuseLogger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

/**
 * Created by Claire on 8/27/2015.
 * <p>
 * Ported to Java by lehjr on 10/22/16.
 */
@SideOnly(Side.CLIENT)
public class Musique {
    private static HashMap<String, MovingSoundPlayer> soundMap = new HashMap<>();

    public static SoundHandler mcsound() {
        return Minecraft.getMinecraft().getSoundHandler();
    }

    public static void playClientSound(SoundEvent soundEvt, SoundCategory categoryIn, float volumeIn, BlockPos posIn) {
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
            BlockPos pos = (posIn != null) ? posIn : Minecraft.getMinecraft().player.getPosition();

            // creates a sound
            PositionedSoundRecord sound = new PositionedSoundRecord(soundEvt, categoryIn, volumeIn, 1.0F, pos);
            mcsound().playSound(sound);
        }
    }

    public static String makeSoundString(EntityPlayer player, SoundEvent soundEvt) {
                return makeSoundString(player, soundEvt.getSoundName());
    }

    public static String makeSoundString(EntityPlayer player, ResourceLocation soundname) {
        return player.getUniqueID().toString() + soundname;
    }

    public static void playerSound(EntityPlayer player, ResourceLocation location, SoundCategory categoryIn, float volume, Float pitch, Boolean continuous) {
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(location);
        if (soundEvent != null) {
//            System.out.println(soundEvent.getSoundName().toString());
            playerSound(player, soundEvent, categoryIn, volume, pitch, continuous);
        } else {
            soundEvent = new SoundEvent(location);
            if (soundEvent != null)
                playerSound(player, soundEvent, categoryIn, volume, pitch, continuous);
            else
                MuseLogger.logError("Sound event not found for " + location.toString());
        }
    }

    public static void playerSound(EntityPlayer player, SoundEvent soundEvt, SoundCategory categoryIn, float volume, Float pitch, Boolean continuous) {
        pitch = (pitch != null) ? pitch : 1.0F;
        continuous = (continuous != null) ? continuous : true;
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds() && soundEvt != null) {
            String soundID = makeSoundString(player, soundEvt);
            MovingSoundPlayer sound = soundMap.get(soundID);

            if (sound != null && (sound.isDonePlaying() || !sound.canRepeat())) {
                stopPlayerSound(player, soundEvt);
                sound = null;
            }
            if (sound != null) {
                sound.updateVolume(volume).updatePitch(pitch).updateRepeat(continuous);
            } else {
//                MuseLogger.logDebug("New sound: " + soundEvt.getSoundName());
                MovingSoundPlayer newsound = new MovingSoundPlayer(soundEvt, categoryIn, player, volume * 2.0f, pitch, continuous);
                mcsound().playSound(newsound);
                soundMap.put(soundID, newsound);
            }
        }
    }

    public static void stopPlayerSound(EntityPlayer player, ResourceLocation location) {
        SoundEvent soundEvent = SoundEvent.REGISTRY.getObject(location);
        if (soundEvent != null) {
//            System.out.println(soundEvent.getSoundName().toString());
            stopPlayerSound(player, soundEvent);
        } else {
            soundEvent = new SoundEvent(location);
            if (soundEvent != null)
                stopPlayerSound(player, soundEvent);
            else
                MuseLogger.logError("Sound event not found for " + location.toString());
        }
    }

    public static void stopPlayerSound(EntityPlayer player, SoundEvent soundEvt) {
        if ((FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) && NuminaConfig.useSounds()) {
            String soundID = makeSoundString(player, soundEvt);
            MovingSoundPlayer sound = soundMap.get(soundID);
            if (sound != null) {
                sound.stopPlaying();
                mcsound().stopSound(sound);
            }
            soundMap.remove(soundID);
//             MuseLogger.logDebug("Sound stopped: " + soundEvt.getSoundName());
        }
    }

    public GameSettings options() {
        return Minecraft.getMinecraft().gameSettings;
    }
}