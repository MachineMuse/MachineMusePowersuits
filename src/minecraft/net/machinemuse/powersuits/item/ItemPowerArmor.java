package net.machinemuse.powersuits.item;

import ic2.api.ICustomElectricItem;
import icbm.api.explosion.IEMPItem;
import icbm.api.explosion.IExplosive;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.Config.Items;
import net.machinemuse.powersuits.common.ModCompatability;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.IArmorTextureProvider;
import net.minecraftforge.common.ISpecialArmor;
import thermalexpansion.api.core.IChargeableItem;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.implement.IItemElectric;
import atomicscience.api.IAntiPoisonArmor;
import atomicscience.api.Poison;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemArmor
		implements
		ISpecialArmor, //
		IModularItem,
		IArmorTextureProvider,
		IItemElectric, // Universal Electricity
		ICustomElectricItem, // Industrial Craft 2
		IEMPItem, // for ICBM EMP interfacing
		IAntiPoisonArmor, // for atomic science hazmat suits
		IChargeableItem // for Thermal Expansion
{
	Config.Items itemType;

	/**
	 * @param id
	 * @param material
	 * @param renderIndex
	 * @param armorType
	 *            0 = head; 1 = torso; 2 = legs; 3 = feet
	 */
	public ItemPowerArmor(int id,
			int renderIndex, int armorType) {
		super(id, EnumArmorMaterial.CLOTH, renderIndex, armorType);
		setTextureFile(MuseIcon.SEBK_ICON_PATH);
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
	}

	@Override
	public String getArmorTextureFile(ItemStack itemstack) {
		if (itemstack != null) {
			NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(itemstack);
			// MinecraftForgeClient.getRenderPass()? nope
			if (itemTag.hasKey("didColour")) {

				itemTag.removeTag("didColour");
				return Config.BLANK_ARMOR_MODEL_PATH;
			} else {
				if (MuseItemUtils.itemHasActiveModule(itemstack, MuseCommonStrings.MODULE_TRANSPARENT_ARMOR)) {
					return Config.BLANK_ARMOR_MODEL_PATH;
				} else if (itemstack.getItem() instanceof ItemPowerArmorLegs) {
					if (MuseItemUtils.itemHasModule(itemstack, MuseCommonStrings.CITIZEN_JOE_STYLE)) {
						return Config.CITIZENJOE_ARMORPANTS_PATH;
					}

					return Config.SEBK_ARMORPANTS_PATH;
				} else {
					if (MuseItemUtils.itemHasModule(itemstack, MuseCommonStrings.CITIZEN_JOE_STYLE)) {
						return Config.CITIZENJOE_ARMOR_PATH;
					}
					return Config.SEBK_ARMOR_PATH;
				}
			}
		}
		return Config.BLANK_ARMOR_MODEL_PATH;
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

		double armorDouble;

		if (player instanceof EntityPlayer) {
			armorDouble = getArmorDouble((EntityPlayer) player, armor);
		} else {
			armorDouble = 2;
		}

		// How much of incoming damage is absorbed by this armor piece.
		// 1.0 = absorbs all damage
		// 0.5 = 50% damage to item, 50% damage carried over
		double absorbRatio = 0.04 * armorDouble;

		// Maximum damage absorbed by this piece. Actual damage to this item
		// will be clamped between (damage * absorbRatio) and (absorbMax). Note
		// that a player has 20 hp (1hp = 1 half-heart)
		int absorbMax = (int) armorDouble * 75; // Not sure why this is
												// necessary but oh well

		return new ArmorProperties(priority, absorbRatio, absorbMax);
	}

	public static double clampDouble(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	public int getItemEnchantability()
	{
		return 0;
	}

	public Colour getColorFromItemStack(ItemStack stack) {
		double computedred = ModuleManager.computeModularProperty(stack, MuseCommonStrings.RED_TINT);
		double computedgreen = ModuleManager.computeModularProperty(stack, MuseCommonStrings.GREEN_TINT);
		double computedblue = ModuleManager.computeModularProperty(stack, MuseCommonStrings.BLUE_TINT);
		Colour colour = new Colour(
				clampDouble(1 + computedred - (computedblue + computedgreen), 0, 1),
				clampDouble(1 + computedgreen - (computedblue + computedred), 0, 1),
				clampDouble(1 + computedblue - (computedred + computedgreen), 0, 1),
				1.0F);
		return colour;
	}

	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int par2)
	{
		Colour c = getColorFromItemStack(stack);
		return c.getInt();
	}

	@Override
	public int getColor(ItemStack stack) {

		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		itemTag.setString("didColour", "yes");
		Colour c = getColorFromItemStack(stack);
		return c.getInt();
	}

	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return false;
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	public boolean hasColor(ItemStack stack)
	{
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		if (MuseItemUtils.tagHasModule(itemTag, MuseCommonStrings.RED_TINT)
				|| MuseItemUtils.tagHasModule(itemTag, MuseCommonStrings.GREEN_TINT)
				|| MuseItemUtils.tagHasModule(itemTag, MuseCommonStrings.BLUE_TINT)) {
			return true;
		} else {
			return false;
		}
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
		double totalArmor = 0;
		NBTTagCompound props = MuseItemUtils.getMuseItemTag(stack);

		double energy = ElectricItemUtils.getPlayerEnergy(player);
		double physArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_PHYSICAL);
		double enerArmor = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_VALUE_ENERGY);
		double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);

		totalArmor += physArmor;

		if (energy > enerConsum) {
			totalArmor += enerArmor;
		}
		// Make it so each armor piece can only contribute reduction up to the configured amount.
		// Defaults to 6 armor points, or 24% reduction.
		totalArmor = Math.min(Config.getMaximumArmorPerPiece(), totalArmor);
		return totalArmor;
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize how the armor
	 * handles being damaged.
	 */
	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
		double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
		double drain = enerConsum * damage;
		if (entity instanceof EntityPlayer) {
			ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
		} else {
			onUse(drain, stack);
		}
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
		MuseCommonStrings.addInformation(stack, player, currentTipList,
				advancedToolTips);
	}

	public static String formatInfo(String string, double value) {
		return string + "\t" + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = MuseItemUtils
				.getMuseItemTag(stack);
		info.add("Detailed Summary");
		info.add(formatInfo("Armor", getArmorDouble(player, stack)));
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)) + "J");
		info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + "g");
		return info;
	}

	// //////////////////////////////////////////////
	// --- UNIVERSAL ELECTRICITY COMPATABILITY ---//
	// //////////////////////////////////////////////
	@Override
	public double onReceive(double amps, double voltage, ItemStack itemStack) {
		double amount = ElectricInfo.getJoules(amps, voltage, 1);
		return ElectricItemUtils.charge(amount, itemStack);
	}

	@Override
	public double onUse(double joulesNeeded, ItemStack itemStack) {
		return ElectricItemUtils.discharge(joulesNeeded, itemStack);
	}

	@Override
	public double getJoules(Object... data) {
		return ElectricItemUtils.getJoules(getAsStack(data));
	}

	@Override
	public void setJoules(double joules, Object... data) {
		ElectricItemUtils.setJoules(joules, getAsStack(data));
	}

	@Override
	public double getMaxJoules(Object... data) {
		return ElectricItemUtils.getMaxJoules(getAsStack(data));
	}

	@Override
	public double getVoltage(Object... data) {
		return ElectricItemUtils.getVoltage(getAsStack(data));
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

	// //////////////////////////////////////// //
	// --- INDUSTRIAL CRAFT 2 COMPATABILITY --- //
	// //////////////////////////////////////// //
	@Override
	public int charge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double joulesProvided = ModCompatability.joulesFromEU(amount);
		double maxJoules = ElectricItemUtils.getMaxJoules(stack);
		if (!ignoreTransferLimit && (joulesProvided > maxJoules / 200.0)) {
			joulesProvided = maxJoules / 200.0;
		}
		double currentJoules = ElectricItemUtils.getJoules(stack);
		double surplus = ElectricItemUtils.charge(joulesProvided, stack);
		if (simulate) {
			ElectricItemUtils.setJoules(currentJoules, stack);
		}

		return ModCompatability.joulesToEU(joulesProvided - surplus);
	}

	@Override
	public int discharge(ItemStack stack, int amount, int tier, boolean ignoreTransferLimit, boolean simulate) {
		double joulesRequested = ModCompatability.joulesFromEU(amount);
		double maxJoules = ElectricItemUtils.getMaxJoules(stack);
		if (!ignoreTransferLimit && (joulesRequested > maxJoules / 200.0)) {
			joulesRequested = maxJoules / 200.0;
		}
		double currentJoules = ElectricItemUtils.getJoules(stack);
		double givenJoules = ElectricItemUtils.discharge(joulesRequested, stack);
		if (simulate) {
			ElectricItemUtils.setJoules(currentJoules, stack);
		}
		return ModCompatability.joulesToEU(givenJoules);
	}

	@Override
	public boolean canUse(ItemStack stack, int amount) {
		double joulesRequested = ModCompatability.joulesFromEU(amount);
		double currentJoules = ElectricItemUtils.getJoules(stack);
		if (currentJoules > joulesRequested) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean canShowChargeToolTip(ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean canProvideEnergy() {
		return true;
	}

	@Override
	public int getChargedItemId() {
		return this.itemID;
	}

	@Override
	public int getEmptyItemId() {
		return this.itemID;
	}

	@Override
	public int getMaxCharge() {
		return 1;
	}

	@Override
	public int getTier() {
		return 1;
	}

	@Override
	public int getTransferLimit() {
		return 0;
	}

	// //////////// //
	// --- ICBM --- //
	// //////////// //
	@Override
	public void onEMP(ItemStack itemStack, Entity entity, IExplosive empExplosive) {
		ElectricItemUtils.onEMP(itemStack, entity, empExplosive);
	}

	// ////////////////////// //
	// --- Atomic Science --- //
	// ////////////////////// //
	@Override
	public boolean isProtectedFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
		return MuseItemUtils.itemHasModule(itemStack, MuseCommonStrings.MODULE_HAZMAT);
	}

	@Override
	public void onProtectFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
	}

	// ///////////////////////// //
	// --- Thermal Expansion --- //
	// ///////////////////////// //
	@Override
	public float receiveEnergy(ItemStack theItem, float energy, boolean doReceive) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float transferEnergy(ItemStack theItem, float energy, boolean doTransfer) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getEnergyStored(ItemStack theItem) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getMaxEnergyStored(ItemStack theItem) {
		// TODO Auto-generated method stub
		return 0;
	}
}
