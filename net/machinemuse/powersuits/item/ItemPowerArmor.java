package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.general.MuseStringUtils;
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

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemArmor
		implements
		ISpecialArmor,
		IModularItem {
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
		double absorbRatio = Math.min(0.04 * getArmorDouble(armor), 0.25);

		// Maximum damage absorbed by this piece. Actual damage to this item
		// will be clamped between (damage * absorbRatio) and (absorbMax). Note
		// that a player has 20 hp (1hp = 1 half-heart)
		int absorbMax = 5;

		return new ArmorProperties(priority, absorbRatio,
				absorbMax);
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize the calculations for
	 * how much armor will display on the player's HUD.
	 */
	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		return (int) getArmorDouble(armor);
	}

	public double getArmorDouble(ItemStack stack) {
		double totalarmor = 0;
		NBTTagCompound props = ItemUtils.getMuseItemTag(stack);

		double physArmor = ItemUtils.getDoubleOrZero(props,
				ModularItemCommon.ARMOR_VALUE);
		double armorDura = ItemUtils.getDoubleOrZero(props,
				ModularItemCommon.ARMOR_DURABILITY);
		if (armorDura > 0) {
			totalarmor += physArmor;
		}

		double elecArmor = ItemUtils.getDoubleOrZero(props,
				ModularItemCommon.PASSIVE_SHIELDING);
		double energy = ItemUtils.getDoubleOrZero(props,
				ModularItemCommon.CURRENT_ENERGY);
		if (energy > 0) {
			totalarmor += elecArmor;
		}

		// Make it so each armor piece can only contribute 1/4 of the armor
		// value
		totalarmor = Math.min(6.25, totalarmor);
		return totalarmor;
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize how the armor
	 * handles being damaged.
	 */
	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		NBTTagCompound itemProperties = ItemUtils
				.getMuseItemTag(stack);
		float drain = damage * itemProperties.getFloat("Energy per damage");
		onUse(drain, stack);
	}

	@Override
	public Items getItemType() {
		return itemType;
	}

	/**
	 * Adds information to the item's tooltip when 'getting' it.
	 * 
	 * @param stack
	 *            The itemstack to get the tooltip for
	 * @param player
	 *            The player (client) viewing the tooltip
	 * @param currentTipList
	 *            A list of strings containing the existing tooltip. When
	 *            passed, it will just contain the name of the item;
	 *            enchantments and lore are appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in
	 *            their settings.
	 */
	@Override
	public void addInformation(ItemStack stack,
			EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		ModularItemCommon.addInformation(stack, player, currentTipList,
				advancedToolTips);
	}

	public static String formatInfo(String string, double value) {
		return string + "\t" + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = ItemUtils
				.getMuseItemTag(stack);
		info.add(formatInfo("Armor", getArmorDouble(stack)));
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)));
		return info;
	}

	// //////////////////////////////////////////////
	// --- UNIVERSAL ELECTRICITY COMPATABILITY ---//
	// //////////////////////////////////////////////
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		return ModularItemCommon.onReceive(amps, voltage, itemStack);
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
		return ModularItemCommon.onUse(joulesNeeded, itemStack);
	}

	@Override
	public double getJoules(Object... data) {
		return ModularItemCommon.getJoules(getAsStack(data));
	}

	@Override
	public void setJoules(double joules, Object... data) {
		ModularItemCommon.setJoules(joules, getAsStack(data));
	}

	@Override
	public double getMaxJoules(Object... data) {
		return ModularItemCommon.getMaxJoules(getAsStack(data));
	}

	@Override
	public double getVoltage() {
		return ModularItemCommon.getVoltage();
	}

	@Override
	public boolean canReceiveElectricity() {
		return true;
	}

	@Override
	public boolean canProduceElectricity() {
		return true;
	}

	/**
	 * Helper function to deal with UE's use of varargs
	 */
	private ItemStack getAsStack(Object[] data) {
		if (data[0] instanceof ItemStack) {
			return (ItemStack) data[0];
		} else {
			throw new IllegalArgumentException(
					"MusePowerSuits: Invalid ItemStack passed via UE interface");
		}
	}
}
