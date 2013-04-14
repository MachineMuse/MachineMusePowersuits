package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.powersuits.client.render.ArmorBootsModel;
import net.machinemuse.powersuits.client.render.ArmorModel;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.powermodule.misc.CosmeticGlowModule;
import net.machinemuse.powersuits.powermodule.misc.HazmatModule;
import net.machinemuse.powersuits.powermodule.misc.TintModule;
import net.machinemuse.powersuits.powermodule.misc.TransparentArmorModule;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import atomicscience.api.IAntiPoisonArmor;
import atomicscience.api.poison.Poison;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Describes the 4 different modular armor pieces - head, torso, legs, feet.
 * 
 * @author MachineMuse
 */
public abstract class ItemPowerArmor extends ItemElectricArmor
		implements
		ISpecialArmor,
		IAntiPoisonArmor,
		IModularItem {

	/**
	 * @param id
	 * @param material
	 * @param renderIndex
	 * @param armorType
	 *            0 = head; 1 = torso; 2 = legs; 3 = feet
	 */
	public ItemPowerArmor(int id, int renderIndex, int armorType) {
		super(id, EnumArmorMaterial.CLOTH, renderIndex, armorType);
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
	}

	@Override
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, int layer) {

		// if (itemstack != null) {
		// NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(itemstack);
		// // MinecraftForgeClient.getRenderPass()? nope
		// if (itemTag.hasKey("didColour")) {
		//
		// itemTag.removeTag("didColour");
		return Config.BLANK_ARMOR_MODEL_PATH;
		// } else {
		// if (MuseItemUtils.itemHasActiveModule(itemstack,
		// TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
		// return Config.BLANK_ARMOR_MODEL_PATH;
		// } else if (itemstack.getItem() instanceof ItemPowerArmorLeggings) {
		// if (MuseItemUtils.itemHasModule(itemstack,
		// CitizenJoeStyle.CITIZEN_JOE_STYLE)) {
		// return Config.CITIZENJOE_ARMORPANTS_PATH;
		// }
		//
		// return Config.SEBK_ARMORPANTS_PATH;
		// } else {
		// if (MuseItemUtils.itemHasModule(itemstack,
		// CitizenJoeStyle.CITIZEN_JOE_STYLE)) {
		// return Config.CITIZENJOE_ARMOR_PATH;
		// }
		// return Config.SEBK_ARMOR_PATH;
		// }
		// }
		// }
		// return Config.BLANK_ARMOR_MODEL_PATH;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ModelBiped getArmorModel(EntityLiving entityLiving, ItemStack itemstack, int armorSlot) {
		ArmorModel model;
		if (armorSlot == 3) {
			model = ArmorBootsModel.getInstance();
		} else {
			model = ArmorModel.getInstance();
		}
		model.bipedHead.showModel = armorSlot == 0;
		model.bipedHeadwear.showModel = armorSlot == 0;
		model.bipedBody.showModel = armorSlot == 1;
		model.bipedRightArm.showModel = armorSlot == 1;
		model.bipedLeftArm.showModel = armorSlot == 1;
		model.bipedRightLeg.showModel = armorSlot == 2 || armorSlot == 3;
		model.bipedLeftLeg.showModel = armorSlot == 2 || armorSlot == 3;
		if (itemstack != null) {
			if (MuseItemUtils.itemHasActiveModule(itemstack,
					TransparentArmorModule.MODULE_TRANSPARENT_ARMOR)) {
				return null;
			}
			model.normalcolour = this.getColorFromItemStack(itemstack);
			model.glowcolour = this.getGlowFromItemStack(itemstack);

		}
		return model;
	}

	public Colour getColorFromItemStack(ItemStack stack) {
		if (!MuseItemUtils.itemHasActiveModule(stack, TintModule.MODULE_TINT)) {
			return Colour.WHITE;
		}
		double computedred = ModuleManager.computeModularProperty(stack, TintModule.RED_TINT);
		double computedgreen = ModuleManager.computeModularProperty(stack, TintModule.GREEN_TINT);
		double computedblue = ModuleManager.computeModularProperty(stack, TintModule.BLUE_TINT);
		Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 1.0F);
		return colour;
	}

	private Colour getGlowFromItemStack(ItemStack stack) {
		if (!MuseItemUtils.itemHasActiveModule(stack, CosmeticGlowModule.MODULE_GLOW)) {
			return Colour.LIGHTBLUE;
		}
		double computedred = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.RED_GLOW);
		double computedgreen = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.GREEN_GLOW);
		double computedblue = ModuleManager.computeModularProperty(stack, CosmeticGlowModule.BLUE_GLOW);
		Colour colour = new Colour(clampDouble(computedred, 0, 1), clampDouble(computedgreen, 0, 1), clampDouble(computedblue, 0, 1), 0.8);
		return colour;
	}

	/**
	 * Inherited from ISpecialArmor, allows significant customization of damage
	 * calculations.
	 */
	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor, DamageSource source, double damage, int slot) {
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

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack stack, int par2) {
		Colour c = getColorFromItemStack(stack);
		return c.getInt();
	}

	@Override
	public int getColor(ItemStack stack) {
		Colour c = getColorFromItemStack(stack);
		return c.getInt();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses() {
		return false;
	}

	/**
	 * Return whether the specified armor ItemStack has a color.
	 */
	@Override
	public boolean hasColor(ItemStack stack) {
		NBTTagCompound itemTag = MuseItemUtils.getMuseItemTag(stack);
		return MuseItemUtils.tagHasModule(itemTag, TintModule.RED_TINT) || MuseItemUtils.tagHasModule(itemTag, TintModule.GREEN_TINT)
				|| MuseItemUtils.tagHasModule(itemTag, TintModule.BLUE_TINT);
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
		// Make it so each armor piece can only contribute reduction up to the
		// configured amount.
		// Defaults to 6 armor points, or 24% reduction.
		totalArmor = Math.min(Config.getMaximumArmorPerPiece(), totalArmor);
		return totalArmor;
	}

	/**
	 * Inherited from ISpecialArmor, allows us to customize how the armor
	 * handles being damaged.
	 */
	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack, DamageSource source, int damage, int slot) {
		NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
		double enerConsum = ModuleManager.computeModularProperty(stack, MuseCommonStrings.ARMOR_ENERGY_CONSUMPTION);
		double drain = enerConsum * damage;
		if (entity instanceof EntityPlayer) {
			ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entity, drain);
		} else {
			drainEnergyFrom(stack, drain);
		}
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
	 *            enchantments and lore are
	 *            appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in
	 *            their settings.
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips);
	}

	public static String formatInfo(String string, double value) {
		return string + '\t' + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
		info.add("Detailed Summary");
		info.add(formatInfo("Armor", getArmorDouble(player, stack)));
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)) + 'J');
		info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + 'g');
		return info;
	}

	@Override
	public boolean isProtectedFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
		return MuseItemUtils.itemHasActiveModule(itemStack, HazmatModule.MODULE_HAZMAT);
	}

	@Override
	public void onProtectFromPoison(ItemStack itemStack, EntityLiving entityLiving, Poison type) {
	}

}
