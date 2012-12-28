package net.machinemuse.powersuits.tinker;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.machinemuse.powersuits.item.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class TinkerAction {
	public String name;
	public List<TinkerRequirement> requirements;
	public List<TinkerEffect> effects;
	public List<ItemStack> costs;
	public MuseIcon icon;
	public String description;
	public boolean[] validSlots;

	public TinkerAction(String name, boolean[] validSlots) {
		this.name = name;
		requirements = new ArrayList();
		effects = new ArrayList();
		costs = new ArrayList();
		this.validSlots = validSlots;
	}

	public boolean validate(EntityPlayer player, ItemStack stack) {
		boolean slot = validForItemType(stack.getItem());
		boolean req = validateRequirements(ItemUtils
				.getItemModularProperties(stack));
		boolean cost = validateCost(player.inventory);

		return slot && req && cost;
	}

	public boolean validForItemType(Item item) {
		if (item instanceof ItemPowerArmorHead) {
			return validSlots[0];
		}
		if (item instanceof ItemPowerArmorTorso) {
			return validSlots[1];
		}
		if (item instanceof ItemPowerArmorLegs) {
			return validSlots[2];
		}
		if (item instanceof ItemPowerArmorFeet) {
			return validSlots[3];
		}
		if (item instanceof ItemPowerTool) {
			return validSlots[4];
		}
		return false;
	}

	public boolean validateRequirements(NBTTagCompound nbt) {
		for (TinkerRequirement requirement : requirements) {
			if (!requirement.test(nbt)) {
				return false;
			}
		}
		return true;
	}

	public boolean validateCost(InventoryPlayer inventory) {
		return ItemUtils.hasInInventory(costs, inventory);
	}

	public String getName() {
		return name;
	}

	public List<TinkerRequirement> getRequirements() {
		return requirements;
	}

	public TinkerAction addRequirement(TinkerRequirement requirement) {
		this.requirements.add(requirement);
		return this;
	}

	public List<TinkerEffect> getEffects() {
		return effects;
	}

	public TinkerAction addEffect(TinkerEffect effect) {
		this.effects.add(effect);
		return this;
	}

	public List<ItemStack> getCosts() {
		return costs;
	}

	public TinkerAction addCost(ItemStack cost) {
		this.costs.add(cost);
		return this;
	}

	public MuseIcon getIcon() {
		return icon;
	}

	public TinkerAction setIcon(MuseIcon icon) {
		this.icon = icon;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public TinkerAction setDescription(String description) {
		this.description = description;
		return this;
	}

	public void apply(ItemStack stack) {
		NBTTagCompound tag = ItemUtils.getItemModularProperties(stack);
		for (TinkerEffect effect : this.effects) {
			effect.applyEffect(tag);
		}
	}

}
