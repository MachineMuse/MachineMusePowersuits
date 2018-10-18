package net.machinemuse.powersuits.powermodule;

import net.machinemuse.api.IModularItem;

import java.util.List;

public class PowerModule extends PowerModuleBase {
	protected String name;
	protected String description;
	protected String category;
	protected String textureFile;

	public PowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems);
		this.name = name;
		this.category = category;
		this.textureFile = textureFile;
	}

	@Override
	public String getDataName() {
		return name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getCategory() {
		return category;
	}

	public static String getUnit(String propertyName) {
		String unit = units.get(propertyName);
		return unit == null ? "" : unit;
	}

	public PowerModule setDescription(String description) {
		this.description = description;
		return this;
	}

	public PowerModule setCategory(String category) {
		this.category = category;
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

	@Override
	public String getTextureFile() {
		return textureFile;
	}
}
