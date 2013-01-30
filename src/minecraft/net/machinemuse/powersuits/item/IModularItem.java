package net.machinemuse.powersuits.item;

import ic2.api.ICustomElectricItem;
import icbm.api.IEMPItem;

import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import universalelectricity.core.implement.IItemElectric;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem
		extends
		IItemElectric, // Universal Electricity
		ICustomElectricItem, // Industrial Craft 2
		IEMPItem // for ICBM EMP interfacing
{

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
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack);

}
