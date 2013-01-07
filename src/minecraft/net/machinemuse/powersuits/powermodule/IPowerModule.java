package net.machinemuse.powersuits.powermodule;

import java.util.List;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.general.gui.frame.IGuiFrame;
import net.minecraft.item.ItemStack;

public interface IPowerModule {
	public abstract List<ItemStack> getInstallCost(ItemStack item);

	public abstract List<ItemStack> getSalvageRefund(ItemStack item);

	public abstract MuseIcon getIcon(ItemStack item);

	public abstract String getCategory();

	public abstract boolean validForSlot(int slotnumber);

	public abstract IGuiFrame getTinkerFrame(ItemStack item);

	public abstract List<String> getTooltip();
}
