package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;

public class RightClickPowerModule extends PowerModule implements IRightClickModule {

	public RightClickPowerModule(String name, List<IModularItem> validItems, MuseIcon icon, String category) {
		super(name, validItems, icon, category);
	}

}
