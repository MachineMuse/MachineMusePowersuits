package machinemuse.powersuits.block;

import machinemuse.powersuits.common.CommonProxy;
import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.PowersuitsMod;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 * 
 * @author MachineMuse
 * 
 */
public class BlockTinkerTable extends Block {

	/**
	 * Constructor. Reads all the block info from Config.
	 */
	public BlockTinkerTable() {
		super(Config.getAssignedBlockID(Config.Blocks.TinkerTable),
				Config.Blocks.TinkerTable.textureIndex,
				Config.Blocks.TinkerTable.material);
		setHardness(Config.Blocks.TinkerTable.hardness);
		setStepSound(Config.Blocks.TinkerTable.stepSound);
		setBlockName(Config.Blocks.TinkerTable.idName);
		setCreativeTab(Config.getCreativeTab());
		LanguageRegistry.addName(this, Config.Blocks.TinkerTable.englishName);
		MinecraftForge.setBlockHarvestLevel(this,
				Config.Blocks.TinkerTable.harvestTool,
				Config.Blocks.TinkerTable.harvestLevel);
		ItemStack recipeResult = new ItemStack(this);
		GameRegistry.registerBlock(this);

	}

	@Override
	public String getTextureFile() {
		return CommonProxy.BLOCK_PNG;
	}

	/**
	 * Called upon block activation (right click on the block.)
	 */
	public boolean onBlockActivated(World world, int x, int y,
			int z, EntityPlayer player, int par6, float par7,
			float par8, float par9)
	{
		if (player.isSneaking()) {
			return false;
		}
		player.openGui(PowersuitsMod.instance,
				Config.Guis.GuiTinkerTable.ordinal(),
				world, x, y, z);
		return true;
	}
}
