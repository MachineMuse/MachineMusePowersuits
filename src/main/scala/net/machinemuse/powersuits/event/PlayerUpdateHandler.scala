package net.machinemuse.powersuits.event

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.machinemuse.api.{ModuleManager, IModularItem}
import net.machinemuse.general.sound.SoundLoader
import net.machinemuse.numina.sound.proxy.Musique
import net.machinemuse.numina.general.MuseMathUtils
import net.machinemuse.numina.scala.OptionCast
import net.machinemuse.utils.{MuseHeatUtils, MuseItemUtils, MusePlayerUtils}
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent

/**
 * Created by Claire Semple on 9/8/2014.
 */
class PlayerUpdateHandler {
  @SubscribeEvent
  def onPlayerUpdate(e: LivingUpdateEvent) = {
    val player = OptionCast[EntityPlayer](e.entity).map { player =>
      val modularItemsEquipped = MuseItemUtils.modularItemsEquipped(player)
      val totalWeight: Double = MuseItemUtils.getPlayerWeight(player)
      val weightCapacity: Double = 25000
      import scala.collection.JavaConversions._
      for (stack <- modularItemsEquipped) {
        if (stack.getTagCompound.hasKey("ench")) {
          stack.getTagCompound.removeTag("ench")
        }
      }

      import scala.collection.JavaConversions._
      for (module <- ModuleManager.getPlayerTickModules) {
        import scala.collection.JavaConversions._
        for (itemStack <- modularItemsEquipped) {
          if (module.isValidForItem(itemStack)) {
            if (!ModuleManager.itemHasActiveModule(itemStack, module.getDataName)) {
              module.onPlayerTickInactive(player, itemStack)
            }
          }
        }
      }


      val foundItem: Boolean = modularItemsEquipped.size > 0


      if (foundItem) {
        player.fallDistance = MovementManager.computeFallHeightFromVelocity(MuseMathUtils.clampDouble(player.motionY, -1000.0, 0.0)).asInstanceOf[Float]
        if (totalWeight > weightCapacity) {
          player.motionX *= weightCapacity / totalWeight
          player.motionZ *= weightCapacity / totalWeight
        }
        MuseHeatUtils.coolPlayer(player, MusePlayerUtils.getPlayerCoolingBasedOnMaterial(player))
        val maxHeat: Double = MuseHeatUtils.getMaxHeat(player)
        val currHeat: Double = MuseHeatUtils.getPlayerHeat(player)
        if (currHeat > maxHeat) {
          player.attackEntityFrom(MuseHeatUtils.overheatDamage, Math.sqrt(currHeat - maxHeat).asInstanceOf[Int] / 4)
          player.setFire(1)
        }
        else {
          player.extinguish
        }
        val velsq2: Double = MuseMathUtils.sumsq(player.motionX, player.motionY, player.motionZ) - 0.5
        if (player.isAirBorne && velsq2 > 0) {
          Musique.playerSound(player, SoundLoader.SOUND_GLIDER, (velsq2 / 3).asInstanceOf[Float], 1.0f) //, true)
        }
        else {
          Musique.stopPlayerSound(player, SoundLoader.SOUND_GLIDER)
        }
      }
    }
  }
}
