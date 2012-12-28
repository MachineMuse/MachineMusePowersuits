package net.machinemuse.powersuits.gui;

import java.util.Arrays;
import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.powersuits.item.IModularItem;
import net.machinemuse.powersuits.item.TinkerAction;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.GL11;

public class ItemInfoFrame extends GuiFrame {
	public static final double SCALEFACTOR = 0.5;
	protected ItemStack stack;
	protected TinkerAction previewAction;

	public ItemInfoFrame(int left, int top, int right, int bottom,
			Colour borderColour, Colour insideColour, ItemStack itemStack) {
		super(left, top, right, bottom, borderColour, insideColour);
		this.stack = itemStack;
		this.left /= SCALEFACTOR;
		this.right /= SCALEFACTOR;
		this.top /= SCALEFACTOR;
		this.bottom /= SCALEFACTOR;
	}

	@Override
	public void draw() {
		GL11.glPushMatrix();
		GL11.glScaled(SCALEFACTOR, SCALEFACTOR, SCALEFACTOR);
		drawBackground();
		int xoffset = 8;
		int yoffset = 8;
		int i = 0;
		List<String> info = ((IModularItem) stack.getItem()).getLongInfo(stack);
		for (String infostring : info) {
			String[] str = infostring.split("\t");
			MuseRenderer.drawStringsEvenlySpaced(Arrays.asList(str), left
					+ xoffset, right - xoffset, top + yoffset + i * 10);

			i++;
		}
		GL11.glPopMatrix();

	}

}
