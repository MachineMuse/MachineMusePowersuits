package net.machinemuse.powersuits.tinker;

import java.util.List;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.machinemuse.powersuits.gui.frame.IGuiFrame;
import net.minecraft.item.ItemStack;

public class GenericModule implements IPowerModule {

	public GenericModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<ItemStack> getInstallCost(ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ItemStack> getSalvageAmount(ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MuseIcon getIcon(ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGuiFrame getTinkerFrame(ItemStack item) {
		// TODO Auto-generated method stub
		return null;
	}

}
