package net.machinemuse.powersuits.common

import java.nio.file.{Paths, Files}

import com.google.gson.Gson
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.registry.GameRegistry
import cpw.mods.fml.relauncher.Side
import net.machinemuse.api.IModularItem
import net.machinemuse.api.IPowerModule
import net.machinemuse.api.ModuleManager
import net.machinemuse.numina.basemod.Numina
import net.machinemuse.numina.general.MuseLogger
import net.machinemuse.powersuits.item.ItemComponent
import net.machinemuse.powersuits.powermodule.armor.BasicPlatingModule
import net.machinemuse.powersuits.powermodule.armor.DiamondPlatingModule
import net.machinemuse.powersuits.powermodule.armor.EnergyShieldModule
import net.machinemuse.powersuits.powermodule.armor.HeatSinkModule
import net.machinemuse.powersuits.powermodule.energy.AdvancedBatteryModule
import net.machinemuse.powersuits.powermodule.energy.BasicBatteryModule
import net.machinemuse.powersuits.powermodule.energy.EliteBatteryModule
import net.machinemuse.powersuits.powermodule.misc._
import net.machinemuse.powersuits.powermodule.movement._
import net.machinemuse.powersuits.powermodule.tool._
import net.machinemuse.powersuits.powermodule.weapon.BladeLauncherModule
import net.machinemuse.powersuits.powermodule.weapon.MeleeAssistModule
import net.machinemuse.powersuits.powermodule.weapon.PlasmaCannonModule
import net.machinemuse.powersuits.powermodule.weapon.RailgunModule
import net.machinemuse.utils.MuseStringUtils
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraftforge.common.config.Configuration
import org.lwjgl.input.Keyboard
import java.io.{PrintWriter, FileOutputStream, FileInputStream, File}
import java.util.Arrays
import java.util.Collections
import java.util.List

import scala.io.Source

/**
 * Initial attempt at storing all tweakable/configurable values in one class.
 * This got really messy really fast so it's in the process of being
 * reworked.
 *
 * @author MachineMuse
 */
object Config {
  /**
   * Called in post-init. Extracts recipes if the configuration value is not found.
   */
  def extractRecipes() = {
    val key = "Auto-extract recipes"
    if(!config.hasKey(Configuration.CATEGORY_GENERAL, key) || config.get(Configuration.CATEGORY_GENERAL, key, false).getBoolean) {
      config.get(Configuration.CATEGORY_GENERAL, key, false)
      var found=false
      if(ModCompatibility.isThermalExpansionLoaded) {
        found=true
        //TE
        copyRecipe("mps-thermalexpansion.recipes")
      }
      if (ModCompatibility.isIndustrialCraftLoaded) {
        found=true
        //IC2
        copyRecipe("mps-ic2.recipes")
      }
      if (ModCompatibility.isEnderIOLoaded) {
        found=true
        //enderIO
        copyRecipe("mps-enderio.recipes")
      }
      if(!found) {
        //vanilla
        copyRecipe("mps-vanilla.recipes")
      }
    }
  }

  def copyRecipe(inFile:String): Unit = {
    val src = classOf[CommonProxy].getClassLoader.getResourceAsStream(inFile)
    val dest = new File(Numina.configDir.toString + "/machinemuse/recipes/" + inFile)
    if(!dest.exists()) {
      Files.copy(src, dest.toPath)
    }
  }
  /**
   * Called in the pre-init phase of initialization, informs Forge that we
   * want the following blockIDs.
   *
   * @param config The Forge configuration object which will handle such
   *               requests.
   */
  def init(config: Configuration) {
    Config.config = config
    config.load
    config.save
  }

  /**
   * The packet channel for this mod. We will only listen for and send packets
   * on this 'channel'. Max of 16 characters.
   *
   * @return
   */
  def getNetworkChannelName: String = {
    return "powerSuits"
  }

  /**
   * The default creative tab to add all these items to. This behaviour may
   * change if more items are added, but for now there are only 5 items and 1
   * block, so misc is the most appropriate target.
   *
   * @return
   */
  def getCreativeTab: CreativeTabs = {
    return MPSCreativeTab
  }

  /**
   * Chance of each item being returned when salvaged.
   *
   * @return percent chance, 0.0 to 1.0
   */
  def getSalvageChance: Double = {
    return config.get(Configuration.CATEGORY_GENERAL, "Salvage Ratio", 0.9).getDouble(0.9)
  }

  /**
   * The maximum amount of armor contribution allowed per armor piece. Total
   * armor when the full set is worn can never exceed 4 times this amount.
   *
   * @return
   */
  def getMaximumArmorPerPiece: Double = {
    return Math.max(0.0, config.get(Configuration.CATEGORY_GENERAL, "Maximum Armor per Piece", 6.0).getDouble(6.0))
  }

  def getMaximumFlyingSpeedmps: Double = {
    return config.get(Configuration.CATEGORY_GENERAL, "Maximum flight speed (in m/s)", 25.0).getDouble(25.0)
  }

  def useMouseWheel: Boolean = {
    return config.get(Configuration.CATEGORY_GENERAL, "Use Mousewheel to change modes", true).getBoolean(true)
  }

  def addModule(module: IPowerModule) {
    ModuleManager.addModule(module)
  }

  /**
   * Load all the modules in the config file into memory. Eventually. For now,
   * they are hardcoded.
   */
  def loadPowerModules {
    val ARMORONLY: List[IModularItem] = Arrays.asList[IModularItem](MPSItems.powerArmorHead.asInstanceOf[IModularItem], MPSItems.powerArmorTorso.asInstanceOf[IModularItem], MPSItems.powerArmorLegs.asInstanceOf[IModularItem], MPSItems.powerArmorFeet.asInstanceOf[IModularItem])
    val ALLITEMS: List[IModularItem] = Arrays.asList(MPSItems.powerArmorHead.asInstanceOf[IModularItem], MPSItems.powerArmorTorso.asInstanceOf[IModularItem], MPSItems.powerArmorLegs.asInstanceOf[IModularItem], MPSItems.powerArmorFeet.asInstanceOf[IModularItem], MPSItems.powerTool.asInstanceOf[IModularItem])
    val HEADONLY: List[IModularItem] = Collections.singletonList(MPSItems.powerArmorHead.asInstanceOf[IModularItem])
    val TORSOONLY: List[IModularItem] = Collections.singletonList(MPSItems.powerArmorTorso.asInstanceOf[IModularItem])
    val LEGSONLY: List[IModularItem] = Collections.singletonList(MPSItems.powerArmorLegs.asInstanceOf[IModularItem])
    val FEETONLY: List[IModularItem] = Collections.singletonList(MPSItems.powerArmorFeet.asInstanceOf[IModularItem])
    val TOOLONLY: List[IModularItem] = Collections.singletonList(MPSItems.powerTool.asInstanceOf[IModularItem])
    addModule(new BasicPlatingModule(ARMORONLY))
    addModule(new DiamondPlatingModule(ARMORONLY))
    addModule(new EnergyShieldModule(ARMORONLY))
    addModule(new HeatSinkModule(ARMORONLY))
    addModule(new AxeModule(TOOLONLY))
    addModule(new PickaxeModule(TOOLONLY))
    addModule(new ShovelModule(TOOLONLY))
    addModule(new ShearsModule(TOOLONLY))
    addModule(new HoeModule(TOOLONLY))
    addModule(new LuxCapacitor(TOOLONLY))
    addModule(new OmniWrenchModule(TOOLONLY))
    addModule(new FieldTinkerModule(TOOLONLY))
    addModule(new MeleeAssistModule(TOOLONLY))
    addModule(new PlasmaCannonModule(TOOLONLY))
    addModule(new RailgunModule(TOOLONLY))
    addModule(new BladeLauncherModule(TOOLONLY))
    addModule(new BasicBatteryModule(ALLITEMS))
    addModule(new AdvancedBatteryModule(ALLITEMS))
    addModule(new EliteBatteryModule(ALLITEMS))
    addModule(new ParachuteModule(TORSOONLY))
    addModule(new GliderModule(TORSOONLY))
    addModule(new JetPackModule(TORSOONLY))
    addModule(new SprintAssistModule(LEGSONLY))
    addModule(new JumpAssistModule(LEGSONLY))
    addModule(new SwimAssistModule(LEGSONLY))
    addModule(new ClimbAssistModule(LEGSONLY))
    addModule(new JetBootsModule(FEETONLY))
    addModule(new ShockAbsorberModule(FEETONLY))
    addModule(new WaterElectrolyzerModule(HEADONLY))
    addModule(new NightVisionModule(HEADONLY))
    addModule(new BinocularsModule(HEADONLY))
    addModule(new FlightControlModule(HEADONLY))
    addModule(new InvisibilityModule(TORSOONLY))
    addModule(new BlinkDriveModule(TOOLONLY))
    addModule(new DiamondPickUpgradeModule(TOOLONLY))
    addModule(new AquaAffinityModule(TOOLONLY))
    addModule(new CoolingSystemModule(TORSOONLY))
    addModule(new TintModule(TOOLONLY))
    addModule(new TransparentArmorModule(ARMORONLY))
    addModule(new CosmeticGlowModule(ARMORONLY))
  }

  def getConfig: Configuration = {
    return config
  }

  def doAdditionalInfo: Boolean = {
    if (FMLCommonHandler.instance.getEffectiveSide eq Side.CLIENT) {
      if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
        return true
      }
    }
    return false
  }

  def additionalInfoInstructions: AnyRef = {
    var message: String = "Press SHIFT for more information."
    message = MuseStringUtils.wrapMultipleFormatTags(message, MuseStringUtils.FormatCodes.Grey, MuseStringUtils.FormatCodes.Italic)
    return message
  }

  def useGraphicalMeters: Boolean = {
    return config.get(Configuration.CATEGORY_GENERAL, "Use Graphical Meters", true).getBoolean(true)
  }

  def baseMaxHeat: Double = {
    return config.get(Configuration.CATEGORY_GENERAL, "Base Heat Cap", 50.0).getDouble(50.0)
  }

  def allowConflictingKeybinds: Boolean = {
    return config.get(Configuration.CATEGORY_GENERAL, "Allow Conflicting Keybinds", true).getBoolean(true)
  }

  def useCustomFonts: Boolean = {
    return config.get("Font", "Use Custom Font Engine", true).getBoolean(true)
  }

  def fontDetail: Double = {
    return config.get("Font", "Font Detail Multiplier", 4).getDouble(4)
  }

  def fontURI: String = {
    return config.get("Font", "Font URI", Config.RESOURCE_PREFIX + "fonts/cra.ttf").getString
  }

  def fontName: String = {
    return config.get("Font", "Native Font Name (Overrides URI)", "").getString
  }

  def fontAntiAliasing: Boolean = {
    return config.get("Font", "Font Anti-Aliasing", false).getBoolean(false)
  }

  def glowMultiplier: Int = {
    return config.get("Graphics", "Bloom Multiplier", 3).getInt(3)
  }

  def useShaders: Boolean = {
    return config.get("Graphics", "Use Pixel/Vertex Shaders", true).getBoolean(true)
  }

  def setConfigFolderBase(folder: File) {
    configFolder = new File(folder.getAbsolutePath + "/machinemuse")
  }

  final val RESOURCE_PREFIX: String = "powersuits:"
  final val TEXTURE_PREFIX: String = "powersuits:textures/"
  final val SOUND_PREFIX: String = RESOURCE_PREFIX + "sound/"
  final val LANG_PREFIX: String = RESOURCE_PREFIX + "lang/"
  final val SEBK_ARMOR_PATH: String = TEXTURE_PREFIX + "models/sebkarmor.png"
  final val SEBK_ARMORPANTS_PATH: String = TEXTURE_PREFIX + "models/sebkarmorpants.png"
  final val TINKERTABLE_TEXTURE_PATH: String = TEXTURE_PREFIX + "models/tinkertable_tx.png"
  final val ARMOR_TEXTURE_PATH: String = TEXTURE_PREFIX + "models/diffuse.png"
  final val BLANK_ARMOR_MODEL_PATH: String = TEXTURE_PREFIX + "models/blankarmor.png"
  final val SEBK_TOOL_TEXTURE: String = TEXTURE_PREFIX + "models/tool.png"
  final val LIGHTNING_TEXTURE: String = TEXTURE_PREFIX + "gui/lightning-medium.png"
  final val CITIZENJOE_ARMOR_PATH: String = TEXTURE_PREFIX + "models/joearmor.png"
  final val CITIZENJOE_ARMORPANTS_PATH: String = TEXTURE_PREFIX + "models/joearmorpants.png"
  final val GLASS_TEXTURE: String = TEXTURE_PREFIX + "gui/glass.png"
  var configFolder: File = null
  private var config: Configuration = null
  var canUseShaders: Boolean = false

  def addCustomInstallCosts():Unit = {
    val installCostFile = new File(configFolder, "custominstallcosts.json")
    val gson = new Gson()
    if(installCostFile.exists) {
      val source = Source.fromFile(installCostFile)
      val string = source.mkString
      source.close()
      MuseLogger.logDebug(string)
      val costs = gson.fromJson[Array[InstallCost]](string, classOf[Array[InstallCost]])
      costs.foreach { cost=>
        val moduleName = cost.moduleName
        val item = GameRegistry.findItem(cost.modId, cost.itemName)
        if(item != null) {
          val metadata = if (cost.itemMetadata == null) 0 else cost.itemMetadata.intValue()
          val quantity = if (cost.itemQuantity == null) 1 else cost.itemQuantity.intValue()
          val stack = new ItemStack(item, quantity, metadata)
          if(stack != null) {
            ModuleManager.addCustomInstallCost(moduleName, stack)
          } else {
            MuseLogger.logError("Invalid Itemstack in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]")
          }
        } else {
          MuseLogger.logError("Invalid Item in custom install cost. Module [" + cost.moduleName + "], item [" + cost.itemName + "]")
        }
      }
    } else {
      installCostFile.createNewFile()
      val examplecost = new InstallCost()
      examplecost.moduleName = "Shock Absorber"
      examplecost.itemName = "wool"
      examplecost.modId = "minecraft"
      examplecost.itemQuantity = 2
      examplecost.itemMetadata = 0
      val examplecost2 = new InstallCost()
      examplecost2.moduleName = "Shock Absorber"
      examplecost2.itemName = "powerArmorComponent"
      examplecost2.modId = "powersuits"
      examplecost2.itemQuantity = 2
      examplecost2.itemMetadata = 2
      val output = Array(examplecost, examplecost2)
      val json = gson.toJson(output)
      val dest = new PrintWriter(installCostFile)
      dest.write(json)
      dest.close()
    }
  }
}