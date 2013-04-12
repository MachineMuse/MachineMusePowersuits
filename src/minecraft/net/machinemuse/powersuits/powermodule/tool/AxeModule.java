package net.machinemuse.powersuits.powermodule.tool;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IBlockBreakingModule;
import net.machinemuse.api.moduletrigger.IToggleableModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

public class AxeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
	public static final String MODULE_AXE = "Axe";
	public static final ItemStack ironAxe = new ItemStack(Item.axeIron);
	public static final String AXE_ENERGY_CONSUMPTION = "Axe Energy Consumption";
	public static final String AXE_HARVEST_SPEED = "Axe Harvest Speed";
	public static final String AXE_SEARCH_RADIUS = "Axe Search Radius";

	public AxeModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.ingotIron, 3));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
		addBaseProperty(AXE_ENERGY_CONSUMPTION, 50, "J");
		addBaseProperty(AXE_HARVEST_SPEED, 8, "x");
		addTradeoffProperty("Overclock", AXE_ENERGY_CONSUMPTION, 950);
		addTradeoffProperty("Overclock", AXE_HARVEST_SPEED, 22);
		addTradeoffProperty("Radius", AXE_ENERGY_CONSUMPTION, 1000);
		addTradeoffProperty("Radius", AXE_SEARCH_RADIUS, 3);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_TOOL;
	}

	@Override
	public String getName() {
		return MODULE_AXE;
	}

	@Override
	public String getDescription() {
		return "Axes are mostly for chopping trees.";
	}

	@Override
	public String getTextureFile() {
		return "toolaxe";
	}

	@Override
	public boolean canHarvestBlock(ItemStack stack, Block block, int meta, EntityPlayer player) {
		if (ironAxe.canHarvestBlock(block) || ForgeHooks.canToolHarvestBlock(block, meta, ironAxe)) {
			if (ElectricItemUtils.getPlayerEnergy(player) > ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World world, int blockID, int x, int y, int z, EntityPlayer player) {
		if (player instanceof EntityPlayerMP) {
			EntityPlayerMP playerMP = (EntityPlayerMP) player;
			boolean found = true;
			double radius = ModuleManager.computeModularProperty(stack, AXE_SEARCH_RADIUS);
			int minx = (int) Math.floor(x - radius);
			int maxx = (int) Math.ceil(x + radius);
			int minz = (int) Math.floor(z - radius);
			int maxz = (int) Math.ceil(z + radius);
			int newminx, newmaxx, newminz, newmaxz;
			while (found) {
				y++;
				found = false;
				newminx = (minx + maxx) / 2;
				newmaxx = newminx;
				newminz = (minz + maxz) / 2;
				newmaxz = newminz;

				for (int i = minx; i < maxx; i++) {
					for (int j = minz; j < maxz; j++) {
						int id = world.getBlockId(i, y, j);
						if (canHarvestBlock(stack, Block.blocksList[id], world.getBlockMetadata(i, y, j), player)) {
							found = true;
							newminx = (int) Math.min(newminx, i - radius);
							newmaxx = (int) Math.max(newmaxx, i + radius);
							newminz = (int) Math.min(newminz, j - radius);
							newmaxz = (int) Math.max(newmaxz, j + radius);
							playerMP.theItemInWorldManager.tryHarvestBlock(i, y, j);
							ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
						}
					}
				}
				minx = newminx;
				maxx = newmaxx;
				minz = newminz;
				maxz = newmaxz;
			}
			ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
			return true;
		}
		return false;
	}

	@Override
	public void handleBreakSpeed(BreakSpeed event) {
		event.newSpeed *= ModuleManager.computeModularProperty(event.entityPlayer.getCurrentEquippedItem(), AXE_HARVEST_SPEED);
	}
}
