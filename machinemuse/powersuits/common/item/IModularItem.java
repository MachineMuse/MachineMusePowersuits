package machinemuse.powersuits.common.item;

import machinemuse.powersuits.common.augmentation.AugmentationTypes;

/**
 * Interface for ItemPowerArmor and ItemPowerTool to share.
 * 
 * @author Claire
 */
public interface IModularItem {
	AugmentationTypes[] getValidAugTypes();

}
