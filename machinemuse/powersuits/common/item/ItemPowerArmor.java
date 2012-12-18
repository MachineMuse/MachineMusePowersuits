package machinemuse.powersuits.common.item;

import machinemuse.powersuits.common.CommonProxy;
import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.augmentation.AugmentationTypes;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.common.registry.LanguageRegistry;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public class ItemPowerArmor extends ItemArmor implements ISpecialArmor,
		IModularItem {
	AugmentationTypes[] validAugTypes;

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 * 
	 * @param item
	 */
	public ItemPowerArmor(Config.Items item) {
		super(Config.getAssignedItemID(item), // itemID
				EnumArmorMaterial.IRON, // Material
				item.iconIndex, // Texture index
				item.ordinal()); // armor type.
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(item.iconIndex);
		setItemName(item.idName);
		LanguageRegistry.addName(this, item.englishName);
	}

	/**
	 * Returns the texture file for this tool. Probably won't be used since we
	 * are using a custom renderer.
	 */
	public String getTextureFile() {
		// TODO: Delete this probably
		return CommonProxy.ITEMS_PNG;
	}

	/**
	 * Inherited from ISpecialArmor, allows significant customization of damage
	 * calculations.
	 */
	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor,
			DamageSource source, double damage, int slot) {
		// Order in which this armor is assessed for damage. Higher(?) priority
		// items take damage first, and if none spills over, the other items
		// take no damage.
		int priority = 1;

		// How much of incoming damage is absorbed by this armor piece.
		// 1.0 = absorbs all damage
		// 0.5 = 50% damage to item, 50% damage carried over
		double absorbRatio = 0.2;

		// Maximum damage absorbed by this piece. Actual damage to this item
		// will be clamped between (damage * absorbRatio) and (absorbMax). Note
		// that a player has 20 hp (1hp = 1 half-heart)
		int absorbMax = 4;

		return new ArmorProperties(priority, absorbRatio,
				absorbMax);
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize the calculations for
	 * how much armor will display on the player's HUD.
	 */
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return 4;
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize how the armor
	 * handles being damaged.
	 */
	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		// Damage the armor's durability
	}

	/**
	 * For IModularItem's aug-list functionality.
	 * 
	 * @param types
	 */
	public void setValidAugTypes(AugmentationTypes[] types) {
		validAugTypes = types;
	}

	/**
	 * Inherited from IModularItem, returns a (potentially sparse) array of
	 * valid augmentations for this item.
	 */
	@Override
	public AugmentationTypes[] getValidAugTypes() {
		return validAugTypes;
	}
}
