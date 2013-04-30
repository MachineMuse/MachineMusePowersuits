package net.machinemuse.general.sound

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.client.audio.SoundManager
import scala.Predef.String

/**
 * Handles sound mechanics
 */
object Musique {
  def soundsystem = SoundManager.sndSystem

  def mcsound = Minecraft.getMinecraft.sndManager

  def options = Minecraft.getMinecraft.gameSettings

  val soundprefix = "MMMPS"


  def playClientSound(soundname: String, volume: Float) {
    if (FMLCommonHandler.instance.getEffectiveSide eq Side.CLIENT) {
      val pitch: Float = 1.0f
      mcsound.playSoundFX(soundname, volume, pitch)
    }
  }

  def makeSoundString(player: EntityPlayer, soundname: String): String = soundprefix + player.username + soundname

  def playerSound(player: EntityPlayer, soundname: String, volume: Float, pitch: Float = 1.0f, continuous: Boolean = true) {
    if (FMLCommonHandler.instance.getEffectiveSide eq Side.CLIENT) {
      val pitch: Float = 1.0f
      val unknownflag = true
      val soundid = makeSoundString(player, soundname)
      if (!soundsystem.playing(soundid)) {
        val soundfile = getSoundPoolEntry(soundname)
        val amp: Float = 16.0F * Math.max(1, volume)
        soundsystem.newSource(unknownflag, soundid, soundfile.soundUrl, soundfile.soundName, false, player.posX.toFloat, player.posY.toFloat, player.posZ.toFloat, 2, amp)
        soundsystem.setLooping(soundid, continuous)
        soundsystem.play(soundid)
      }
      soundsystem.setPitch(soundid, pitch)
      soundsystem.setPosition(soundid, player.posX.toFloat, player.posY.toFloat, player.posZ.toFloat)
      soundsystem.setVolume(soundid, Math.min(volume, 1) * this.options.soundVolume)
      soundsystem.setVelocity(soundid, player.motionX.toFloat, player.motionY.toFloat, player.motionZ.toFloat)
    }
  }

  def stopPlayerSound(player: EntityPlayer, soundname: String) {
    if (FMLCommonHandler.instance.getEffectiveSide eq Side.CLIENT) {
      val soundid = makeSoundString(player, soundname)
      val vol = soundsystem.getVolume(soundid) - 0.1f
      if (vol > 0) {
        soundsystem.setVolume(soundid, vol)
      } else {
        soundsystem.stop(makeSoundString(player, soundname))
      }
    }
  }

  def getSoundPoolEntry(s: String) = mcsound.soundPoolSounds.getRandomSoundFromSoundPool(s)
}