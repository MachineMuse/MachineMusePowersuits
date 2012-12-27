package net.machinemuse.powersuits.trash;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IPowerModuleTool {
	public static final String DURABILITY = "Durability";

	public abstract float getStrVsBlock(ItemStack stack, Block block, int meta);

	public abstract boolean onBlockDestroyed(ItemStack stack, World world,
			int blockID, int x, int y, int z,
			EntityLiving destroyerEntity);
	// if (Block.blocksList[blockID]
	// .getBlockHardness(world, x, y, z) != 0.0D) {
	// stack.damageItem(1, par7EntityLiving);
	// }
	//
	// return true;
}
