package net.machinemuse.general.geometry;

public class MuseRelativeRect extends MuseRect {

	public MuseRelativeRect(double left, double top, double right, double bottom) {
		super(left, top, right, bottom);
	}

	public MuseRelativeRect(double left, double top, double right, double bottom, boolean growFromMiddle) {
		super(left, top, right, bottom, growFromMiddle);
	}

	public MuseRelativeRect(MusePoint2D ul, MusePoint2D br) {
		super(ul, br);
	}

}
