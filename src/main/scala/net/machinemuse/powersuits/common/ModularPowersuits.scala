package net.machinemuse.powersuits.common

import java.io.File

import cpw.mods.fml.common.{Mod, SidedProxy}
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import cpw.mods.fml.common.network.NetworkRegistry
import cpw.mods.fml.common.registry.EntityRegistry
import net.machinemuse.api.{ILocalizeableModule, IPowerModule, ModuleManager}
import net.machinemuse.powersuits.entity.{EntityLuxCapacitor, EntityPlasmaBolt, EntitySpinningBlade}
import net.machinemuse.powersuits.event.{HarvestEventHandler, MovementManager}
import net.machinemuse.powersuits.network.packets.MPSPacketList
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.machinemuse.powersuits.powermodule.tool.TerminalHandler;


/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 *
 * @author MachineMuse
 */
@Mod(modid = "powersuits", modLanguage = "scala", dependencies = "required-after:numina@[@numina_version@,)")
object ModularPowersuits {
  @SidedProxy(clientSide = "net.machinemuse.powersuits.common.ClientProxy", serverSide = "net.machinemuse.powersuits.common.ServerProxy")
  var proxy: CommonProxy = null
  var config: Configuration = null
  val INSTANCE=this

  @Mod.EventHandler def preInit(event: FMLPreInitializationEvent) {
    val newConfig: File = new File(event.getModConfigurationDirectory + "/machinemuse/powersuits.cfg")
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
    Config.fontAntiAliasing
    Config.useCustomFonts
    Config.glowMultiplier
    Config.useShaders
    Config.getWeightCapacity()
    Config.keybindHUDon
    Config.keybindHUDx
    Config.toggleModuleSpam
    EntityRegistry.registerModEntity(classOf[EntityPlasmaBolt], "entityPlasmaBolt", 2477, this, 64, 20, true)
    EntityRegistry.registerModEntity(classOf[EntitySpinningBlade], "entitySpinningBlade", 2478, this, 64, 20, true)
    EntityRegistry.registerModEntity(classOf[EntityLuxCapacitor], "entityLuxCapacitor", 2479, this, 64, 20, true)
    proxy.registerHandlers()
    proxy.registerRenderers()
    MPSPacketList.registerPackets()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, MPSGuiHandler)
    TerminalHandler.registerHandler();
  }

  @Mod.EventHandler def postInit(event: FMLPostInitializationEvent) {
    proxy.postInit()
    ModCompatibility.registerModSpecificModules()
    Config.extractRecipes
    Config.addCustomInstallCosts
    Config.getConfig.save
  }

}