package net.machinemuse.powersuits.event

import net.machinemuse.api.ModuleManager
import net.machinemuse.general.gui.MuseIcon
import net.machinemuse.general.gui.clickable.{ClickableKeybinding, ClickableModule}
import net.machinemuse.numina.geometry.{Colour, DrawableMuseRect}
import net.machinemuse.numina.render.{MuseIconUtils, MuseTextureUtils}
import net.machinemuse.powersuits.common.Config
import net.machinemuse.powersuits.control.KeybindManager
import net.machinemuse.powersuits.powermodule.misc.BinocularsModule
import net.machinemuse.powersuits.powermodule.movement.{FlightControlModule, GliderModule, JetBootsModule, JetPackModule}
import net.machinemuse.utils.MuseItemUtils
import net.machinemuse.utils.render.MuseRenderer
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType
import net.minecraftforge.client.event._
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.{Side, SideOnly}

object RenderEventHandler {
  private[event] var ownFly: Boolean = false
}

class RenderEventHandler {
  @SideOnly(Side.CLIENT)
  @SubscribeEvent def renderLast(event: RenderWorldLastEvent) {
    val mc: Minecraft = Minecraft.getMinecraft
    val screen: ScaledResolution = new ScaledResolution(mc)
  }

  @SideOnly(Side.CLIENT)
  @SubscribeEvent def preTextureStitch(event: TextureStitchEvent.Pre) {
    MuseIcon.registerIcons(event)
  }

  @SubscribeEvent def onTextureStitch(event: TextureStitchEvent.Post) {
  }

  @SubscribeEvent def onPreRenderPlayer(event: RenderPlayerEvent.Pre) {
    if (!event.getEntityPlayer.capabilities.isFlying && !event.getEntityPlayer.onGround && playerHasFlightOn(event.getEntityPlayer)) {
      event.getEntityPlayer.capabilities.isFlying = true
      RenderEventHandler.ownFly = true
    }
  }

  private def playerHasFlightOn(player: EntityPlayer): Boolean = {
    ModuleManager.itemHasActiveModule(player.inventory.armorItemInSlot(2), JetPackModule.MODULE_JETPACK) || ModuleManager.itemHasActiveModule(player.inventory.armorItemInSlot(2), GliderModule.MODULE_GLIDER) || ModuleManager.itemHasActiveModule(player.inventory.armorItemInSlot(0), JetBootsModule.MODULE_JETBOOTS) || ModuleManager.itemHasActiveModule(player.inventory.armorItemInSlot(3), FlightControlModule.MODULE_FLIGHT_CONTROL)
  }

  @SubscribeEvent def onPostRenderPlayer(event: RenderPlayerEvent.Post) {
    if (RenderEventHandler.ownFly) {
      RenderEventHandler.ownFly = false
      event.getEntityPlayer.capabilities.isFlying = false
    }
  }

  @SubscribeEvent def onFOVUpdate(e: FOVUpdateEvent) {
    val helmet = e.getEntity.inventory.armorItemInSlot(3)
    if (ModuleManager.itemHasActiveModule(helmet, "Binoculars")) {
      e.setNewfov(e.getFov / ModuleManager.computeModularProperty(helmet, BinocularsModule.FOV_MULTIPLIER).toFloat)
    }
  }


  @SubscribeEvent def onPostRenderGameOverlayEvent(e: RenderGameOverlayEvent.Post) {
    e.getType match {
      //      case ElementType.ALL =>
      //      case ElementType.HELMET =>
      //      case ElementType.PORTAL =>
      //      case ElementType.CROSSHAIRS =>
      //      case ElementType.BOSSHEALTH =>
      //      case ElementType.ARMOR =>
      //      case ElementType.HEALTH =>
      //      case ElementType.FOOD =>
      //      case ElementType.AIR =>
      case ElementType.HOTBAR => drawKeybindToggles()
      //      case ElementType.EXPERIENCE =>
      //      case ElementType.TEXT =>
      //      case ElementType.HEALTHMOUNT =>
      //      case ElementType.JUMPBAR =>
      case _ =>
    }
  }

  val frame = new DrawableMuseRect(Config.keybindHUDx, Config.keybindHUDy, Config.keybindHUDx + 16, Config.keybindHUDy + 16, true, Colour.DARKGREEN.withAlpha(0.2), Colour.GREEN.withAlpha(0.2))

  def drawKeybindToggles(): Unit = {
    if(Config.keybindHUDon)
    for {
      mc <- Option(Minecraft.getMinecraft)
      player <- Option(mc.thePlayer)
    } {
      val screen = new ScaledResolution(mc)
      frame.setLeft(Config.keybindHUDx)
      frame.setTop(Config.keybindHUDy)
      frame.setBottom(frame.top() + 16)
      import scala.collection.JavaConverters._
      for (kb: ClickableKeybinding <- KeybindManager.getKeybindings.asScala) {
        if (kb.displayOnHUD) {
          val stringwidth = MuseRenderer.getStringWidth(kb.getLabel)
          frame.setWidth(stringwidth + kb.getBoundModules.size() * 16)
          frame.draw()
          MuseRenderer.drawString(kb.getLabel, frame.left()+1, frame.top()+3, if (kb.toggleval) Colour.RED else Colour.GREEN)
          var x = frame.left() + stringwidth
          for (module: ClickableModule <- kb.getBoundModules.asScala) {

            MuseTextureUtils.pushTexture(module.getModule.getStitchedTexture(null))
            var active = false
            for (stack <- MuseItemUtils.modularItemsEquipped(player).asScala)
              if (ModuleManager.itemHasActiveModule(stack, module.getModule.getDataName))
                active = true
            MuseIconUtils.drawIconAt(x, frame.top, module.getModule.getIcon(null), if (active) Colour.WHITE else Colour.DARKGREY.withAlpha(0.5))
            MuseTextureUtils.popTexture()
            x += 16
          }
          frame.setTop(frame.top() + 16)
          frame.setBottom(frame.top() + 16)
        }
      }
    }
  }
}