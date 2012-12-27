package net.machinemuse.powersuits.item;

import java.util.List;

import net.machinemuse.powersuits.common.Config;


/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author MachineMuse
 */
public interface IModularItem {

	public Config.Items getItemType();

	public List<String> getValidProperties();

}
