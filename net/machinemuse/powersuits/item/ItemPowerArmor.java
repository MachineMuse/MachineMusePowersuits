package net.machinemuse.powersuits.item;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.Config.Items;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemArmor
		implements
		ISpecialArmor,
		IModularItem,
		IItemElectric {
	Config.Items itemType;

	/**
	 * @param id
	 * @param material
	 * @param renderIndex
	 * @param armorType
	 *            0 = head; 1 = torso; 2 = legs; 3 = feet
	 */
	public ItemPowerArmor(int id, EnumArmorMaterial material,
			int renderIndex, int armorType) {
		super(id, material, renderIndex, armorType);
		setTextureFile("/icons.png");
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
		double totalarmor = 0;
		NBTTagCompound props = ItemUtils.getItemModularProperties(armor);

		double physArmor = ItemUtils.getDoubleOrZero(props,
				IModularItem.ARMOR_VALUE);
		double armorDura = ItemUtils.getDoubleOrZero(props,
				IModularItem.ARMOR_DURABILITY);
		if (armorDura > 0) {
			totalarmor += physArmor;
		}

		double elecArmor = ItemUtils.getDoubleOrZero(props,
				IModularItem.PASSIVE_SHIELDING);
		double energy = ItemUtils.getDoubleOrZero(props,
				IModularItem.CURRENT_ENERGY);
		if (energy > 0) {
			totalarmor += elecArmor;
		}

		return (int) totalarmor;
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize how the armor
	 * handles being damaged.
	 */
	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(stack);
		float drain = damage * itemProperties.getFloat("Energy per damage");
		onUse(drain, stack);
	}

	@Override
	public Items getItemType() {
		return itemType;
	}

	@Override
	public List<String> getValidProperties() {
		return Arrays.asList(
				"Max Energy",
				"Energy Flow",
				"Armor Value",
				"Weight",
				"Shield Value",
				"Shield Efficiency",
				"Shock Absorption",
				"Heat Resistance"
				);
	}

	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		double stored = getJoules(itemStack);
		double receivable = ElectricInfo.getJoules(amps, voltage, 1);
		double received = Math.min(receivable,
				getMaxJoules(itemStack) - stored);
		setJoules(stored + received, itemStack);
		return receivable - received;
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(itemStack);

		double joulesAvail = getJoules(itemStack);
		double joulesProvided = Math.min(joulesAvail, joulesNeeded);

		setJoules(joulesAvail - joulesProvided, itemStack);
		return joulesProvided;
	}

	@Override
	public boolean canReceiveElectricity() {
		return true;
	}

	@Override
	public boolean canProduceElectricity() {
		return true;
	}

	@Override
	public double getJoules(Object... data) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(getStackFromData(data));
		return ItemUtils.getDoubleOrZero(itemProperties,
				IModularItem.CURRENT_ENERGY);
	}

	@Override
	public void setJoules(double joules, Object... data) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(getStackFromData(data));
		itemProperties.setDouble(IModularItem.CURRENT_ENERGY, joules);
	}

	@Override
	public double getMaxJoules(Object... data) {
		NBTTagCompound itemProperties = ItemUtils
				.getItemModularProperties(getStackFromData(data));
		return ItemUtils.getDoubleOrZero(itemProperties,
				IModularItem.MAXIMUM_ENERGY);
	}

	@Override
	public double getVoltage() {
		return 120;
	}

	/**
	 * Helper function to deal with UE's use of varargs
	 * 
	 * @param data
	 * @return
	 */
	private ItemStack getStackFromData(Object[] data) {
		if (data[0] instanceof ItemStack) {
			return (ItemStack) data[0];
		} else {
			throw new IllegalArgumentException(
					"MusePowerSuits: Invalid ItemStack");
		}
	}
}
