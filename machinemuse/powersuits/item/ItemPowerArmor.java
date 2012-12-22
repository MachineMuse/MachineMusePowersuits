package machinemuse.powersuits.item;

import java.util.List;

import machinemuse.powersuits.augmentation.Augmentation;
import machinemuse.powersuits.common.Config;
import machinemuse.powersuits.common.Config.Items;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemArmor
		implements ISpecialArmor, IModularItem {
	protected List<Augmentation> validAugTypes;

	Config.Items itemType;

	/**
	 * @param par1
	 * @param par2EnumArmorMaterial
	 * @param par3
	 * @param par4
	 */
	public ItemPowerArmor(int par1, EnumArmorMaterial par2EnumArmorMaterial,
			int par3, int par4) {
		super(par1, par2EnumArmorMaterial, par3, par4);
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
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

	}

	@Override
	public Items getItemType() {
		return itemType;
	}

	/**
	 * For IModularItem's aug-list functionality.
	 */
	public void addValidAugType(Augmentation template) {
		validAugTypes.add(template);
	}

	/**
	 * Inherited from IModularItem, returns an array of valid augmentations for
	 * this item.
	 */
	@Override
	public List<Augmentation> getValidAugs() {
		return validAugTypes;
	}

}
