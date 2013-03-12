package net.machinemuse.powersuits.powermodule.modules;

import java.util.List;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IRightClickModule;
import net.machinemuse.api.MuseCommonStrings;
import net.machinemuse.api.MuseItemUtils;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class InPlaceAssemblerModule extends PowerModuleBase implements IRightClickModule {
	public InPlaceAssemblerModule(List<IModularItem> validItems) {
		super(validItems);
		addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
		addInstallCost(new ItemStack(Block.workbench, 1));
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		return new MuseIcon(Block.workbench.getTextureFile(), 60);
	}

	@Override
	public String getCategory() {
		return MuseCommonStrings.CATEGORY_SPECIAL;
	}

	@Override
	public String getName() {
		return MuseCommonStrings.MODULE_PORTABLE_CRAFTING;
	}

	@Override
	public String getDescription() {
		return "A larger crafting grid, on the go.";
	}

	@Override
	public void onRightClick(EntityPlayer player, World world, ItemStack item) {
		player.openGui(ModularPowersuits.instance, 2, world, 0, 0, 0);
	}

}
