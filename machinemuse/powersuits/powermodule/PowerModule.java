/**
 * 
 */
package machinemuse.powersuits.powermodule;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.common.MuseLogger;
import machinemuse.powersuits.item.ItemPowerArmor;
import machinemuse.powersuits.item.ItemPowerTool;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

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
		public static final String MAXENERGY = "Maximum Energy";
		public static final String CURRENERGY = "Current Energy";
		public static final String LEVEL = "Level";
		public static final String DURABILITY = "Durability";
		public static final String nbtPrefix = "mmmpsmod";
	}

	/**
	 * Initialization for the module list
	 */
	static {
		new PowerModuleIronPlating();
		new PowerModuleBattery();
	}

	/**
	 * Module List
	 */
	public static BiMap<String, PowerModule> allModules = HashBiMap.create();
	/**
	 * Module validity mappings. Slot 0-3 = armor, 4 = tool
	 */
	public static List<PowerModule>[] validModulesForSlot = new ArrayList[5];

	/**
	 * Constructor adds the module to the appropriate lists.
	 */
	public PowerModule() {
		for (int i = 0; i < 5; i++) {
			if (validModulesForSlot[i] == null) {
				validModulesForSlot[i] = new ArrayList();
			}
			if (getValidSlots()[i]) {
				validModulesForSlot[i].add(this);
			}
		}
		allModules.put(getName(), this);
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
	 * Returns the weight of the module in kg. More than 25kg of equipment will
	 * start to slow a player down.
	 * 
	 * @return Module's weight in kg
	 */
	public abstract float getWeight(
			EntityPlayer player, NBTTagCompound moduleTag);

	/**
	 * Returns the current upgrade level of the module based on the nbttag.
	 * Level 0 modules should do nothing.
	 * 
	 * @return Module's weight in kg
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
	 * Returns the current armor value provided by the module.
	 * 
	 * @return
	 */
	public abstract int getArmorValue(
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

	/**
	 * 
	 * 
	 * @param next
	 * @return
	 */
	public static NBTTagCompound getItemModules(ItemStack stack) {
		NBTTagCompound augs = null;
		if (stack.hasTagCompound()) {
			NBTTagCompound stackTag = stack.getTagCompound();
			if (stackTag.hasKey(Constants.nbtPrefix)) {
				augs = stackTag.getCompoundTag(Constants.nbtPrefix);
			} else {
				augs = new NBTTagCompound();
				stackTag.setCompoundTag(Constants.nbtPrefix, augs);
			}
		} else {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
			augs = new NBTTagCompound();
			stackTag.setCompoundTag(Constants.nbtPrefix, augs);
		}
		return augs;
	}

	public static void addModule(String id, PowerModule p) {
		if (allModules.containsKey(id)) {
			MuseLogger.logError("Warning: module " + p.getName()
					+ " already bound!");
		}
		allModules.put(id, p);
	}

	public static PowerModule getModuleByID(String i) {
		if (allModules.containsKey(i)) {
			return allModules.get(i);
		} else {
			MuseLogger.logError("Module " + i + " not assigned!");
			return null;
		}
	}

	public static PowerModule getModuleFromNBT(NBTTagCompound moduleTag) {
		return getModuleByID(moduleTag.getString("Name"));
	}

	public static String getModuleID(PowerModule module) {
		if (allModules.inverse().containsKey(module)) {
			return allModules.inverse().get(module);
		} else {
			MuseLogger.logError("Module " + module.getName()
					+ " not initialized!");
			return "";
		}
	}

	public static List<PowerModule> getValidModulesForItem(Item item) {
		if (item instanceof ItemPowerArmor) {
			ItemPowerArmor itema = (ItemPowerArmor) item;
			return validModulesForSlot[itema.armorType];
		} else if (item instanceof ItemPowerTool) {
			return validModulesForSlot[4];
		} else {
			return null;
		}
	}
}
