package net.machinemuse.powersuits.common;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.event.EventHandler;
import net.machinemuse.powersuits.item.*;
import net.machinemuse.powersuits.network.MusePacketHandler;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import basiccomponents.common.BasicComponents;
import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

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
@Mod(modid = "mmmPowersuits", name = "MachineMuse Modular Powersuits", version = "0.0.1")
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
public class PowersuitsMod {
	
	/**
	 * The instance of the mod that Forge will access. Note that it has to be
	 * set by hand in the preInit step.
	 */
	@Instance("PowersuitsMod") public static PowersuitsMod instance;
	
	/**
	 * Tells Forge what classes to load for the client and server proxies. These
	 * execute side-specific code like registering renderers (for the client) or
	 * different tick handlers (for the server).
	 */
	@SidedProxy(clientSide = "net.machinemuse.powersuits.client.ClientProxy", serverSide = "net.machinemuse.powersuits.common.CommonProxy") public static CommonProxy proxy;
	
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
	@PreInit public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		Config.init(new Configuration(
				event.getSuggestedConfigurationFile()));
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}
	
	public static Config config;
	/**
	 * A static handle for the blocks and items. We only want one instance of
	 * each of them.
	 */
	public static List<Block> allBlocks = new ArrayList<Block>();
	public static List<Item> allItems = new ArrayList<Item>();
	public static GuiHandler guiHandler = new GuiHandler();
	
	/**
	 * This is where all the heavy loading and registering of handlers goes.
	 * This occurs when you connect to a server or open a world.
	 * 
	 * @param event
	 *            An event object with useful data
	 */
	@Init public void load(FMLInitializationEvent event) {
		loadBlocks();
		
		loadItems();
		
		proxy.registerHandlers();
		proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}
	
	/**
	 * Custom function to collect all the item-loading in one place.
	 */
	public static void loadItems() {
		// Recipe
		ItemStack iron = new ItemStack(Item.ingotIron);
		ItemStack circuit = new ItemStack(BasicComponents.itemCircuit, 1, 0);
		
		ItemPowerArmor item = new ItemPowerArmorHead();
		allItems.add(item);
		GameRegistry.addRecipe(new ItemStack(item),
				"III",
				"C C",
				'I', iron, 'C', circuit);
		
		item = new ItemPowerArmorTorso();
		allItems.add(item);
		GameRegistry.addRecipe(new ItemStack(item),
				"I I",
				"CIC",
				"III",
				'I', iron, 'C', circuit);
		
		item = new ItemPowerArmorLegs();
		allItems.add(item);
		GameRegistry.addRecipe(new ItemStack(item),
				"III",
				"C C",
				"I I",
				'I', iron, 'C', circuit);
		
		item = new ItemPowerArmorFeet();
		allItems.add(item);
		GameRegistry.addRecipe(new ItemStack(item),
				"C C",
				"I I",
				'I', iron, 'C', circuit);
		
		ItemPowerTool tool = new ItemPowerTool();
		allItems.add(tool);
		GameRegistry.addRecipe(new ItemStack(tool),
				" C ",
				"CI ",
				" IC",
				'I', iron, 'C', circuit);
		
		Config.loadTinkerings();
		
	}
	
	/**
	 * Custom function to collect all the block-loading in one place.
	 */
	public static void loadBlocks() {
		Block tinkTable = BlockTinkerTable.instance();
		
		allBlocks.add(tinkTable);
	}
	
	/**
	 * Stuff to do after the player connects. This is for things that need to
	 * wait until the world is completely loaded before initializing.
	 * 
	 * @param event
	 *            An event object with useful data
	 */
	@PostInit public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit();
	}
}