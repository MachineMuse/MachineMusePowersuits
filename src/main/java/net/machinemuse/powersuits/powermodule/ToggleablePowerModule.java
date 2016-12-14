package net.machinemuse.powersuits.powermodule;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;

import java.util.List;

public class ToggleablePowerModule extends PowerModule implements IToggleableModule {
	public ToggleablePowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems, textureFile, category);
	}
}
