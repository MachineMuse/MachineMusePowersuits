/**
 * 
 */
package net.machinemuse.powersuits.trash;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * PowerModule is the base class for the modules which can be installed in
 * various power armor items.
 * 
 * @author MachineMuse
 * 
 */
public abstract class PowerModule {
	/**
	 * Contains constant strings. Mainly for consistency.
	 * 
	 * @author MachineMuse
	 */
	public static final class Constants {
		public static final String NAME = "Name";
		public static final String LEVEL = "Level";
		public static final String nbtPrefix = "mmmpsmod";
	}

	/**
	 * Constructor adds the module to the appropriate lists.
	 */
	public PowerModule() {
		for (int i = 0; i < 5; i++) {
			if (getValidSlots()[i]) {
				ModuleUtils.validModulesForSlot[i].add(this);
			}
		}
		ModuleUtils.addModule(getName(), this);
	}

	/**
	 * Since modules themselves should be singletons, this is our instance
	 * pattern.
	 * 
	 * @return A new NBTTagCompound describing a new instance of this module
	 *         (pre-upgrade)
	 */
	public abstract NBTTagCompound newModuleTag();

	/**
	 * Returns the name of the module. Should be unique. Used for both tooltips
	 * and for ID mapping.
	 * 
	 * @return A string
	 */
	public abstract String getName();

	/**
	 * Returns the current upgrade level of the module based on the nbttag.
	 * Level 0 modules should do nothing.
	 * 
	 * @return
	 */
	public int getLevel(
			EntityPlayer player, NBTTagCompound moduleTag) {
		return moduleTag.getInteger(Constants.LEVEL);
	}

	/**
	 * Returns the maximum level of the upgrade. If the upgrade's level is equal
	 * or greater, further upgrades will not be allowed.
	 * 
	 * @return
	 */
	public abstract int getMaxLevel(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns the lines of text to appear in the module's tooltip in the
	 * tinkertable UI.
	 * 
	 * @return
	 */
	public abstract List<String> getTooltip(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns the list of itemstacks a player should receive if they uninstall
	 * this module.
	 * 
	 * @return
	 */
	public abstract List<ItemStack> getRefund(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns the list of itemstacks required to upgrade this module.
	 * 
	 * @return
	 */
	public abstract List<ItemStack> getCost(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns the a list of modules which must be purchased before this one
	 * will be available. Can be different depending on level.
	 * 
	 * @return
	 */
	public abstract List<PowerModule> getPrerequisites(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns an array of booleans, one for each possible itemslot, describing
	 * whether or not the module can be slotted in that item.
	 * 
	 * @return
	 */
	public abstract boolean[] getValidSlots();

	/**
	 * Returns a category name for the module. Modules with the same category
	 * will be grouped together in the UI.
	 * 
	 * @return
	 */
	public abstract String getCategory();

	/**
	 * Event trigger in the player tick.
	 */
	public abstract void onPlayerTick(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Event trigger when the item is assigned durability damage.
	 * 
	 * @param player
	 * @param moduleTag
	 * @param damageAmount
	 */
	public abstract void onDamageItem(
			EntityPlayer player, NBTTagCompound moduleTag, float damageAmount);

	/**
	 * Event trigger for when the item is upgraded. Should include leveling up.
	 * 
	 * @param player
	 * @param moduleTag
	 * @return
	 */
	public abstract boolean onUpgrade(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Texture file for the module in GUI.
	 * 
	 * @return
	 */
	public String getIconFile() {
		return "/moduleicons.png";
	}

	/**
	 * Texture file for the module when slotted in an item.
	 * 
	 * @return
	 */
	public String getItemOverlayFile() {
		return "/moduleoverlays.png";
	}

	/**
	 * Index in the 256p*256p icon file. Indices go like this:
	 * 
	 * <pre>
	 *  0  1  2  3 ...
	 * 16 18 19 20 ...
	 * 32 33 34 35 ...
	 * </pre>
	 * 
	 * @return
	 */
	public abstract int getIconIndex();

	/**
	 * Index in the item overlay file, same convention as icon index
	 * 
	 * @return
	 */
	public abstract int getItemOverlayIndex();

}
