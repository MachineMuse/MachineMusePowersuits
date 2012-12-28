package net.machinemuse.powersuits.gui;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;

public abstract class GuiFrame {
	protected Colour borderColour, insideColour;
	protected int left, top, right, bottom;

	public GuiFrame(int left, int top, int right, int bottom,
			Colour borderColour, Colour insideColour) {
		super();
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.borderColour = borderColour;
		this.insideColour = insideColour;
	}

	public void drawBackground() {
		MuseRenderer.drawFrameRect(left, top, right, bottom, borderColour,
				insideColour, 0, 8);
	}

	abstract public void draw();
}
