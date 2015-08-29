package net.machinemuse.powersuits.event

import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.{Side, SideOnly}
import net.machinemuse.api.ModuleManager
import net.machinemuse.powersuits.powermodule.misc.BinocularsModule
import net.machinemuse.powersuits.powermodule.movement.{FlightControlModule, GliderModule, JetBootsModule, JetPackModule}
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.{FOVUpdateEvent, RenderPlayerEvent, RenderWorldLastEvent, TextureStitchEvent}

object RenderEventHandler {
  private[event] var ownFly: Boolean = false
}

class RenderEventHandler {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent def renderLast(event: RenderWorldLastEvent) {
    val mc: Minecraft = Minecraft.getMinecraft
    val screen: ScaledResolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)
  }

  @SubscribeEvent def onTextureStitch(event: TextureStitchEvent.Post) {
  }

  @SubscribeEvent def onPreRenderPlayer(event: RenderPlayerEvent.Pre) {
    if (!event.entityPlayer.capabilities.isFlying && !event.entityPlayer.onGround && playerHasFlightOn(event.entityPlayer)) {
      event.entityPlayer.capabilities.isFlying = true
      RenderEventHandler.ownFly = true
    }
  }

  private def playerHasFlightOn(player: EntityPlayer): Boolean = {
    return ModuleManager.itemHasActiveModule(player.getCurrentArmor(2), JetPackModule.MODULE_JETPACK) || ModuleManager.itemHasActiveModule(player.getCurrentArmor(2), GliderModule.MODULE_GLIDER) || ModuleManager.itemHasActiveModule(player.getCurrentArmor(0), JetBootsModule.MODULE_JETBOOTS) || ModuleManager.itemHasActiveModule(player.getCurrentArmor(3), FlightControlModule.MODULE_FLIGHT_CONTROL)
  }

  @SubscribeEvent def onPostRenderPlayer(event: RenderPlayerEvent.Post) {
    if (RenderEventHandler.ownFly) {
      RenderEventHandler.ownFly = false
      event.entityPlayer.capabilities.isFlying = false
    }
  }

  @SubscribeEvent def onFOVUpdate(e: FOVUpdateEvent) {
    val helmet = e.entity.getCurrentArmor(3)
    if (ModuleManager.itemHasActiveModule(helmet, "Binoculars")) {
      e.newfov = e.newfov / ModuleManager.computeModularProperty(helmet, BinocularsModule.FOV_MULTIPLIER).toFloat
    }
  }

}