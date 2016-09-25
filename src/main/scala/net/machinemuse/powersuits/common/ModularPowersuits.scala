package net.machinemuse.powersuits.common

import java.io.File

import net.machinemuse.powersuits.block.{BlockLuxCapacitor, BlockTinkerTable, TileEntityLuxCapacitor, TileEntityTinkerTable}
import net.machinemuse.powersuits.entity.{EntityLuxCapacitor, EntityPlasmaBolt, EntitySpinningBlade}
import net.machinemuse.powersuits.event.{HarvestEventHandler, ModelBakeEventHandler, MovementManager}
import net.machinemuse.powersuits.network.packets.MPSPacketList
import net.minecraft.item.ItemBlock
import net.minecraftforge.client.model.obj.OBJLoader
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPostInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.registry.{EntityRegistry, GameRegistry}
import net.minecraftforge.fml.common.{Mod, SidedProxy}
;


/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 *
 * @author MachineMuse
 */
@Mod(modid = "powersuits", modLanguage = "scala", dependencies = "required-after:numina@[@numina_version@,)")
object ModularPowersuits {
  @SidedProxy(clientSide = "net.machinemuse.powersuits.common.ClientProxy", serverSide = "net.machinemuse.powersuits.common.ServerProxy")
  var proxy: CommonProxy = _
  var config: Configuration = _
  val INSTANCE=this
  val MODID="powersuits"
  val VERSION = "@VERSION@"

  @Mod.EventHandler def preInit(event: FMLPreInitializationEvent) {
    OBJLoader.INSTANCE.addDomain(MODID.toLowerCase)


    val newConfig: File = new File(event.getModConfigurationDirectory + "/machinemuse/powersuits.cfg")
    Config.init(new Configuration(newConfig))
    Config.setConfigFolderBase(event.getModConfigurationDirectory)
    MinecraftForge.EVENT_BUS.register(new HarvestEventHandler)
    MinecraftForge.EVENT_BUS.register(new MovementManager)
    MinecraftForge.EVENT_BUS.register(new ModelBakeEventHandler)

    proxy.registerEvents()



    GameRegistry.register(BlockTinkerTable.instance)



    GameRegistry.register(new ItemBlock(BlockTinkerTable.instance).setRegistryName(BlockTinkerTable.instance.getRegistryName))
    GameRegistry.register(BlockLuxCapacitor.instance)
    GameRegistry.register(new ItemBlock(BlockLuxCapacitor.instance).setRegistryName(BlockLuxCapacitor.instance.getRegistryName))
    GameRegistry.registerTileEntity(classOf[TileEntityTinkerTable], BlockTinkerTable.name)
    GameRegistry.registerTileEntity(classOf[TileEntityLuxCapacitor], BlockLuxCapacitor.name)
    proxy.registerRenderers()
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
    MPSPacketList.registerPackets()
    NetworkRegistry.INSTANCE.registerGuiHandler(this, MPSGuiHandler)
  }

  @Mod.EventHandler def postInit(event: FMLPostInitializationEvent) {
    proxy.postInit()
    ModCompatibility.registerModSpecificModules()
    Config.extractRecipes
    Config.addCustomInstallCosts
    Config.getConfig.save
  }

}