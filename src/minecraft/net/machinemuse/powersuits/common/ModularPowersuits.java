package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.event.EventHandler;
import net.machinemuse.powersuits.event.MovementManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.ItemPowerArmorFeet;
import net.machinemuse.powersuits.item.ItemPowerArmorHead;
import net.machinemuse.powersuits.item.ItemPowerArmorLegs;
import net.machinemuse.powersuits.item.ItemPowerArmorTorso;
import net.machinemuse.powersuits.item.ItemPowerTool;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * Main mod class. This is what Forge loads to get the mod up and running, both
 * server- and client-side.
 * 
 * @author MachineMuse
 * 
 */
// Informs forge that this is a base mod class, and gives it some info for the
// FML mod list. This is also where it looks to see if your client's version
// matches the server's.
@Mod(modid = "mmmPowersuits", name = "MachineMuse's Modular Powersuits", version = "@MOD_VERSION@")
// Informs forge of the requirements:
//
// clientSideRequired means players can't connect without it. True for things
// that add new blocks/items, false for things like bukkit plugins.
//
// serverSideRequired means clients can't connect to servers that don't have it.
// This isn't a strict restriction currently but it can cause problems if the
// mod does anything potentially incompatible in its preInit function. True for
// things that add new blocks/items, false for things like Rei's Minimap or
// Inventory Tweaks.
@NetworkMod(clientSideRequired = true, serverSideRequired = true,
		clientPacketHandlerSpec =
		@SidedPacketHandler(channels = { "mmmPowersuits" }, packetHandler = MusePacketHandler.class),
		serverPacketHandlerSpec =
		@SidedPacketHandler(channels = { "mmmPowersuits" }, packetHandler = MusePacketHandler.class))
public class ModularPowersuits {

	public static ItemPowerArmorHead powerArmorHead;
	public static ItemPowerArmorTorso powerArmorTorso;
	public static ItemPowerArmorLegs powerArmorLegs;
	public static ItemPowerArmorFeet powerArmorFeet;
	public static ItemPowerTool powerTool;
	public static ItemComponent components;
	public static BlockTinkerTable tinkerTable;

	/**
	 * The instance of the mod that Forge will access. Note that it has to be
	 * set by hand in the preInit step.
	 */
	@Instance("ModularPowersuits")
	public static ModularPowersuits instance;

	/**
	 * Tells Forge what classes to load for the client and server proxies. These
	 * execute side-specific code like registering renderers (for the client) or
	 * different tick handlers (for the server).
	 */
	@SidedProxy(clientSide = "net.machinemuse.powersuits.client.ClientProxy", serverSide = "net.machinemuse.powersuits.common.CommonProxy")
	public static CommonProxy proxy;

	/**
	 * In the preInit step you only want to load configs, reserve block/item
	 * IDs, and inform Forge if your mod has to be loaded after any others. No
	 * heavy loading or registering should occur here, because it happens as
	 * soon as they start Minecraft and there's no guarantee that your mod will
	 * be loaded.
	 * 
	 * @param event
	 *            An event object with useful data
	 */
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		Config.init(new Configuration(event.getSuggestedConfigurationFile()));
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new MovementManager());
	}

	public static Config config;
	/**
	 * A static handle for the blocks and items. We only want one instance of
	 * each of them.
	 */
	public static GuiHandler guiHandler = new GuiHandler();

	/**
	 * This is where all the heavy loading and registering of handlers goes.
	 * This occurs when you connect to a server or open a world.
	 * 
	 * @param event
	 *            An event object with useful data
	 */
	@Init
	public void load(FMLInitializationEvent event) {
		powerArmorHead = new ItemPowerArmorHead();
		powerArmorTorso = new ItemPowerArmorTorso();
		powerArmorLegs = new ItemPowerArmorLegs();
		powerArmorFeet = new ItemPowerArmorFeet();
		powerTool = new ItemPowerTool();
		tinkerTable = new BlockTinkerTable();
		components = new ItemComponent();
		components.populate();

		Config.loadPowerModules();

		EntityRegistry.registerModEntity(EntityPlasmaBolt.class, "entityPlasmaBolt", 2477, this, 64, 20, true);
		proxy.registerHandlers();
		proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}

	/**
	 * Stuff to do after the player connects. This is for things that need to
	 * wait until the world is completely loaded before initializing.
	 * 
	 * @param event
	 *            An event object with useful data
	 */
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
		RecipeManager.addRecipes();
		ModCompatability.registerModSpecificModules();
		Config.getConfig().save();
	}
}