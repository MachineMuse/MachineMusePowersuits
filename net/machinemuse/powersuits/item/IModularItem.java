package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem {

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

	/**
	 * Adds information to the item's tooltip when 'getting' it.
	 * 
	 * @param stack
	 *            The itemstack to get the tooltip for
	 * @param player
	 *            The player (client) viewing the tooltip
	 * @param currentTipList
	 *            A list of strings containing the existing tooltip. When
	 *            passed, it will just contain the name of the item;
	 *            enchantments and lore are appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in
	 *            their settings.
	 */
	public void addInformation(ItemStack stack,
			EntityPlayer player, List currentTipList, boolean advancedToolTips);

}
