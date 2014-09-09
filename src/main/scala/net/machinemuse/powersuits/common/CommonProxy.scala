package net.machinemuse.powersuits.common

import java.net.URL

import cpw.mods.fml.client.registry.{ClientRegistry, RenderingRegistry}
import cpw.mods.fml.common.FMLCommonHandler
import net.machinemuse.general.sound.SoundLoader
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.numina.network.{MusePacket, MusePacketHandler, MusePacketModeChangeRequest, PacketSender}
import net.machinemuse.numina.render.RenderGameOverlayEventHandler
import net.machinemuse.powersuits.block.{BlockTinkerTable, TileEntityLuxCapacitor, TileEntityTinkerTable}
import net.machinemuse.powersuits.client.render.block.{RenderLuxCapacitorTESR, TinkerTableRenderer}
import net.machinemuse.powersuits.client.render.entity.{RenderLuxCapacitorEntity, RenderPlasmaBolt, RenderSpinningBlade}
import net.machinemuse.powersuits.client.render.item.ToolRenderer
import net.machinemuse.powersuits.client.render.modelspec.ModelSpecXMLReader
import net.machinemuse.powersuits.control.{KeybindKeyHandler, KeybindManager}
import net.machinemuse.powersuits.entity.{EntityLuxCapacitor, EntityPlasmaBolt, EntitySpinningBlade}
import net.machinemuse.powersuits.event.{ClientTickHandler, PlayerLoginHandlerThingy, PlayerUpdateHandler, RenderEventHandler}
import net.machinemuse.utils.render.MuseShaders
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityClientPlayerMP
import net.minecraftforge.client.MinecraftForgeClient
import net.minecraftforge.common.MinecraftForge

/**
 * Common side of the proxy. Provides functions which
 * the ClientProxy and ServerProxy will override if the behaviour is different for client and
 * server, and keeps some common behaviour.
 *
 * @author MachineMuse
 */
object CommonProxy {
  def getResource(url: String): URL = {
    return classOf[CommonProxy].getResource(url)
  }
}

trait CommonProxy {
  def registerEvents() {}

  def registerRenderers() {}

  def registerHandlers() {}

  def postInit() {}

  def sendModeChange(dMode: Int, newMode: String) {}
}

object ClientProxy {
  private var toolRenderer: ToolRenderer = null
  var keybindHandler: KeybindKeyHandler = null
}

class ClientProxy extends CommonProxy {
  override def registerEvents {
    MinecraftForge.EVENT_BUS.register(new SoundLoader)
  }

  /**
   * Register all the custom renderers for this mod.
   */
  override def registerRenderers {
    MinecraftForgeClient.registerItemRenderer(MPSItems.powerTool, new ToolRenderer)
    val tinkTableRenderID: Int = RenderingRegistry.getNextAvailableRenderId
    val tinkTableRenderer: TinkerTableRenderer = new TinkerTableRenderer(tinkTableRenderID)
    BlockTinkerTable.setRenderType(tinkTableRenderID)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityTinkerTable], tinkTableRenderer)
    RenderingRegistry.registerBlockHandler(tinkTableRenderer)
    val luxCapacitorRenderID: Int = RenderingRegistry.getNextAvailableRenderId
    val luxCapacitorRenderer: RenderLuxCapacitorTESR = new RenderLuxCapacitorTESR(luxCapacitorRenderID)
    MPSItems.luxCapacitor.setRenderType(luxCapacitorRenderID)
    ClientRegistry.bindTileEntitySpecialRenderer(classOf[TileEntityLuxCapacitor], luxCapacitorRenderer)
    RenderingRegistry.registerBlockHandler(luxCapacitorRenderer)
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityPlasmaBolt], new RenderPlasmaBolt)
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntitySpinningBlade], new RenderSpinningBlade)
    RenderingRegistry.registerEntityRenderingHandler(classOf[EntityLuxCapacitor], new RenderLuxCapacitorEntity)
    MinecraftForge.EVENT_BUS.register(new RenderEventHandler)
    val resource: URL = classOf[ClientProxy].getResource(Config.RESOURCE_PREFIX + "models/modelspec.xml")
    ModelSpecXMLReader.parseFile(resource)
    val otherResource: URL = classOf[ClientProxy].getResource(Config.RESOURCE_PREFIX + "models/armor2.xml")
    ModelSpecXMLReader.parseFile(otherResource)
    try {
      val x = MuseShaders.hBlurProgram.program // want this to initialize :s
      Config.canUseShaders = true
    }
    catch {
      case e: Throwable => {
        MuseLogger.logException("Loading shaders failed!", e)
      }
    }
  }

  /**
   * Register the tick handler (for on-tick behaviour) and packet handler (for
   * network synchronization and permission stuff).
   */
  override def registerHandlers {
    FMLCommonHandler.instance.bus.register(new KeybindKeyHandler)
    MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler)
    FMLCommonHandler.instance.bus.register(new ClientTickHandler)
    val packetHandler: MusePacketHandler.type = MusePacketHandler
  }

  override def postInit() {
    KeybindManager.readInKeybinds()
  }

  override def sendModeChange(dMode: Int, newMode: String) {
    val player: EntityClientPlayerMP = Minecraft.getMinecraft.thePlayer
    RenderGameOverlayEventHandler.updateSwap(Math.signum(dMode).asInstanceOf[Int])
    val modeChangePacket: MusePacket = new MusePacketModeChangeRequest(player, newMode, player.inventory.currentItem)
    PacketSender.sendToServer(modeChangePacket)
  }
}


class ServerProxy extends CommonProxy {
  override def registerEvents {
    FMLCommonHandler.instance().bus().register(PlayerLoginHandlerThingy)
  }

  override def registerHandlers() {
    MinecraftForge.EVENT_BUS.register(new PlayerUpdateHandler)
  }
}
