package net.machinemuse.powersuits.common

import java.io.File

import cpw.mods.fml.client.FMLClientHandler
import cpw.mods.fml.common.{Mod, SidedProxy}
import cpw.mods.fml.common.event._
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.EntityRegistry
import net.machinemuse.numina.recipe.JSONRecipeList
import net.machinemuse.powersuits.entity.{EntityLuxCapacitor, EntityPlasmaBolt, EntitySpinningBlade}
import net.machinemuse.powersuits.event.{HarvestEventHandler, MovementManager}
import net.machinemuse.powersuits.network.packets.MPSPacketList
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration

/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 *
 * @author MachineMuse
 */
@Mod(modid = "powersuits", modLanguage = "scala", dependencies = "required-after:numina;after:EnderIO;after:Railcraft;after:RenderPlayerAPI")
object ModularPowersuits {
  @SidedProxy(clientSide = "net.machinemuse.powersuits.common.ClientProxy", serverSide = "net.machinemuse.powersuits.common.ServerProxy")
  var proxy: CommonProxy = null
  var config: Configuration = null
  
  var configDir: java.io.File = null

  val INSTANCE=this

  @Mod.EventHandler def preInit(event: FMLPreInitializationEvent) {
    proxy.preInit()
    configDir = event.getModConfigurationDirectory
    val recipesFolder = new File(configDir, "machinemuse/recipes/powersuits")
    val newConfig: File = new File(configDir, "machinemuse/powersuits.cfg")
    recipesFolder.mkdir()
    Config.init(new Configuration(newConfig))
    Config.setConfigFolderBase(event.getModConfigurationDirectory)
    MinecraftForge.EVENT_BUS.register(new HarvestEventHandler)
    MinecraftForge.EVENT_BUS.register(new MovementManager)
    proxy.registerEvents()

  }

  @Mod.EventHandler def load(event: FMLInitializationEvent) {

    Config.loadPowerModules
    Config.getMaximumArmorPerPiece
    Config.getMaximumFlyingSpeedmps
    Config.useMouseWheel
    Config.useGraphicalMeters
    Config.getSalvageChance
    Config.baseMaxHeat
    Config.allowConflictingKeybinds
    Config.fontURI
    Config.fontName
    Config.fontDetail
    Config.fontAntiAliasing
    Config.useCustomFonts
    Config.glowMultiplier
    Config.useShaders
    EntityRegistry.registerModEntity(classOf[EntityPlasmaBolt], "entityPlasmaBolt", 2477, this, 64, 20, true)
    EntityRegistry.registerModEntity(classOf[EntitySpinningBlade], "entitySpinningBlade", 2478, this, 64, 20, true)
    EntityRegistry.registerModEntity(classOf[EntityLuxCapacitor], "entityLuxCapacitor", 2479, this, 64, 20, true)
    proxy.registerHandlers()
    proxy.registerRenderers()
    MPSPacketList.registerPackets()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, MPSGuiHandler)
  }

  @Mod.EventHandler def postInit(event: FMLPostInitializationEvent) {
    proxy.postInit()
    ModCompatability.registerModSpecificModules()
    Config.getConfig.save
  }

  @Mod.EventHandler def atLaunch(event: FMLLoadCompleteEvent) {
    proxy.atLaunch()
  }
  
  @Mod.EventHandler def onServerStarting(event: FMLServerStartingEvent) {
    MPSRecipeManager.loadOrPutRecipesFromJar(configDir.getAbsolutePath + "/machinemuse/recipes/powersuits")
  }

}
