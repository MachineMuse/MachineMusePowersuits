package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem extends IItemElectric {

	/**
	 * Returns the item's 'type' as assigned in the Config.Items enum. TODO:
	 * Eliminate dependence on this function.
	 * 
	 * @return
	 */
	public Config.Items getItemType();

	/**
	 * Gets the item's extended summary for displaying in the gui.
	 * 
	 * @param stack
	 * @return
	 */
	public List<String> getLongInfo(ItemStack stack);

}
