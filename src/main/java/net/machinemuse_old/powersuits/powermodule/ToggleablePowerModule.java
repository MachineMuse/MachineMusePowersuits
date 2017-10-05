package net.machinemuse_old.powersuits.powermodule;

import net.machinemuse_old.api.IModularItem;
import net.machinemuse_old.api.moduletrigger.IToggleableModule;

import java.util.List;

public class ToggleablePowerModule extends PowerModule implements IToggleableModule {
	public ToggleablePowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems, textureFile, category);
	}
}