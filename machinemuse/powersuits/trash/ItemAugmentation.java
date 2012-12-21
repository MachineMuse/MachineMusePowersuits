package machinemuse.powersuits.trash;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.common.Config;
import net.minecraft.item.Item;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

public class ItemAugmentation extends Item {
	static List<ItemAugmentation> allAugs = new ArrayList<ItemAugmentation>();
	public static int index;

	public ItemAugmentation(Config.Items item) {
		super(Config.getAssignedItemID(item));
		index = shiftedIndex;
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		allAugs.add(this);
	}

	public static List<ItemAugmentation> getAllAugs() {
		return allAugs;
	}

	public boolean canGoInSlot(AugSlot.SlotType type) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	/**
	 * Gets an icon index based on an item's damage value
	 */
	public int getIconFromDamage(int par1)
	{
		return this.iconIndex;
	}

	// public String getItemNameIS(ItemStack par1ItemStack)
	// {
	// int var2 = MathHelper.clamp_int(par1ItemStack.getItemDamage(), 0, 15);
	// // return "augmentation." + AugTypes.values()[var2].idName;
	// }

	// @SideOnly(Side.CLIENT)
	// /**
	// * returns a list of items with the same ID, but different meta (eg: dye
	// returns 16 items)
	// */
	// // public void getSubItems(int id, CreativeTabs creativeTab,
	// // List tabItems)
	// // {
	// // for (AugTypes i : AugTypes.values())
	// // {
	// // tabItems.add(new ItemStack(id, 1, i.id));
	// // }
	// // }
}
