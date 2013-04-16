package net.machinemuse.api.electricity;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.item.ItemComponent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ElectricItemUtils {

	public static final String MAXIMUM_ENERGY = "Maximum Energy";
	public static final String CURRENT_ENERGY = "Current Energy";

	private static List<ElectricAdapter> electricItemsEquipped(EntityPlayer player) {
		List<ElectricAdapter> electrics = new ArrayList<ElectricAdapter>();
		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
			ItemStack stack = player.inventory.getStackInSlot(i);
			ElectricAdapter adapter = ElectricAdapter.wrap(stack);
			if (adapter != null) {
				electrics.add(adapter);
			}
		}
		return electrics;
	}

	public static double getPlayerEnergy(EntityPlayer player) {
		double avail = 0;
		for (ElectricAdapter adapter : electricItemsEquipped(player)) {
			avail += adapter.getCurrentEnergy();
		}
		return avail;
	}

	public static double getMaxEnergy(EntityPlayer player) {
		double avail = 0;
		for (ElectricAdapter adapter : electricItemsEquipped(player)) {
			avail += adapter.getMaxEnergy();
		}
		return avail;
	}

	public static void drainPlayerEnergy(EntityPlayer player, double drainAmount) {
		for (ElectricAdapter adapter : electricItemsEquipped(player)) {
			double drained = adapter.drainEnergy(drainAmount);
			drainAmount -= drained;
		}
	}

	public static void givePlayerEnergy(EntityPlayer player, double joulesToGive) {
		for (ElectricAdapter adapter : electricItemsEquipped(player)) {
			double given = adapter.giveEnergy(joulesToGive);
			joulesToGive -= given;
		}
	}

	public static double jouleValueOfComponent(ItemStack stackInCost) {
		if (stackInCost.getItem() instanceof ItemComponent) {
			if (stackInCost.getItemDamage() == ItemComponent.lvcapacitor.getItemDamage()) {
				return 20000 * stackInCost.stackSize;
			}
			if (stackInCost.getItemDamage() == ItemComponent.mvcapacitor.getItemDamage()) {
				return 100000 * stackInCost.stackSize;
			}
			if (stackInCost.getItemDamage() == ItemComponent.hvcapacitor.getItemDamage()) {
				return 750000 * stackInCost.stackSize;
			}
		}
		return 0;
	}

	public static double getPlayerHeat(EntityPlayer player) {
		return 0;
	}

	public static double getMaxHeat(EntityPlayer player) {
		return 1;
	}

}
