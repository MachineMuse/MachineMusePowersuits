package net.machinemuse.powersuits.common.items;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.common.items.modules.PowerModule;

import java.util.List;

public class ToggleablePowerModule extends PowerModule implements IToggleableModule {
	public ToggleablePowerModule(String name, List<IModularItem> validItems, String textureFile, String category) {
		super(name, validItems, textureFile, category);
	}
}