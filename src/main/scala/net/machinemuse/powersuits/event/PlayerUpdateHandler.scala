package net.machinemuse.powersuits.event

import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import net.machinemuse.api.ModuleManager
import net.machinemuse.general.sound.SoundDictionary
import net.machinemuse.numina.basemod.NuminaConfig
import net.machinemuse.numina.general.MuseMathUtils
import net.machinemuse.numina.sound.Musique
import net.machinemuse.powersuits.common.Config
import net.machinemuse.utils.{MuseHeatUtils, MuseItemUtils, MusePlayerUtils}
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent

/**
 * Created by Claire Semple on 9/8/2014.
 */
class PlayerUpdateHandler {
  @SubscribeEvent
  def onPlayerUpdate(e: LivingUpdateEvent) = {
    e.entity match {
      case player: EntityPlayer => {

        val modularItemsEquipped = MuseItemUtils.modularItemsEquipped(player)
        val totalWeight: Double = MuseItemUtils.getPlayerWeight(player)
        val weightCapacity: Double = Config.getWeightCapacity()

        import scala.collection.JavaConversions._
        for (stack <- modularItemsEquipped) {
          if (stack.getTagCompound.hasKey("ench")) {
            stack.getTagCompound.removeTag("ench")
          }
        }



        var foundItemWithModule: Boolean = false
        import scala.collection.JavaConversions._
        for (module <- ModuleManager.getPlayerTickModules) {
          foundItemWithModule = false
          import scala.collection.JavaConversions._
          for (itemStack <- modularItemsEquipped) {
            if (module.isValidForItem(itemStack)) {
              if (ModuleManager.itemHasActiveModule(itemStack, module.getDataName)) {
                module.onPlayerTickActive(player, itemStack)
                foundItemWithModule = true
              }
            }
          }
          if (!foundItemWithModule) {
            import scala.collection.JavaConversions._
            for (itemStack <- modularItemsEquipped) {
              module.onPlayerTickInactive(player, itemStack)
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
            player.extinguish()
          }
          val velsq2: Double = MuseMathUtils.sumsq(player.motionX, player.motionY, player.motionZ) - 0.5
          if ((FMLCommonHandler.instance.getEffectiveSide == Side.CLIENT) && NuminaConfig.useSounds) {
            if (player.isAirBorne && velsq2 > 0) {
              Musique.playerSound(player, SoundDictionary.SOUND_GLIDER, (velsq2 / 3).asInstanceOf[Float], 1.0f, continuous = true)
            }
            else {
              Musique.stopPlayerSound(player, SoundDictionary.SOUND_GLIDER)
            }
          }
        }
      }
      case _ =>
    }
  }
}