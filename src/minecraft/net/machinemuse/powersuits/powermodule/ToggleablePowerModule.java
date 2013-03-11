package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IToggleableModule;
import net.machinemuse.general.gui.MuseIcon;

public class ToggleablePowerModule extends PowerModule implements IToggleableModule {

	public ToggleablePowerModule(String name, List<IModularItem> validItems, MuseIcon icon, String category) {
		super(name, validItems, icon, category);
	}

}
