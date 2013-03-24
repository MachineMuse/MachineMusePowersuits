package net.machinemuse.powersuits.powermodule.tool;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.api.electricity.ElectricItemUtils;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event.Result;
import net.minecraftforge.event.entity.player.UseHoeEvent;

public class HoeModule extends PowerModuleBase implements IPowerModule, IRightClickModule {
	public static final String HOE_ENERGY_CONSUMPTION = "Hoe Energy Consumption";

	public HoeModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(new ItemStack(Item.ingotIron, 2));
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));

		addBaseProperty(HOE_ENERGY_CONSUMPTION, 50);
	}

	@Override
	public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
	}

	@Override
	public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
		if (!player.canPlayerEdit(x, y, z, side, itemStack))
		{
			return;
		}
		else
		{
			UseHoeEvent event = new UseHoeEvent(player, itemStack, world, x, y, z);
			if (MinecraftForge.EVENT_BUS.post(event))
			{
				return;
			}

			if (event.getResult() == Result.ALLOW)
			{
				ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, HOE_ENERGY_CONSUMPTION));
				return;
			}

			int i1 = world.getBlockId(x, y, z);
			int j1 = world.getBlockId(x, y + 1, z);

			if ((side == 0 || j1 != 0 || i1 != Block.grass.blockID) && i1 != Block.dirt.blockID)
			{
				return;
			}
			else
			{
				Block block = Block.tilledField;
				world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
						block.stepSound.getStepSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);

				if (world.isRemote)
				{
					return;
				}
				else
				{
					world.setBlock(x, y, z, block.blockID);
					ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, HOE_ENERGY_CONSUMPTION));
					return;
				}
			}
		}
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
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_TOOL;
	}

	@Override
	public String getName() {
		return "Rototiller";
	}

	@Override
	public String getDescription() {
		return "An automated tilling addon to make it easy to till large swaths of land at once.";
	}

	@Override
	public String getTextureFile() {
		return null;
	}

	@Override
	public Icon getIcon(ItemStack item) {
		return Item.hoeGold.getIconFromDamage(0);
	}
}
