package net.machinemuse_old.general.gui.clickable;

import net.machinemuse_old.numina.geometry.MusePoint2D;

import java.util.List;

public interface IClickable {

	void draw();

	void move(double x, double y);

	MusePoint2D getPosition();

	boolean hitBox(double x, double y);

	List getToolTip();
}