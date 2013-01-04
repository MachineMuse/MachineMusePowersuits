package net.machinemuse.powersuits.gui.frame;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;

public class ScrollableFrame implements IGuiFrame {
	protected int totalsize;
	protected int currentscrollpixels;
	protected int buttonsize = 5;

	protected Point2D topleft;
	protected Point2D bottomright;
	protected Colour borderColour;
	protected Colour insideColour;

	public ScrollableFrame(Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour) {
		this.topleft = topleft;
		this.bottomright = bottomright;
		this.borderColour = borderColour;
		this.insideColour = insideColour;
	}

	protected double getScrollAmount() {
		return 8;
	}

	@Override
	public void draw() {
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 8);
	}

	// @Override
	// public boolean hitBox(int x, int y) {
	// if (x > left && x < right && y > top && y < bottom) {
	// if ((y - top) < buttonsize && currentscrollpixels > 0) {
	// currentscrollpixels -= getScrollAmount();
	// } else if ((bottom - y) < buttonsize
	// && currentscrollpixels + bottom - top < totalsize) {
	// currentscrollpixels += getScrollAmount();
	// }
	//
	// return true;
	// } else {
	// return false;
	// }
	// }

	@Override
	public void onMouseDown(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseMove(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMouseUp(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<String> getToolTip(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

}
