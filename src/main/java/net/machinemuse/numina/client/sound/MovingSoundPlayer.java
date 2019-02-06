package net.machinemuse.numina.client.sound;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

/**
 * Ported to Java by lehjr on 10/22/16.
 */
public class MovingSoundPlayer extends MovingSound {
    private static EntityPlayer player;

    /*
     * Important porting note:
     * Sounds are now event based instead of resource location.
     */
    public MovingSoundPlayer(SoundEvent soundIn,
                             SoundCategory categoryIn,
                             EntityPlayer playerIn,
                             float newvolume,
                             float pitchIn,
                             boolean repeatIn) {

        super(soundIn, categoryIn);
        this.player = playerIn;
        this.pitch = pitchIn;
        this.volume = newvolume;
        this.repeat = repeatIn;
    }

    public EntityPlayer player() {
        return this.player;
    }

    public MovingSoundPlayer updatePitch(float newpitch) {
        this.pitch = newpitch;
        return this;
    }

    public MovingSoundPlayer updateVolume(float newvolume) {
        this.volume = (4.0f * this.volume + newvolume) / 5.0f;
        return this;
    }

    public MovingSoundPlayer updateRepeat(boolean newrepeat) {
        this.repeat = newrepeat;
        return this;
    }

    @Override
    public ISound.AttenuationType getAttenuationType() {
        return ISound.AttenuationType.LINEAR;
    }

    public void stopPlaying() {
        super.donePlaying = true;
    }

    @Override
    public void update() {
        this.xPosF = (float) this.player().posX;
        this.yPosF = (float) this.player().posY;
        this.zPosF = (float) this.player().posZ;
    }
}