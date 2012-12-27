package net.machinemuse.powersuits.gui;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.item.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class StatsFrame extends GuiFrame {
	protected NBTTagCompound properties;
	protected ItemStack stack;
	protected List<String> propertiesToList;

	public StatsFrame(int left, int top, int right, int bottom,
			Colour borderColour, Colour insideColour, ItemStack itemStack) {
		super(left, top, right, bottom, borderColour, insideColour);
		this.stack = itemStack;
		this.properties = ItemUtils.getItemModularProperties(stack);
		this.propertiesToList = ItemUtils.getAsModular(stack.getItem())
				.getValidProperties();
	}

	public void draw() {
		drawBackground();
		int xoffset = 8;
		int yoffset = 8;
		for (int i = 0; i < propertiesToList.size(); i++) {
			String propName = propertiesToList.get(i);
			double propValue = ItemUtils.getDoubleOrZero(properties, propName);
			String propValueString = String.format("%.2f", propValue);
			int strlen = MuseGui.getFontRenderer().getStringWidth(
					propValueString);
			MuseRenderer.drawString(propName, left + xoffset,
					top + yoffset + i * 10, borderColour);
			MuseRenderer.drawString(propValueString, right - xoffset
					- strlen,
					top + yoffset + i * 10, borderColour);

		}

	}
}
