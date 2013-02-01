package net.machinemuse.general.geometry;

import java.nio.DoubleBuffer;

public class DrawableMuseRect extends MuseRect {
	Colour insideColour;
	Colour outsideColour;
	DoubleBuffer points;

	public DrawableMuseRect(double left, double top, double right, double bottom, boolean growFromMiddle) {
		super(left, top, right, bottom, growFromMiddle);
		ul = new MusePoint2D(left, top);
		br = new MusePoint2D(right, bottom);
		if (growFromMiddle) {
			MusePoint2D center = ul.plus(br).times(0.5);
			this.ul = new FlyFromPointToPoint2D(center, ul, 200);
			this.br = new FlyFromPointToPoint2D(center, br, 200);
		}
	}

	public DrawableMuseRect(double left, double top, double right, double bottom) {
		this(left, top, right, bottom, false);
	}

	public DrawableMuseRect(MusePoint2D ul, MusePoint2D br) {
		super(ul, br);
	}

}
