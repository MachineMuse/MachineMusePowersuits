package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.item.Item;

public class ItemPowerArmorComponent extends Item {
	public List<MuseIcon> icons;
	public List<String> names;

	public ItemPowerArmorComponent(int par1) {
		super(par1);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setCreativeTab(Config.getCreativeTab());

	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	public int getIconFromDamage(int par1)
	{
		return icons.get(par1).getIconIndex();
	}
}
