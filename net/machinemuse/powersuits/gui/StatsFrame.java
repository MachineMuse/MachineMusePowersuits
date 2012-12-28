package net.machinemuse.powersuits.gui;

import java.util.Set;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.common.NBTTagAccessor;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.tinker.TinkerAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.lwjgl.opengl.GL11;

public class StatsFrame extends GuiFrame {
	protected NBTTagCompound properties;
	protected ItemStack stack;
	protected Set<String> propertiesToList;
	protected TinkerAction previewAction;

	public StatsFrame(int left, int top, int right, int bottom,
			Colour borderColour, Colour insideColour, ItemStack itemStack) {
		super(left, top, right, bottom, borderColour, insideColour);
		this.stack = itemStack;
		this.properties = ItemUtils.getItemModularProperties(stack);
		this.propertiesToList = NBTTagAccessor.getMap(properties).keySet();
		this.left *= 2.0;
		this.right *= 2.0;
		this.top *= 2.0;
		this.bottom *= 2.0;
	}

	public void draw() {
		GL11.glPushMatrix();
		GL11.glScaled(0.50, 0.50, 0.50);
		drawBackground();
		int xoffset = 8;
		int yoffset = 8;
		int i = 0;
		for (String propName : propertiesToList) {
			double propValue = ItemUtils.getDoubleOrZero(properties, propName);
			String propValueString = String.format("%.2f", propValue);
			int strlen = MuseGui.getFontRenderer().getStringWidth(
					propValueString);
			MuseRenderer.drawString(propName, left + xoffset,
					top + yoffset + i * 10);
			MuseRenderer.drawString(propValueString, right - xoffset
					- strlen - 40,
					top + yoffset + i * 10);
			i++;
		}
		GL11.glPopMatrix();

	}
}
