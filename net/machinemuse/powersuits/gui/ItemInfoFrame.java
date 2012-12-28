package net.machinemuse.powersuits.gui;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.ItemUtils;
import net.machinemuse.powersuits.item.TinkerAction;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class ItemInfoFrame extends GuiFrame {
	protected ItemStack stack;
	protected TinkerAction previewAction;

	public ItemInfoFrame(int left, int top, int right, int bottom,
			Colour borderColour, Colour insideColour, ItemStack itemStack) {
		super(left, top, right, bottom, borderColour, insideColour);
		this.stack = itemStack;
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
		List<String> info = ((IModularItem) stack.getItem()).getLongInfo();
		for (String propName : propertiesToList) {
			double propValue = ItemUtils.getDoubleOrZero(
					ItemUtils.getItemModularProperties(stack), propName);
			String propValueString = String.format("%.2f", propValue);
			int strlen = MuseGui.getFontRenderer().getStringWidth(
					propValueString);
			MuseRenderer.drawString(propName, left + xoffset,
					top + yoffset + i * 10, borderColour);
			MuseRenderer.drawString(propValueString, right - xoffset
					- strlen - 40,
					top + yoffset + i * 10, borderColour);
			i++;
		}
		GL11.glPopMatrix();

	}

}
