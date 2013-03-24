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
	public static final ItemStack ironAxe = new ItemStack(Item.axeSteel);
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
			while (found) {
				y++;
				found = false;
				for (int i = (int) Math.floor(-radius); i < radius; i++) {
					for (int j = (int) Math.floor(-radius); j < radius; j++) {
						int id = world.getBlockId(x + i, y, z + j);
						if (canHarvestBlock(stack, Block.blocksList[id], world.getBlockMetadata(x + i, y, z + j), player)) {
							found = true;
							playerMP.theItemInWorldManager.tryHarvestBlock(x + i, y, z + j);
							ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(stack, AXE_ENERGY_CONSUMPTION));
						}
					}
				}
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
