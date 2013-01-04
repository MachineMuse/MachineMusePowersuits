package net.machinemuse.powersuits.gui.frame;

import java.util.List;

import net.machinemuse.general.geometry.Colour;
import net.machinemuse.general.geometry.MuseRenderer;
import net.machinemuse.general.geometry.Point2D;

public class ItemSelectionFrame implements IGuiFrame {
	protected Point2D topleft;
	protected Point2D bottomright;
	protected Colour borderColour;
	protected Colour insideColour;

	public ItemSelectionFrame(Point2D topleft, Point2D bottomright,
			Colour borderColour, Colour insideColour) {
		this.topleft = topleft;
		this.bottomright = bottomright;
		this.borderColour = borderColour;
		this.insideColour = insideColour;
	}

	@Override
	public void draw() {
		// TODO Auto-generated method stub
		MuseRenderer.drawFrameRect(topleft, bottomright, borderColour,
				insideColour, 0, 8);
	}

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
