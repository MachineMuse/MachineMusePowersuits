package net.machinemuse.powersuits.tinker;

import java.util.List;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.machinemuse.powersuits.gui.frame.IGuiFrame;
import net.minecraft.item.ItemStack;

public interface IPowerModule {
	public abstract List<ItemStack> getInstallCost(ItemStack item);

	public abstract List<ItemStack> getSalvageAmount(ItemStack item);

	public abstract MuseIcon getIcon(ItemStack item);

	public abstract IGuiFrame getTinkerFrame(ItemStack item);
}
