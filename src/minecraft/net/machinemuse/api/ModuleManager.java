package net.machinemuse.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.machinemuse.powersuits.common.Config;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModuleManager {

	protected static final Map<String, IPowerModule> moduleMap = new HashMap();
	protected static final List<IPowerModule> moduleList = new ArrayList();

	public static List<IPowerModule> getAllModules() {
		return moduleList;
	}

	public static IPowerModule getModule(String key) {
		return moduleMap.get(key);
	}

	public static void addModule(IPowerModule module) {
		boolean isModuleEnabled = Config.getConfig().get("Modules", module.getName(), true)
				.getBoolean(true);
		if (isModuleEnabled) {
			moduleMap.put(module.getName(), module);
			moduleList.add(module);
		}
	}

	public static double computeModularProperty(ItemStack stack, String propertyName) {
		double propertyValue = 0;
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		for (IPowerModule module : moduleList) {
			if (MuseItemUtils.isModuleActive(itemTag, module.getName())) {
				propertyValue = module.applyPropertyModifiers(itemTag, propertyName, propertyValue);
			}
		}
		return propertyValue;
	}

}
