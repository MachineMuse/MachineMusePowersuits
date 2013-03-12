package net.machinemuse.powersuits.item;

import ic2.api.ICustomElectricItem;
import icbm.api.explosion.IEMPItem;
import icbm.api.explosion.IExplosive;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.ElectricItemUtils;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.Config.Items;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.network.packets.MusePacketPlasmaBolt;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import thermalexpansion.api.core.IChargeableItem;
import universalelectricity.core.electricity.ElectricInfo;
import universalelectricity.core.electricity.ElectricInfo.ElectricUnit;
import universalelectricity.core.electricity.ElectricityPack;
import universalelectricity.core.implement.IConductor;
import universalelectricity.core.implement.IItemElectric;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Describes the modular power tool.
 * 
 * @author MachineMuse
 */
public class ItemPowerTool extends ItemTool
		implements
		IModularItem,
		IItemElectric, // Universal Electricity
		ICustomElectricItem, // Industrial Craft 2
		IEMPItem, // for ICBM EMP interfacing
		IChargeableItem // for Thermal Expansion
{
	public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeSteel);
	public static final ItemStack ironAxe = new ItemStack(Item.axeSteel);
	public static final ItemStack ironShovel = new ItemStack(Item.shovelSteel);
	public static final ItemStack diamondPick = new ItemStack(Item.pickaxeDiamond);

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 */
	public ItemPowerTool() {
		super( // ID
				Config.getAssignedItemID(Config.Items.PowerTool),
				// Damage bonus, added to the material's damage
				0,
				// Tool material, can be changed if necessary
				EnumToolMaterial.EMERALD,
				// not important since it's private and we will override the
				// getter
				new Block[0] // Block[] BlocksEffectiveAgainst
		);
		setMaxStackSize(1);
		setMaxDamage(0);
		this.damageVsEntity = 1;
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(59);
		setTextureFile(MuseIcon.WC_ICON_PATH);
		setItemName(Config.Items.PowerTool.idName);
		LanguageRegistry.addName(this, Config.Items.PowerTool.englishName);
	}

	@Override
	public Items getItemType() {
		return Config.Items.PowerTool;
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base,
	 * (Quality+1)*2 if correct blocktype, 1.5F if sword
	 */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block) {
		return getStrVsBlock(stack, block, 0);
	}

	public static Colour getColorFromItemStack(ItemStack stack) {
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

	public static double clampDouble(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	public static boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
		if (player != null) {
			double energy = ElectricItemUtils.getPlayerEnergy(player);
			if (useIronPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION)) {
				return true;
			} else if (useIronShovel(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.SHOVEL_ENERGY_CONSUMPTION)) {
				return true;
			} else if (useIronAxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.AXE_ENERGY_CONSUMPTION)) {
				return true;
			} else if (useDiamondPickaxe(stack, block, meta)
					&& energy > ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION)) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	/** FORGE: Overridden to allow custom tool effectiveness */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		return 1;
	}

	/**
	 * Current implementations of this method in child classes do not use the
	 * entry argument beside stack. They just raise the damage on the stack.
	 */
	@Override
	public boolean hitEntity(ItemStack stack, EntityLiving entityBeingHit, EntityLiving entityDoingHitting) {
		if (entityDoingHitting instanceof EntityPlayer && MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_MELEE_ASSIST)) {
			EntityPlayer player = (EntityPlayer) entityDoingHitting;
			double drain = ModuleManager.computeModularProperty(stack, MuseCommonStrings.PUNCH_ENERGY);
			if (ElectricItemUtils.getPlayerEnergy(player) > drain) {
				ElectricItemUtils.drainPlayerEnergy(player, drain);
				double damage = ModuleManager.computeModularProperty(stack, MuseCommonStrings.PUNCH_DAMAGE);
				double knockback = ModuleManager.computeModularProperty(stack, MuseCommonStrings.PUNCH_KNOCKBACK);
				DamageSource damageSource = DamageSource.causePlayerDamage(player);
				if (entityBeingHit.attackEntityFrom(damageSource, (int) damage)) {
					Vec3 lookVec = player.getLookVec();
					entityBeingHit.addVelocity(lookVec.xCoord * knockback, Math.abs(lookVec.yCoord + 0.2f) * knockback, lookVec.zCoord * knockback);
				}
			}
		}
		return true;
	}

	/**
	 * Called when a block is destroyed using this tool.
	 */
	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world,
			int blockID, int x, int y, int z,
			EntityLiving entity) {
		double drain = 1;
		Block block = Block.blocksList[blockID];
		int meta = 0;
		if (useIronPickaxe(stack, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION);
		} else if (useIronShovel(stack, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, MuseCommonStrings.SHOVEL_ENERGY_CONSUMPTION);
		} else if (useIronAxe(stack, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, MuseCommonStrings.AXE_ENERGY_CONSUMPTION);
		} else if (useDiamondPickaxe(stack, block, meta)) {
			drain = ModuleManager.computeModularProperty(stack, MuseCommonStrings.PICKAXE_ENERGY_CONSUMPTION);
		} else {
			drain = 0;
		}

		if (drain > 0 && entity.isInsideOfMaterial(Material.water)
				&& MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_AQUA_AFFINITY)) {
			drain += ModuleManager.computeModularProperty(stack, MuseCommonStrings.AQUA_AFFINITY_ENERGY_CONSUMPTION);
		}
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			ElectricItemUtils.drainPlayerEnergy(player, drain);
		} else {
			onUse(drain, stack);
		}
		return true;
	}

	public static boolean useIronPickaxe(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_PICKAXE)) {
			if (ForgeHooks.isToolEffective(ironPickaxe, block, meta)) {
				return true;
			} else if ((!ForgeHooks.isToolEffective(diamondPick, block, meta)) && (block.blockMaterial == Material.iron
					|| block.blockMaterial == Material.anvil
					|| block.blockMaterial == Material.rock)) {
				return true;
			}
		}
		return false;
	}

	public static boolean useIronShovel(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_SHOVEL)) {
			if (ForgeHooks.isToolEffective(ironShovel, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.snow) {
				return true;
			}
		}
		return false;
	}

	public static boolean useIronAxe(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_AXE)) {
			if (ForgeHooks.isToolEffective(ironAxe, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.wood
					|| block.blockMaterial == Material.plants
					|| block.blockMaterial == Material.vine) {
				return true;
			}
		}
		return false;
	}

	public static boolean useDiamondPickaxe(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_DIAMOND_PICK_UPGRADE)) {
			if (ForgeHooks.isToolEffective(diamondPick, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.iron
					|| block.blockMaterial == Material.anvil
					|| block.blockMaterial == Material.rock) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the damage against a given entity.
	 */
	@Override
	public int getDamageVsEntity(Entity par1Entity) {
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * Returns True is the item is renderer in full 3D when hold.
	 */
	public boolean isFull3D() {
		return true;
	}

	/**
	 * Return the enchantability factor of the item. In this case, 0. Might add
	 * an enchantability module later :P
	 */
	@Override
	public int getItemEnchantability() {
		return 0;
	}

	/**
	 * Return the name for this tool's material.
	 */
	@Override
	public String getToolMaterialName() {
		return this.toolMaterial.toString();
	}

	/**
	 * Return whether this item is repairable in an anvil.
	 */
	@Override
	public boolean getIsRepairable(ItemStack par1ItemStack,
			ItemStack par2ItemStack) {
		return false;
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
		info.add(formatInfo("Energy Storage", getMaxJoules(stack)) + "J");
		info.add(formatInfo("Weight", MuseCommonStrings.getTotalWeight(stack)) + "g");
		return info;
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

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack)
	{
		return 72000;
	}

	/**
	 * What happens when the duration runs out
	 */
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer)
	{
		return par1ItemStack;
	}

	/**
	 * Called when the right click button is pressed
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player)
	{
		for (IRightClickModule module : ModuleManager.getRightClickModules()) {
			if (module.isValidForItem(itemStack, player) && MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
				((IRightClickModule) module).onRightClick(player, world, itemStack);
			}
		}
		return itemStack;
	}

	/**
	 * returns the action that specifies what animation to play when the items
	 * is being used
	 */
	public EnumAction getItemUseAction(ItemStack stack)
	{
		return EnumAction.bow;
	}

	/**
	 * Called when the right click button is released
	 */
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4)
	{
		int chargeTicks = Math.max(this.getMaxItemUseDuration(itemStack) - par4, 10);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
		{
			double energyConsumption = ModuleManager.computeModularProperty(itemStack, MuseCommonStrings.PLASMA_CANNON_ENERGY_PER_TICK) * chargeTicks;
			if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
				ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
				double explosiveness = ModuleManager.computeModularProperty(itemStack, MuseCommonStrings.PLASMA_CANNON_EXPLOSIVENESS);
				double damagingness = ModuleManager.computeModularProperty(itemStack, MuseCommonStrings.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE);

				EntityPlasmaBolt plasmaBolt = new EntityPlasmaBolt(world, player, explosiveness, damagingness, chargeTicks);
				world.spawnEntityInWorld(plasmaBolt);
				MusePacketPlasmaBolt packet = new MusePacketPlasmaBolt((Player) player, plasmaBolt.entityId,
						plasmaBolt.size);
				PacketDispatcher.sendPacketToAllPlayers(packet.getPacket250());
			}
		}
	}

	// /////////////////////////////////////////// //
	// --- UNIVERSAL ELECTRICITY COMPATABILITY --- //
	// /////////////////////////////////////////// //
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

	public static MuseIcon getCurrentIconFor(ItemStack itemStack) {
		return MuseIcon.POWERTOOL;
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World worldObj, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {

		if (ModCompatability.isBasicComponentsLoaded() && MuseItemUtils.itemHasActiveModule(itemStack, MuseCommonStrings.MODULE_MULTIMETER)) {
			if (!worldObj.isRemote) {
				TileEntity tileEntity = worldObj.getBlockTileEntity(x, y, z);
				if (tileEntity instanceof IConductor) {
					IConductor wireTile = (IConductor) tileEntity;
					ElectricityPack getProduced = wireTile.getNetwork().getProduced();
					player.addChatMessage("Reading: " + ElectricInfo.getDisplay(getProduced.amperes, ElectricUnit.AMPERE) + ", "
							+ ElectricInfo.getDisplay(getProduced.voltage, ElectricUnit.VOLTAGE) + ", "
							+ ElectricInfo.getDisplay(getProduced.getWatts() * 20, ElectricUnit.WATT));
					return true;
				}
			}
		}

		return false;
	}

	// //////////// //
	// --- ICBM --- //
	// //////////// //
	@Override
	public void onEMP(ItemStack itemStack, Entity entity, IExplosive empExplosive) {
		ElectricItemUtils.onEMP(itemStack, entity, empExplosive);
	}

	// ///////////////////////// //
	// --- Thermal Expansion --- //
	// ///////////////////////// //

	/**
	 * Adds energy to an item. Returns the quantity of energy that was accepted.
	 * This should always return 0 if the item cannot be externally charged.
	 * 
	 * @param theItem
	 *            ItemStack to be charged.
	 * @param energy
	 *            Maximum amount of energy to be sent into the item.
	 * @param doReceive
	 *            If false, the charge will only be simulated.
	 * @return Amount of energy that was accepted by the item.
	 */
	public float receiveEnergy(ItemStack stack, float energy, boolean doReceive) {
		double offeredJoules = energy * ModCompatability.getBCRatio();
		double missingJoules = ElectricItemUtils.getMaxJoules(stack) - ElectricItemUtils.getJoules(stack);
		double transferredJoules = Math.min(offeredJoules, missingJoules);
		ElectricItemUtils.charge(transferredJoules, stack);
		return (float) (transferredJoules / ModCompatability.getBCRatio());
	}

	/**
	 * Removes energy from an item. Returns the quantity of energy that was
	 * removed. This should always return 0 if the item cannot be externally
	 * discharged.
	 * 
	 * @param theItem
	 *            ItemStack to be discharged.
	 * @param energy
	 *            Maximum amount of energy to be removed from the item.
	 * @param doTransfer
	 *            If false, the discharge will only be simulated.
	 * @return Amount of energy that was removed from the item.
	 */
	public float transferEnergy(ItemStack stack, float energy, boolean doTransfer) {
		double requestedJoules = energy * ModCompatability.getBCRatio();
		double availableJoules = ElectricItemUtils.getJoules(stack);
		double transferredJoules = Math.min(requestedJoules, availableJoules);
		ElectricItemUtils.discharge(transferredJoules, stack);
		return (float) (transferredJoules / ModCompatability.getBCRatio());
	}

	/**
	 * Get the amount of energy currently stored in the item.
	 */
	public float getEnergyStored(ItemStack stack) {
		return (float) (ModCompatability.getBCRatio() * ElectricItemUtils.getJoules(stack));
	}

	/**
	 * Get the max amount of energy that can be stored in the item.
	 */
	public float getMaxEnergyStored(ItemStack stack) {
		return (float) (ModCompatability.getBCRatio() * ElectricItemUtils.getMaxJoules(stack));
	}

}
