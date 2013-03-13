package net.machinemuse.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleManager {

	protected static final Map<String, IPowerModule> moduleMap = new HashMap();
	protected static final List<IPowerModule> moduleList = new ArrayList();
	protected static final List<IPlayerTickModule> playerTickModules = new ArrayList();
	protected static final List<IRightClickModule> rightClickModules = new ArrayList();
	protected static final List<IToggleableModule> toggleableModules = new ArrayList();

	public static List<IPowerModule> getAllModules() {
		return moduleList;
	}

	public static List<IPlayerTickModule> getPlayerTickModules() {
		return playerTickModules;
	}

	public static IPowerModule getModule(String key) {
		return moduleMap.get(key);
	}

	public static void addModule(IPowerModule module) {

		moduleMap.put(module.getName(), module);
		moduleList.add(module);
		if (module instanceof IPlayerTickModule) {
			playerTickModules.add((IPlayerTickModule) module);
		}
		if (module instanceof IRightClickModule) {
			rightClickModules.add((IRightClickModule) module);
		}
		if (module instanceof IToggleableModule) {
			toggleableModules.add((IToggleableModule) module);
		}
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		double propertyValue = 0;
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		for (IPowerModule module : moduleList) {
			if (MuseItemUtils.itemHasActiveModule(stack, module.getName())) {
				propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
			}
		}
		return propertyValue;
	}

	public static List<IRightClickModule> getRightClickModules() {
		return rightClickModules;
	}

	public static List<IToggleableModule> getToggleableModules() {
		return toggleableModules;
	}
}
