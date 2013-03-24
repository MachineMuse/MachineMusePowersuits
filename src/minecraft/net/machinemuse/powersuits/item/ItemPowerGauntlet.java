package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.MuseStringUtils;
import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.entity.EntityPlasmaBolt;
import net.machinemuse.powersuits.network.packets.MusePacketPlasmaBolt;
import net.machinemuse.powersuits.powermodule.modules.PlasmaCannonModule;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
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
public class ItemPowerGauntlet extends ItemElectricTool implements IModularItem {
	public static int assignedItemID;

	public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeSteel);
	public static final ItemStack ironAxe = new ItemStack(Item.axeSteel);
	public static final ItemStack ironShovel = new ItemStack(Item.shovelSteel);
	public static final ItemStack diamondPick = new ItemStack(Item.pickaxeDiamond);

	/**
	 * Constructor. Takes information from the Config.Items enum.
	 */
	public ItemPowerGauntlet() {
		super( // ID
				assignedItemID,
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
		LanguageRegistry.addName(this, "Power Gauntlet");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateIcons(IconRegister iconRegister) {
		MuseIcon.POWERTOOL.register(iconRegister);
		iconIndex = MuseIcon.POWERTOOL.getIconRegistration();
	}

	public static MuseIcon getCurrentIconFor(ItemStack itemStack) {
		return MuseIcon.POWERTOOL;
	}

	public static Colour getColorFromItemStack(ItemStack stack) {
		double computedred = ModuleManager.computeModularProperty(stack, MuseCommonStrings.RED_TINT);
		double computedgreen = ModuleManager.computeModularProperty(stack, MuseCommonStrings.GREEN_TINT);
		double computedblue = ModuleManager.computeModularProperty(stack, MuseCommonStrings.BLUE_TINT);
		Colour colour = new Colour(clampDouble(1 + computedred - (computedblue + computedgreen), 0, 1), clampDouble(1 + computedgreen
				- (computedblue + computedred), 0, 1), clampDouble(1 + computedblue - (computedred + computedgreen), 0, 1), 1.0F);
		return colour;
	}

	public static double clampDouble(double value, double min, double max) {
		if (value < min)
			return min;
		if (value > max)
			return max;
		return value;
	}

	/**
	 * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if sword
	 */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block) {
		return getStrVsBlock(stack, block, 0);
	}

	/** FORGE: Overridden to allow custom tool effectiveness */
	@Override
	public float getStrVsBlock(ItemStack stack, Block block, int meta) {
		return 1;
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

	/**
	 * Current implementations of this method in child classes do not use the entry argument beside stack. They just raise the damage on the stack.
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
	public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityLiving entity) {
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
			// onUse(drain, stack);
		}
		return true;
	}

	public static boolean useIronPickaxe(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_PICKAXE)) {
			if (ForgeHooks.isToolEffective(ironPickaxe, block, meta)) {
				return true;
			} else if ((!ForgeHooks.isToolEffective(diamondPick, block, meta))
					&& (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock)) {
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
			} else if (block.blockMaterial == Material.wood || block.blockMaterial == Material.plants || block.blockMaterial == Material.vine) {
				return true;
			}
		}
		return false;
	}

	public static boolean useDiamondPickaxe(ItemStack stack, Block block, int meta) {
		if (MuseItemUtils.itemHasActiveModule(stack, MuseCommonStrings.MODULE_DIAMOND_PICK_UPGRADE)) {
			if (ForgeHooks.isToolEffective(diamondPick, block, meta)) {
				return true;
			} else if (block.blockMaterial == Material.iron || block.blockMaterial == Material.anvil || block.blockMaterial == Material.rock) {
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
	 * Return the enchantability factor of the item. In this case, 0. Might add an enchantability module later :P
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
	public boolean getIsRepairable(ItemStack par1ItemStack, ItemStack par2ItemStack) {
		return false;
	}

	public static String formatInfo(String string, double value) {
		return string + "\t" + MuseStringUtils.formatNumberShort(value);
	}

	@Override
	public List<String> getLongInfo(EntityPlayer player, ItemStack stack) {
		List<String> info = new ArrayList();
		NBTTagCompound itemProperties = MuseItemUtils.getMuseItemTag(stack);
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
	 *            A list of strings containing the existing tooltip. When passed, it will just contain the name of the item; enchantments and lore are
	 *            appended afterwards.
	 * @param advancedToolTips
	 *            Whether or not the player has 'advanced tooltips' turned on in their settings.
	 */
	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List currentTipList, boolean advancedToolTips) {
		MuseCommonStrings.addInformation(stack, player, currentTipList, advancedToolTips);
	}

	/**
	 * How long it takes to use or consume an item
	 */
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	/**
	 * What happens when the duration runs out
	 */
	public ItemStack onFoodEaten(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		return par1ItemStack;
	}

	/**
	 * Called when the right click button is pressed
	 */
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
		for (IRightClickModule module : ModuleManager.getRightClickModules()) {
			if (module.isValidForItem(itemStack, player) && MuseItemUtils.itemHasActiveModule(itemStack, module.getName())) {
				((IRightClickModule) module).onRightClick(player, world, itemStack);
			}
		}
		return itemStack;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.bow;
	}

	/**
	 * Called when the right click button is released
	 */
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
		int chargeTicks = Math.max(this.getMaxItemUseDuration(itemStack) - par4, 10);

		if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
			double energyConsumption = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_ENERGY_PER_TICK)
					* chargeTicks;
			if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
				ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);
				double explosiveness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_EXPLOSIVENESS);
				double damagingness = ModuleManager.computeModularProperty(itemStack, PlasmaCannonModule.PLASMA_CANNON_DAMAGE_AT_FULL_CHARGE);

				EntityPlasmaBolt plasmaBolt = new EntityPlasmaBolt(world, player, explosiveness, damagingness, chargeTicks);
				world.spawnEntityInWorld(plasmaBolt);
				MusePacketPlasmaBolt packet = new MusePacketPlasmaBolt((Player) player, plasmaBolt.entityId, plasmaBolt.size);
				PacketDispatcher.sendPacketToAllPlayers(packet.getPacket250());
			}
		}
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		String mode = MuseItemUtils.getActiveMode(itemStack);
		IPowerModule module = ModuleManager.getModule(mode);
		if (module instanceof IRightClickModule) {
			((IRightClickModule) module).onItemUseFirst(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
			return false;
		}
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		String mode = MuseItemUtils.getActiveMode(itemStack);
		IPowerModule module = ModuleManager.getModule(mode);
		if (module instanceof IRightClickModule) {
			((IRightClickModule) module).onItemUse(itemStack, player, world, x, y, z, side, hitX, hitY, hitZ);
			return false;
		}
		return false;
	}

}
