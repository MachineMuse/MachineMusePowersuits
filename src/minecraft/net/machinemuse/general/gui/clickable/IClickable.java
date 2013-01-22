package net.machinemuse.general.gui.clickable;

import java.util.List;

import net.machinemuse.general.geometry.Point2D;

public interface IClickable {

	public abstract void draw();

	public abstract void move(double x, double y);

	public abstract Point2D getPosition();

	public abstract boolean hitBox(double x, double y);

	public abstract List<String> getToolTip();
}
