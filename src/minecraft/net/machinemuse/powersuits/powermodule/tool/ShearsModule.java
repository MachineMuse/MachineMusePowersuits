package net.machinemuse.powersuits.powermodule.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.MusePlayerUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class ShearsModule extends PowerModuleBase implements IBlockBreakingModule, IRightClickModule {
	public static final ItemStack shears = new ItemStack(Item.shears);
	private static final String SHEARING_ENERGY_CONSUMPTION = "Shearing Energy Consumption";
	private static final String SHEARING_HARVEST_SPEED = "Shearing Harvest Speed";

	public ShearsModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.ingotIron, 2));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
		addBaseProperty(SHEARING_ENERGY_CONSUMPTION, 50, "J");
		addBaseProperty(SHEARING_HARVEST_SPEED, 8, "x");
		addTradeoffProperty("Overclock", SHEARING_ENERGY_CONSUMPTION, 950);
		addTradeoffProperty("Overclock", SHEARING_HARVEST_SPEED, 22);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_TOOL;
	}

	@Override
	public String getName() {
		return "Retractable Blades";
	}

	@Override
	public String getDescription() {
		return "A pair of retractable razor blades for cutting through stringy things.";
	}

	@Override
	public void onRightClick(EntityPlayer playerClicking, World world, ItemStack stack) {
		if (playerClicking.worldObj.isRemote)
		{
			return;
		}
		MovingObjectPosition hitMOP = MusePlayerUtils.raytraceEntities(world, playerClicking, false, 8);

		if (hitMOP != null && hitMOP.entityHit instanceof IShearable) {
			IShearable target = (IShearable) hitMOP.entityHit;
			Entity entity = hitMOP.entityHit;
			if (target.isShearable(stack, entity.worldObj, (int) entity.posX, (int) entity.posY, (int) entity.posZ))
			{
				ArrayList<ItemStack> drops = target.onSheared(stack, entity.worldObj, (int) entity.posX, (int) entity.posY,
						(int) entity.posZ,
						EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, stack));

				Random rand = new Random();
				for (ItemStack drop : drops)
				{
					EntityItem ent = entity.entityDropItem(drop, 1.0F);
					ent.motionY += rand.nextFloat() * 0.05F;
					ent.motionX += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
					ent.motionZ += (rand.nextFloat() - rand.nextFloat()) * 0.1F;
				}
				ElectricItemUtils.drainPlayerEnergy(playerClicking, ModuleManager.computeModularProperty(stack, SHEARING_ENERGY_CONSUMPTION));
			}
		}
	}

	@Override
	public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
	}

	@Override
	public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
			float hitZ) {
		return false;
	}

	@Override
	public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
	}

	@Override
	public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
		if (shears.canHarvestBlock(block) || ForgeHooks.canToolHarvestBlock(block, meta, shears) || shears.getStrVsBlock(block) > 1) {
			if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, SHEARING_ENERGY_CONSUMPTION)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack itemstack, World world, int blockID, int x, int y, int z, EntityPlayer player) {
		if (player.worldObj.isRemote)
		{
			return false;
		}
		int id = player.worldObj.getBlockId(x, y, z);
		if (Block.blocksList[id] instanceof IShearable)
		{
			IShearable target = (IShearable) Block.blocksList[id];
			if (target.isShearable(itemstack, player.worldObj, x, y, z))
			{
				ArrayList<ItemStack> drops = target.onSheared(itemstack, player.worldObj, x, y, z,
						EnchantmentHelper.getEnchantmentLevel(Enchantment.fortune.effectId, itemstack));
				Random rand = new Random();

				for (ItemStack stack : drops)
				{
					float f = 0.7F;
					double d = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d1 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					double d2 = (double) (rand.nextFloat() * f) + (double) (1.0F - f) * 0.5D;
					EntityItem entityitem = new EntityItem(player.worldObj, (double) x + d, (double) y + d1, (double) z + d2, stack);
					entityitem.delayBeforeCanPickup = 10;
					player.worldObj.spawnEntityInWorld(entityitem);
				}

				ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemstack, SHEARING_ENERGY_CONSUMPTION));
				player.addStat(StatList.mineBlockStatArray[id], 1);
			}
		}
		return false;
	}

	@Override
	public void handleBreakSpeed(BreakSpeed event) {
		float defaultEffectiveness = shears.getStrVsBlock(event.block);
		double ourEffectiveness = ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), SHEARING_HARVEST_SPEED);
		event.newSpeed *= Math.max(defaultEffectiveness, ourEffectiveness);

	}

	@Override
	public Icon getIcon(ItemStack item) {
		return shears.getIconIndex();
	}

	@Override
	public String getTextureFile() {
		return null;
	}
}
