package machinemuse.powersuits.common;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.client.ClientPacketHandler;
import machinemuse.powersuits.common.augmentation.AugmentationTypes;
import machinemuse.powersuits.common.block.BlockTinkerTable;
import machinemuse.powersuits.common.item.ItemPowerArmor;
import machinemuse.powersuits.common.item.ItemPowerTool;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.common.Configuration;
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

@Mod(modid = "mmmPowersuits", name = "MachineMuse Modular Powersuits", version = "0.0.1")
@NetworkMod(clientSideRequired = true, serverSideRequired = true,
		clientPacketHandlerSpec =
		@SidedPacketHandler(channels = { "mmPowersuits" }, packetHandler = ClientPacketHandler.class),
		serverPacketHandlerSpec =
		@SidedPacketHandler(channels = { "mmPowersuits" }, packetHandler = ServerPacketHandler.class))
public class PowersuitsMod {

	// The instance of your mod that Forge uses.
	@Instance("PowersuitsMod")
	public static PowersuitsMod instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide = "machinemuse.powersuits.client.ClientProxy", serverSide = "machinemuse.powersuits.common.CommonProxy")
	public static CommonProxy proxy;

	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		instance = this;
		Config.init(new Configuration(
				event.getSuggestedConfigurationFile()));
	}

	public static List<Block> allBlocks = new ArrayList<Block>();
	public static List<Item> allItems = new ArrayList<Item>();
	public static GuiHandler guiHandler = new GuiHandler();

	@Init
	public void load(FMLInitializationEvent event) {
		allBlocks.add(new BlockTinkerTable());

		loadItems();

		proxy.registerHandlers();
		proxy.registerRenderers();
		NetworkRegistry.instance().registerGuiHandler(this, guiHandler);
	}

	public static void loadItems() {
		ItemPowerArmor item = new ItemPowerArmor(Config.Items.PowerArmorHead);
		item.setValidAugTypes(AugmentationTypes.validHeadAugmentations());
		allItems.add(item);

		item = new ItemPowerArmor(Config.Items.PowerArmorTorso);
		item.setValidAugTypes(AugmentationTypes.validTorsoAugmentations());
		allItems.add(item);

		item = new ItemPowerArmor(Config.Items.PowerArmorLegs);
		item.setValidAugTypes(AugmentationTypes.validLegsAugmentations());
		allItems.add(item);

		item = new ItemPowerArmor(Config.Items.PowerArmorFeet);
		item.setValidAugTypes(AugmentationTypes.validFeetAugmentations());
		allItems.add(item);

		ItemPowerTool tool = new ItemPowerTool(Config.Items.PowerTool);
		item.setValidAugTypes(AugmentationTypes.validToolAugmentations());
		allItems.add(tool);
	}

	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		// Stub Method
	}
}