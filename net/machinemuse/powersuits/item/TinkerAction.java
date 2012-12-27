package net.machinemuse.powersuits.item;

import java.util.ArrayList;
import java.util.List;

import net.machinemuse.powersuits.gui.MuseIcon;
import net.minecraft.item.ItemStack;

public class TinkerAction {
	public String name;
	public List<TinkerRequirement> requirements;
	public List<TinkerEffect> effects;
	public List<ItemStack> costs;
	public MuseIcon icon;
	public String description;

	public TinkerAction(String name) {
		this.name = name;
		requirements = new ArrayList();
		effects = new ArrayList();
		costs = new ArrayList();
	}

	public String getName() {
		return name;
	}

	public List<TinkerRequirement> getRequirements() {
		return requirements;
	}

	public void addRequirement(TinkerRequirement requirement) {
		this.requirements.add(requirement);
	}

	public List<TinkerEffect> getEffects() {
		return effects;
	}

	public void addEffect(TinkerEffect effect) {
		this.effects.add(effect);
	}

	public List<ItemStack> getCosts() {
		return costs;
	}

	public void addCosts(ItemStack cost) {
		this.costs.add(cost);
	}

	public MuseIcon getIcon() {
		return icon;
	}

	public void setIcon(MuseIcon icon) {
		this.icon = icon;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
