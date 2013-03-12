package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.powermodule.modules.PowerModuleBase;
import net.minecraft.item.ItemStack;

public class PowerModule extends PowerModuleBase {
	protected String name;
	protected String description;
	protected String category;
	protected MuseIcon icon;

	public PowerModule(String name, List<IModularItem> validItems, MuseIcon icon, String category) {
		super(name, validItems);
		this.name = name;
		this.icon = icon;
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return icon;
	}

	public static String getUnit(String propertyName) {
		if (units.containsKey(propertyName)) {
			return units.get(propertyName);
		} else {
			return "";
		}
	}

	public PowerModule setDescription(String description) {
		this.description = description;
		return this;
	}

	public PowerModule setCategory(String category) {
		this.category = category;
		return this;
	}

	public PowerModule setIcon(MuseIcon icon) {
		this.icon = icon;
		return this;
	}

	public PowerModule setDefaultString(String key, String value) {
		this.defaultTag.setString(key, value);
		return this;
	}

	public PowerModule setDefaultDouble(String key, double value) {
		this.defaultTag.setDouble(key, value);
		return this;
	}

}
