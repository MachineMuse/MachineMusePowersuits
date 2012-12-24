/**
 * 
 */
package machinemuse.powersuits.client;

import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.Config.Blocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

/**
 * @author Claire
 * 
 */
public class BlockRenderer implements ISimpleBlockRenderingHandler {

	/**
	 * 
	 */
	public BlockRenderer() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#
	 * renderInventoryBlock(net.minecraft.block.Block, int, int,
	 * net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID,
			RenderBlocks renderer) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#renderWorldBlock
	 * (net.minecraft.world.IBlockAccess, int, int, int,
	 * net.minecraft.block.Block, int,
	 * net.minecraft.client.renderer.RenderBlocks)
	 */
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z,
			Block block, int modelId, RenderBlocks renderer) {

		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#
	 * shouldRender3DInInventory()
	 */
	@Override
	public boolean shouldRender3DInInventory() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler#getRenderId()
	 */
	@Override
	public int getRenderId() {
		return Config.getAssignedBlockID(Blocks.TinkerTable);
	}

}
