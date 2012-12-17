package machinemuse.powersuits.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BlockTinkerTable extends Block {

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
		player.openGui(PowersuitsMod.instance, 0, world, x, y, z);
		return true;
	}
}
