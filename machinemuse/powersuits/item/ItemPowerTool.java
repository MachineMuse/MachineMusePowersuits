package machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.augmentation.AugmentationBattery;
import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.Config.Items;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Describes the modular power tool.
 * 
 * @author MachineMuse
 */
public class ItemPowerTool extends Item implements IModularItem {
	protected List<Augmentation> validAugTypes;

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 */
	public ItemPowerTool() {
		super(Config.getAssignedItemID(Config.Items.PowerTool));
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(Config.Items.PowerTool.iconIndex);
		setItemName(Config.Items.PowerTool.idName);
		setValidAugTypes();
		LanguageRegistry.addName(this, Config.Items.PowerTool.englishName);
	}

	/**
	 * For IModularItem's aug-list functionality.
	 * 
	 * @param types
	 */
	public void setValidAugTypes() {
		validAugTypes = new ArrayList<Augmentation>();
		validAugTypes.add(new AugmentationBattery());
	}

	/**
	 * Inherited from IModularItem, returns a (potentially sparse) array of
	 * valid augmentations for this item.
	 */
	@Override
	public List<Augmentation> getValidAugs() {
		return validAugTypes;
	}

	@Override
	public Items getItemType() {
		return Config.Items.PowerTool;
	}

}
