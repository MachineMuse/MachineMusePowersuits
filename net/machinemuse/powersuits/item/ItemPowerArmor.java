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
		double absorbRatio;
		if (player instanceof EntityPlayer) {
			absorbRatio = 0.04 * getArmorDouble((EntityPlayer) player, armor);
		} else {
			absorbRatio = 0.1;
		}

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
		return (int) getArmorDouble(player, armor);
	}

	public double getArmorDouble(EntityPlayer player, ItemStack stack) {
		double totalarmor = 0;
		NBTTagCompound props = ItemUtils.getMuseItemTag(stack);

		double energy = ItemUtils.getAvailableEnergy(player);

		if (ItemUtils.itemHasModule(stack, ModularCommon.IRON_SHIELDING)
				&& energy > 0) {
			totalarmor += 3;
		}
		if (ItemUtils.itemHasModule(stack, ModularCommon.DIAMOND_SHIELDING)
				&& energy > 0) {
			totalarmor += 5;
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
		ModularCommon.addInformation(stack, player, currentTipList,
				advancedToolTips);
	}

	public static String formatInfo(String string, double value) {
		return string + "\t" + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = ItemUtils
				.getMuseItemTag(stack);
		info.add("Detailed Summary");
		info.add(formatInfo("Armor", getArmorDouble(player, stack)));
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)));
		return info;
	}

	// //////////////////////////////////////////////
	// --- UNIVERSAL ELECTRICITY COMPATABILITY ---//
	// //////////////////////////////////////////////
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		return ModularCommon.onReceive(amps, voltage, itemStack);
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
		return ModularCommon.onUse(joulesNeeded, itemStack);
	}

	@Override
	public double getJoules(Object... data) {
		return ModularCommon.getJoules(getAsStack(data));
	}

	@Override
	public void setJoules(double joules, Object... data) {
		ModularCommon.setJoules(joules, getAsStack(data));
	}

	@Override
	public double getMaxJoules(Object... data) {
		return ModularCommon.getMaxJoules(getAsStack(data));
	}

	@Override
	public double getVoltage() {
		return ModularCommon.getVoltage();
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
