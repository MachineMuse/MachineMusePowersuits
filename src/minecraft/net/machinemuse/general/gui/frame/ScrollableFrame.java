package net.machinemuse.general.gui.frame;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.DrawableMuseRect;
import net.machinemuse.general.geometry.MusePoint2D;

import org.lwjgl.input.Mouse;

public class ScrollableFrame implements IGuiFrame {
	protected int totalsize;
	protected int currentscrollpixels;
	protected int buttonsize = 5;
	protected boolean scrollbarPicked = false;
	protected boolean scrolldownPicked = false;
	protected boolean scrollupPicked = false;

	protected DrawableMuseRect border;

	public ScrollableFrame(MusePoint2D topleft, MusePoint2D bottomright,
			Colour borderColour, Colour insideColour) {
		border = new DrawableMuseRect(topleft, bottomright, borderColour, insideColour);
	}

	protected double getScrollAmount() {
		return 8;
	}

	@Override
	public void update(double x, double y) {
		if (Mouse.isButtonDown(0) && x > border.left() && x < border.right() && y > border.top() && y < border.bottom()) {
			if ((y - border.top()) < buttonsize && currentscrollpixels > 0) {
				currentscrollpixels -= getScrollAmount();
			} else if ((border.bottom() - y) < buttonsize
					&& currentscrollpixels + border.bottom() - border.top() < totalsize) {
				currentscrollpixels += getScrollAmount();
			}
		}
	}

	@Override
	public void draw() {
		border.draw();
		if (currentscrollpixels < totalsize) {

		}
	}

	@Override
	public void onMouseDown(double x, double y, int button) {

	}

	@Override
	public void onMouseUp(double x, double y, int button) {

	}

	@Override
	public List<String> getToolTip(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

}
