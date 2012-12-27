package net.machinemuse.powersuits.trash;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.common.MuseLogger;
import net.machinemuse.powersuits.item.ItemPowerArmor;
import net.machinemuse.powersuits.item.ItemPowerTool;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public abstract class ModuleUtils {

	protected static boolean initialized = false;
	static {
		if (!initialized) {
			init();
		}
	}
	/**
	 * Module List
	 */
	public static BiMap<String, PowerModule> allModules;
	/**
	 * Module validity mappings. Slot 0-3 = armor, 4 = tool
	 */
	public static List<PowerModule>[] validModulesForSlot;

	public static final String DURABILITY = "Durability";

	/**
	 * Reduces durability of the passed module tag by the specified amount, to a
	 * minimum of 0. Returns 0 unless there is some remainder.
	 * 
	 * @param compound
	 * @return
	 */
	public static float reduceDurability(NBTTagCompound moduleTag,
			float damageAmount) {
		float durability = 0;
		float absorbed = 0;
		if (moduleTag.hasKey(DURABILITY)) {
			durability = moduleTag.getFloat(DURABILITY);
			if (durability > damageAmount) {
				absorbed = damageAmount;
			} else {
				absorbed = durability;
			}
			moduleTag.setFloat(DURABILITY, durability - absorbed);
		}
		return (int) damageAmount - absorbed;
	}

	/**
	 * Initialization for the module list
	 */
	protected static void init() {

		allModules = HashBiMap.create();
		validModulesForSlot = new ArrayList[5];
		for (int i = 0; i < 5; i++) {
			validModulesForSlot[i] = new ArrayList();
		}
		new PowerModuleIronPlating();
		new PowerModuleBattery();
		initialized = true;
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

	/**
	 * 
	 * 
	 * @param next
	 * @return
	 */
	public static NBTTagCompound getItemModules(ItemStack stack) {
		NBTTagCompound modules = null;
		if (stack.hasTagCompound()) {
			NBTTagCompound stackTag = stack.getTagCompound();
			if (stackTag.hasKey(PowerModule.Constants.nbtPrefix)) {
				modules = stackTag
						.getCompoundTag(PowerModule.Constants.nbtPrefix);
			} else {
				modules = new NBTTagCompound();
				stackTag.setCompoundTag(PowerModule.Constants.nbtPrefix,
						modules);
			}
		} else {
			NBTTagCompound stackTag = new NBTTagCompound();
			stack.setTagCompound(stackTag);
			modules = new NBTTagCompound();
			stackTag.setCompoundTag(PowerModule.Constants.nbtPrefix, modules);
		}
		return modules;
	}
}
