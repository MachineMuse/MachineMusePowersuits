package machinemuse.powersuits.item;

import java.util.List;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.common.Config;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author Claire
 */
public interface IModularItem {
	public List<Augmentation> getValidAugs();

	public Config.Items getItemType();
}
