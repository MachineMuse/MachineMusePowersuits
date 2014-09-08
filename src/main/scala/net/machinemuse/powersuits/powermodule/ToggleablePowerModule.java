package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;

public class ToggleablePowerModule extends PowerModule implements IToggleableModule {

	public ToggleablePowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems, textureFile, category);
	}

}
