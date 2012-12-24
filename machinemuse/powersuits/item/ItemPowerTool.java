package machinemuse.powersuits.item;

import java.util.List;

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
	protected List<String> validAugTypes;

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 */
	public ItemPowerTool() {
		super(Config.getAssignedItemID(Config.Items.PowerTool));
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(Config.Items.PowerTool.iconIndex);
		setItemName(Config.Items.PowerTool.idName);
		LanguageRegistry.addName(this, Config.Items.PowerTool.englishName);
	}

	@Override
	public Items getItemType() {
		return Config.Items.PowerTool;
	}

}
