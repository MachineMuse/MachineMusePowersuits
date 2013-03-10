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
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		double propertyValue = 0;
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		for (IPowerModule module : moduleList) {
			if (MuseItemUtils.isModuleOnline(itemTag, module.getName())) {
				propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
			}
		}
		return propertyValue;
	}

}
