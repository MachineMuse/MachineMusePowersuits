package net.machinemuse.numina.api.gui;

import net.machinemuse.numina.utils.math.geometry.MusePoint2D;

import java.util.List;

public interface IClickable {
	void draw();

	void move(double x, double y);

	MusePoint2D getPosition();

	boolean hitBox(double x, double y);

	List getToolTip();
}
